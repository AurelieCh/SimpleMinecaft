import entities.Block;
import entities.Window;
import exceptions.WindowsCreationException;
import exceptions.WindowsInitializationException;
import lombok.extern.log4j.Log4j2;

import static org.lwjgl.opengl.GL11C.*;

@Log4j2
public class Main {

    public static void main(String[] args) {
        Window window = new Window(800, 600, "Simple Minecraft", true, true);

        try {
            window.create();

            Block grassBlock = new Block("Herbe", 1, "texture_herbe.png", true, true); // Ensure you have the correct path

            // Game loop
            while (!window.closed()) {
                // Clear the window
                window.setClearColor(0.5f, 0.7f, 1.0f, 0.0f); // Set clear color
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the buffer

                // Update projection matrix
                window.updateProjectionMatrix();

                // Render the block
                grassBlock.render();

                // Update the window
                window.update();
            }
        } catch (WindowsInitializationException | WindowsCreationException e) {
            log.error("Une erreur est survenue lors de l'initialisation de la fenêtre", e);
        } finally {
            window.cleanup();
        }
    }
}
