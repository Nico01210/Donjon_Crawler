package fr.campus.dungeoncrawler.dao;

import fr.campus.dungeoncrawler.Board.BasicCell;
import fr.campus.dungeoncrawler.Board.Board;
import fr.campus.dungeoncrawler.Board.Cell;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.enemy.Enemy;
import fr.campus.dungeoncrawler.item.Item;
import fr.campus.dungeoncrawler.item.Weapon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion du plateau de jeu (Board) avec la base de données.
 */
public class boardDAO {

    private final Connection connection;

    public boardDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Sauvegarde un plateau complet avec ses cellules dans la base de données.
     */
    public void saveBoard(Board board) throws SQLException {
        // 1. Enregistrer le plateau
        String sqlBoard = "INSERT INTO Board (name) VALUES (?)";
        PreparedStatement psBoard = connection.prepareStatement(sqlBoard, Statement.RETURN_GENERATED_KEYS);
        psBoard.setString(1, board.getName());
        psBoard.executeUpdate();

        ResultSet rs = psBoard.getGeneratedKeys();
        if (rs.next()) {
            board.setId(rs.getInt(1));
        }

        // 2. Enregistrer chaque cellule du plateau
        for (Cell cell : board.getCells()) {
            String sqlCell = "INSERT INTO Cell (board_id, position, item_id, character_id) VALUES (?, ?, ?, ?)";
            PreparedStatement psCell = connection.prepareStatement(sqlCell);
            psCell.setInt(1, board.getId());
            psCell.setInt(2, cell.getPosition());
            psCell.setObject(3, cell.getItem() != null ? cell.getItem().getId() : null);
            psCell.setObject(4, cell.getCharacter() != null ? cell.getCharacter().getId() : null);
            psCell.executeUpdate();
        }
    }

    /**
     * Récupère un plateau complet depuis la base de données.
     */
    public Board getBoard(int boardId) throws SQLException {
        Board board = new Board(true);
        board.setId(boardId);

        // 1. Récupérer le nom du plateau
        String sqlBoard = "SELECT name FROM Board WHERE id = ?";
        PreparedStatement psBoard = connection.prepareStatement(sqlBoard);
        psBoard.setInt(1, boardId);
        ResultSet rsBoard = psBoard.executeQuery();
        if (rsBoard.next()) {
            board.setName(rsBoard.getString("name"));
        }

        // 2. Récupérer les cellules associées
        String sqlCells = "SELECT * FROM Cell WHERE board_id = ?";
        PreparedStatement psCells = connection.prepareStatement(sqlCells);
        psCells.setInt(1, boardId);
        ResultSet rsCells = psCells.executeQuery();

        List<Cell> cells = new ArrayList<>();
        while (rsCells.next()) {
            Cell cell = new BasicCell();
            cell.setId(rsCells.getInt("id"));
            cell.setPosition(rsCells.getInt("position"));

            int itemId = rsCells.getInt("item_id");
            int characterId = rsCells.getInt("character_id");

            if (itemId != 0) cell.setItem(getItemById(itemId));
            if (characterId != 0) cell.setCharacter(getCharacterById(characterId));

            cells.add(cell);
        }

        board.setCells(cells);
        return board;
    }

    /**
     * Récupère un objet Item depuis la base de données par son ID.
     */
    private Item getItemById(int id) throws SQLException {
        String sql = "SELECT * FROM Item WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String type = rs.getString("type");
            String name = rs.getString("name");
            int power = rs.getInt("attack_power");

            if ("weapon".equalsIgnoreCase(type)) {
                return new Weapon(name, power);
            } else if ("potion".equalsIgnoreCase(type)) {
                // return new Potion(name, healAmount);
            }
            // Ajouter d'autres types si besoin
        }
        return null;
    }

    /**
     * Récupère un personnage depuis la base de données par son ID.
     */
    public Character getCharacterById(int id) throws SQLException {
        String sql = "SELECT * FROM `Character` WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            // Ici on suppose un Enemy pour simplifier. À adapter si Hero aussi possible.
            return new Enemy(
                    id,
                    rs.getString("name"),
                    rs.getInt("health"),
                    rs.getInt("attack"),
                    rs.getInt("strength"),
                    null // Ajoute l'Item offensif si nécessaire
            );
        }
        return null;
    }
}
