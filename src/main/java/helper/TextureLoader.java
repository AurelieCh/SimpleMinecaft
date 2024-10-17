package helper;

import lombok.extern.log4j.Log4j2;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.system.MemoryStack.stackPush;

@Log4j2
public class TextureLoader {

    /**
     * Fonction qui charge une texture à partir d'un chemin de fichier et la convertit en OpenGL texture ID.
     *
     * @param filePath Chemin du fichier image de la texture.
     * @return L'ID de la texture dans OpenGL.
     */
    public static int loadTexture(String filePath) {
        // Vérification du contexte GLFW
        if (GLFW.glfwGetCurrentContext() == 0) {
            log.error("Le contexte GLFW n'est pas chargé.");
            throw new IllegalStateException("Le contexte GLFW n'est pas chargé.");
        }

        int textureId;

        // Utilisez un MemoryStack pour gérer la mémoire
        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1); // Largeur de l'image
            IntBuffer h = stack.mallocInt(1); // Hauteur de l'image
            IntBuffer comp = stack.mallocInt(1); // Composantes de couleur

            // Chargez l'image
            STBImage.stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = STBImage.stbi_load(filePath, w, h, comp, 4); // 4 pour RGBA

            if (image == null) {
                throw new RuntimeException("Erreur lors du chargement de la texture à l'adresse " + filePath +
                        ".\nRaison "+ STBImage.stbi_failure_reason());
            }

            // Créez la texture
            try{
                textureId = GL11.glGenTextures();
            } catch (Exception e){
                throw new RuntimeException("Erreur lors de la création de la texture", e);
            }

            glBindTexture(GL12.GL_TEXTURE_3D, textureId);
            glTexImage2D(GL12.GL_TEXTURE_3D, 0, GL_RGBA, w.get(), h.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL12.GL_TEXTURE_3D);

            STBImage.stbi_image_free(image); // Libérez l'image une fois qu'elle a été chargée
        }

        // Configurez les paramètres de la texture
        glTexParameteri(GL12.GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL12.GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        return textureId; // Retourne l'ID de la texture
    }
}

