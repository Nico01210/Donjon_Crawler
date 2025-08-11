package fr.campus.dungeoncrawler.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.campus.dungeoncrawler.Board.Cell;
import fr.campus.dungeoncrawler.character.Character;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SaveManager {

    private static final String SAVE_FILE = "savegame.json";

    public static void saveGame(Character player, List<Cell> board) {
        ObjectMapper mapper = new ObjectMapper();

        GameState state = new GameState(player, player.getPosition(), board);

        try {
            mapper.writeValue(new File(SAVE_FILE), state);
            System.out.println("💾 Sauvegarde réussie !");
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    public static GameState loadGame() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(new File(SAVE_FILE), GameState.class);
        } catch (IOException e) {
            System.out.println("❌ Erreur lors du chargement : " + e.getMessage());
            return null;
        }
    }
}
