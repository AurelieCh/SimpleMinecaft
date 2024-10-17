package entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import registeries.BlockRegistry;

@NoArgsConstructor(force = true)
@Setter
@Getter
public class World {
    private final int width;
    private final int height;
    private final int depth;
    private final Block[][][] blocks;

    public World(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.blocks = new Block[width][height][depth];

        // Fill the world with blocks (e.g., fill with grass)
        fillWorldWithBlocks();
    }

    /**
     * Remplir le monde de blocks
     */
    private void fillWorldWithBlocks() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    if(y == 0) { // On rempli le sol
                        blocks[x][y][z] = BlockRegistry.getBlockById(1);
                    } else {
                        blocks[x][y][z] = null;
                    }
                }
            }
        }
    }

    public Block getBlockId(int x, int y, int z) {
        return blocks[x][y][z];
    }
}
