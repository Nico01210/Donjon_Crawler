package fr.campus.dungeoncrawler.Board;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.game.GameStatus;

public class BasicCell extends Cell {

    @Override
    public void interact(Character character, GameStatus gameStatus) {
        // Exemple d'action quand un personnage entre dans la case
        System.out.println(character.getName() + " est entré dans la case " + getPosition());
    }
}
