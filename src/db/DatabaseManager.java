package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.item.Weapon;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/game_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "Eminem01210!";

    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connexion réussie !");
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Connexion fermée.");
        }
    }

    // Récupère la liste des héros depuis la BDD
    public List<Character> getHeroesList() throws SQLException {
        List<Character> heroes = new ArrayList<>();
        String query = "SELECT * FROM `Character`";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("Id");
                String type = rs.getString("Type");
                String name = rs.getString("Name");
                int lifePoints = rs.getInt("LifePoints");
                int strength = rs.getInt("Strength");
                String offEquip = rs.getString("OffensiveEquipment");

                // Création simplifiée : on crée un Warrior (à adapter selon type)
                Weapon weapon = (offEquip != null) ? new Weapon(offEquip, 10) : null;
                Character hero = new Warrior(id, name, lifePoints, lifePoints, strength, weapon);

                heroes.add(hero);
            }
        }

        return heroes;
    }

    public void createHero(Character hero) throws SQLException {
        String insertSQL = "INSERT INTO `Character` (Type, Name, LifePoints, Strength, OffensiveEquipment, DefensiveEquipment) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, hero.getClass().getSimpleName());
            pstmt.setString(2, hero.getName());
            pstmt.setInt(3, hero.getHealth());
            pstmt.setInt(4, hero.getStrength());

            String offEquip = (hero.getOffensiveItem() != null) ? hero.getOffensiveItem().getName() : null;
            pstmt.setString(5, offEquip);

            String defEquip = null; // À adapter si defensive equipment existe
            pstmt.setString(6, defEquip);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Personnage inséré avec succès !");
            } else {
                System.out.println("Erreur lors de l'insertion du personnage.");
            }
        }
    }
    public void deleteHeroById(int id) throws SQLException {
        String sql = "DELETE FROM `Character` WHERE Id = ?";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                System.out.println("Aucun personnage trouvé avec l'ID " + id);
            }
        }
    }
    public void editHero(Character hero) throws SQLException {
        String updateSQL = "UPDATE `Character` SET Type = ?, Name = ?, LifePoints = ?, Strength = ?, OffensiveEquipment = ?, DefensiveEquipment = ? WHERE Id = ?";

        try (var pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, hero.getClass().getSimpleName());
            pstmt.setString(2, hero.getName());
            pstmt.setInt(3, hero.getHealth());
            pstmt.setInt(4, hero.getStrength());

            String offEquip = (hero.getOffensiveItem() != null) ? hero.getOffensiveItem().getName() : null;
            pstmt.setString(5, offEquip);

            String defEquip = null; // adapte si tu as defensive item
            pstmt.setString(6, defEquip);

            pstmt.setInt(7, hero.getId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Personnage mis à jour avec succès !");
            } else {
                System.out.println("Erreur lors de la mise à jour du personnage.");
            }
        }
    }


    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();

        try {
            dbManager.connect();
            dbManager.getHeroesList();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dbManager.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
