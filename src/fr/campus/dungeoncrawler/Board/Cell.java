package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.campus.dungeoncrawler.Board.Board;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.enemy.Enemy;
import fr.campus.dungeoncrawler.game.GameStatus;
import fr.campus.dungeoncrawler.item.Item;



@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EnemyCell.class, name = "enemy"),
        @JsonSubTypes.Type(value = PotionCell.class, name = "potion"),
        // ... autres sous-classes
})

/*
 * Représente une case abstraite du plateau de jeu.
 */
public abstract class Cell {
    private int id;
    private Enemy enemy;
    private int position;
    private Item item;
    private Character character;
    protected Board board;
    protected int x;
    protected int y;

    public Cell(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public Cell() {}

    // ajoute cette méthode
    public void setBoard(Board board) {
        this.board = board;
    }

    // getters et setters si besoin
    public Board getBoard() {
        return board;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract void interact(Character character, GameStatus gameStatus);

    public int getId() {
        return id;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void removeEnemy() {
        enemy = null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void updateEnemyStatus() {
        if (enemy != null && enemy.isDead()) {
            removeEnemy();
        }
    }
}