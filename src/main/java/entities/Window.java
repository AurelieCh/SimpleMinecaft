package entities;

import exceptions.WindowsCreationException;
import exceptions.WindowsInitializationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static constants.SimpleMinecraftConstantes.*;
import static helper.Utils.checkContext;
import static org.lwjgl.opengl.GL11.*;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Setter
@Getter
@Log4j2
public class Window {
    private int width;
    private int height;
    private final String title;
    private final boolean vSync;
    private boolean resizable;

    private long window;

    private final Matrix4f projectionMatrix;

    // Map pour stocker les textures chargées
    private Map<String, Integer> textureMap = new LinkedHashMap<>();

    public Window(int width, int height, String title, boolean vSync, boolean resiezeable) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
        this.resizable = resiezeable;

        this.projectionMatrix = new Matrix4f();
    }

    public Window(int width, int height, String title, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;

        this.projectionMatrix = new Matrix4f();
    }

    /**
     * Fonction qui permet d'initier la création d'une fenêtre
     *
     * @throws WindowsInitializationException Si une erreur est survenue lors de l'initialisation de LWJGL
     * @throws WindowsCreationException       Si une erreur est survenue lors de la création de la fenêtre
     */
    public void create() throws WindowsInitializationException, WindowsCreationException {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            log.error("Erreur lors de l'initialisation de LWJGL");
            throw new WindowsInitializationException();
        }

        // Configuration de la fenêtre
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE); // Cacher la fenêtre
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE); // Désactiver le redimensionnement
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3); // Version OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2); // Version OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE); // Important for modern OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE); // Important for modern OpenGL

        boolean maximized = false;

        if(height == 0 || width == 0){
            this.width = 100;
            this.height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximized = true;
        }

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        if (window == MemoryUtil.NULL) {
            log.error("Erreur lors de la création de la fenêtre");
            throw new WindowsCreationException();
        }

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            setResizable(true);
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        if(maximized){
            GLFW.glfwMaximizeWindow(window);
        } else {
            GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (videoMode.width() - width) / 2,
                    (videoMode.height() - height) / 2);
        }

        GLFW.glfwMakeContextCurrent(window);
        // Create OpenGL capabilities
        GL.createCapabilities();

        // Vérification du contexte GLFW
        checkContext();

        if(isvSync()){
            GLFW.glfwSwapInterval(1);
        }

        // Afficher la fenêtre
        GLFW.glfwShowWindow(window);

        // Configuration de OpenGL
        glClearColor(0.5f, 0.7f, 1.0f, 0.0f); // Couleur de fond (bleu clair)
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(GL_BACK);
    }

    /**
     * Fonction qui permet de savoir si la fenêtre doit être fermé (appuie sur croix)
     *
     * @return Le booléen true pour doit être fermé, false sinon
     */
    public boolean closed() {
        return GLFW.glfwWindowShouldClose(window);
    }

    /**
     * Fonction qui permet de mettre à jour la fenêtre
     */
    public void update() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    /**
     * Fonction qui permet de nettoyer les ressources
     */
    public void cleanup() {
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    /**
     * Fonction qui permet de set la couleur de fond
     */
    public void setClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    /**
     * Fonction qui permet de savoir si une touche est appuyée
     * @param keyCode Le code de la touche
     * @return Le booléen true si la touche est appuyée, false sinon
     */
    public boolean isKeyPressed(int keyCode) {
        return GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height) {
        float aspectRatio = (float) width / height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public boolean isvSync() {
        return vSync;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public long getWindow() {
        return window;
    }

    public void setWindow(long window) {
        this.window = window;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Map<String, Integer> getTextureMap() {
        return textureMap;
    }

    public void setTextureMap(Map<String, Integer> textureMap) {
        this.textureMap = textureMap;
    }
}