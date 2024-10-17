import entities.Block;
import entities.Window;
import exceptions.shaders.ShadersCompilationException;
import exceptions.shaders.ShadersCreationException;
import exceptions.WindowsCreationException;
import exceptions.WindowsInitializationException;
import exceptions.shaders.ShadersLinkingException;
import exceptions.shaders.ShadersValidatingException;
import lombok.extern.log4j.Log4j2;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

@Log4j2
public class Main {

    public static void main(String[] args) {
        Window window = new Window(800, 600, "Simple Minecraft", true, true);
        Block grassBlock = null;

        try {
            window.create();

            grassBlock = new Block("Herbe", 1, "texture_herbe.png", true, true); // Ensure you have the correct path

            // Set up the view matrix
            Matrix4f viewMatrix = new Matrix4f().lookAt(
                    new org.joml.Vector3f(0.0f, 1.0f, 3.0f), // Camera position
                    new org.joml.Vector3f(0.0f, 0.0f, 0.0f), // Camera target
                    new org.joml.Vector3f(0.0f, 1.0f, 0.0f)  // Camera up vector
            );
            window.setViewMatrix(viewMatrix);

            // Game loop
            while (!window.closed()) {
                // Clear the window
                window.setClearColor(0.5f, 0.7f, 1.0f, 0.0f); // Set clear color
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the buffer

                // Update projection matrix
                window.updateProjectionMatrix();

                // Render the block
                grassBlock.render(window);

                // Update the window
                window.update();
            }
        } catch (WindowsInitializationException e) {
            log.error("Une erreur est survenue lors de l'initialisation de la fenêtre", e);
            System.exit(-1);
        } catch (WindowsCreationException e){
            log.error("Une erreur est survenue lors de la création de la fenêtre", e);
            System.exit(-1);
        } catch (ShadersCreationException e) {
            log.error("Une erreur est survenue lors de la création des shaders", e);
            System.exit(-1);
        } catch (ShadersValidatingException e) {
            log.error("Une erreur est survenue lors de la validation des shaders", e);
            System.exit(-1);
        } catch (ShadersLinkingException e) {
            log.error("Une erreur est survenue lors de la liaison des shaders", e);
            System.exit(-1);
        } catch (ShadersCompilationException e) {
            log.error("Une erreur est survenue lors de la compilation des shaders", e);
            System.exit(-1);
        } finally {
            window.cleanup();
            if (grassBlock != null){
                grassBlock.cleanup();
            }
        }
    }
}
