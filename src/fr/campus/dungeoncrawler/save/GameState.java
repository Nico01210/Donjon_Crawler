package fr.campus.dungeoncrawler.save;

import fr.campus.dungeoncrawler.Board.Board;
import fr.campus.dungeoncrawler.Board.Cell;
import fr.campus.dungeoncrawler.character.Character;

import java.util.List;

public class GameState {

    private Character player;
    private int position;
    private List<Cell> board;
    public GameState() {} // requis pour Jackson


    public GameState(Character player, int position, List<Cell> board) {
        this.player = player;
        this.position = position;
        this.board = board;
    }

    public Character getPlayer() {
        return player;
    }

    public int getPosition() {
        return position;
    }

    public List<Cell> getBoard() {
        return board;
    }
}
