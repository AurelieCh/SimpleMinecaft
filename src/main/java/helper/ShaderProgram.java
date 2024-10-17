package helper;

import exceptions.shaders.ShadersCompilationException;
import exceptions.shaders.ShadersLinkingException;
import exceptions.shaders.ShadersValidatingException;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ShaderProgram {
    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    public ShaderProgram(String vertexPath, String fragmentPath) throws IOException, ShadersLinkingException, ShadersValidatingException, ShadersCompilationException {
        vertexShaderID = loadShader(vertexPath, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentPath, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        GL20.glLinkProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0) {
            throw new ShadersLinkingException("Erreur lors de la liaison des shaders : " + GL20.glGetProgramInfoLog(programID));
        }
        GL20.glValidateProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0) {
            throw new ShadersValidatingException("Erreur lors de la validation des shaders : " + GL20.glGetProgramInfoLog(programID));
        }
    }

    private int loadShader(String filePath, int type) throws IOException, ShadersCompilationException {
        String source = new String(Files.readAllBytes(Paths.get(filePath)));
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, source);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0) {
            throw new ShadersCompilationException("Error compiling shader: " + GL20.glGetShaderInfoLog(shaderID));
        }
        return shaderID;
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanup() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    public int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    public void setUniformMatrix4f(String uniformName, org.joml.Matrix4f matrix) {
        int location = getUniformLocation(uniformName);
        if (location != -1) {
            GL20.glUniformMatrix4fv(location, false, matrix.get(new float[16]));
        }
    }
}
