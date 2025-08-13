package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.game.GameStatus;

@JsonTypeName("basic")
public class BasicCell extends Cell {

    // Constructeur par défaut requis pour Jackson
    public BasicCell() {
        super();
    }

    public BasicCell(Board board, int x, int y) {
        super(board, x, y);
    }

    @Override
    public void interact(Character character, GameStatus gameStatus) {
        // Exemple d'action quand un personnage entre dans la case
        System.out.println(character.getName() + " est entré dans la case " + getPosition());
    }
}
