package entities;

import helper.TextureLoader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static constants.SimpleMinecraftConstantes.TEXTURE_PATH;
import static constants.SimpleMinecraftConstantes.VERTICES_CUBE;
import static helper.Utils.checkContext;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Setter
@Getter
@Log4j2
public class Block {
    private final String name;
    private final int id;
    private final String texture;
    private final boolean solid;
    private final boolean breakable;
    private int textureId;
    private int vbo;
    private int vao;

    public Block(String name, int id, String texture, boolean solid, boolean breakable) {
        this.name = name;
        this.id = id;
        this.solid = solid;
        this.texture = texture;
        this.breakable = breakable;

        loadTexture();
    }

    public void loadTexture() {
        textureId = TextureLoader.loadTexture(TEXTURE_PATH + texture);

        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VERTICES_CUBE, GL15.GL_STATIC_DRAW);

        // Position attribute
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        // Texture Coordinate attribute
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);
    }

    public void render(Window window) {
        // Vérification du contexte GLFW
        checkContext();

        // Use the shader program
        window.getShaderProgram().start();

        // Set the uniforms for the shader
        window.getShaderProgram().setUniformMatrix4f("model", new Matrix4f());
        window.getShaderProgram().setUniformMatrix4f("view", window.getViewMatrix());
        window.getShaderProgram().setUniformMatrix4f("projection", window.getProjectionMatrix());

        // Bind the block's texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // Bind the VAO
        GL30.glBindVertexArray(vao);

        // Draw the cube
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 36);

        // Unbind the VAO
        GL30.glBindVertexArray(0);

        // Stop using the shader program
        window.getShaderProgram().stop();
    }

    public void cleanup() {
        GL15.glDeleteBuffers(vbo);
        GL30.glDeleteVertexArrays(vao);
    }
}
