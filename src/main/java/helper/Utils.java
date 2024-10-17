package helper;

import lombok.extern.log4j.Log4j2;
import org.lwjgl.glfw.GLFW;

@Log4j2
public class Utils {
    public static void checkContext(){
        // Vérification du contexte GLFW
        if (GLFW.glfwGetCurrentContext() == 0) {
            log.error("Contexte GLFW non chargé.");
            throw new IllegalStateException("Contexte GLFW non chargé.");
        } else {
            log.info("Contexte GLFW chargé.");
        }
    }
}
