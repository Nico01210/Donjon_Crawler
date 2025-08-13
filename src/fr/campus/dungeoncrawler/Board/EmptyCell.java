package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.game.GameStatus;

@JsonTypeName("empty")
public class EmptyCell extends Cell {
    
    // Constructeur par défaut requis pour Jackson
    public EmptyCell() {
        super();
    }

    public EmptyCell(Board board, int x, int y) {
        super(board, x, y);
    }
    @Override
    public void interact(Character character, GameStatus gameStatus) {
        System.out.println("🪨 Vous arrivez sur une case vide.");
    }
}