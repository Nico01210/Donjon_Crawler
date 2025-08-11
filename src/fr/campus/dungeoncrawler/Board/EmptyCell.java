package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.game.GameStatus;

@JsonTypeName("enemy")
public class EmptyCell extends Cell {
    private Board board;
    private int x;
    private int y;

    public EmptyCell(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }
    @Override
    public void interact(Character character, GameStatus gameStatus) {
        System.out.println("🪨 Vous arrivez sur une case vide.");
    }
}