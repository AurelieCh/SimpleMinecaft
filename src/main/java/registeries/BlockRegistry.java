package registeries;

import entities.Block;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor(force = true)
@Setter
@Getter
@Log4j2
public class BlockRegistry {
    private static final HashMap<Integer, Block> blocks = new HashMap<>();

    /**
     * Enregistre un nouveau block. Si un block avec le même ID existe déjà, il log un avertissement
     * et n'écrase pas le block existant.
     *
     * @param block Le block à enregistrer
     */
    public static void registerBlock(Block block) {
        if (blocks.containsKey(block.getId())) {
            log.error("Attention: Le block avec l'ID " + block.getId() + " est déjà enregistré.");
        } else {
            blocks.put(block.getId(), block);
        }
    }

    /**
     * Récupère un block en fonction de son ID.
     *
     * @param id L'ID du block à récupérer.
     * @return Le block correspondant à l'ID spécifié, ou null si non trouvé.
     */
    public static Block getBlockById(int id) {
        return blocks.get(id);
    }

    /**
     * Récupère une liste de tous les blocks enregistrés.
     *
     * @return Une liste de tous les blocks enregistrés.
     */
    public static List<Block> getBlocks() {
        // Return an immutable list if you don't want this list to be modified outside.
        return new ArrayList<>(blocks.values());
    }

    /**
     * Retourne une liste non modifiable des blocks enregistrés.
     *
     * @return Une liste non modifiable des blocks enregistrés.
     */
    public static List<Block> getImmutableBlocks() {
        return new ArrayList<>(Collections.unmodifiableCollection(blocks.values()));
    }
}