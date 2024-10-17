package entities;

import helper.TextureLoader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static constants.SimpleMinecraftConstantes.TEXTURE_PATH;
import static constants.SimpleMinecraftConstantes.VERTICES_CUBE;
import static helper.Utils.checkContext;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengles.GLES30.GL_TEXTURE_3D;

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

        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, VERTICES_CUBE, GL_STATIC_DRAW);

        // Position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Texture Coordinate attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }


    public void render() {
        // Vérification du contexte GLFW
        checkContext();

        // Bind the block's texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Bind the VAO
        glBindVertexArray(vao);

        // Draw the cube
        glDrawArrays(GL_TRIANGLES, 0, 36);

        // Unbind the VAO
        glBindVertexArray(0);
    }

    public void cleanup() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
    }
}
