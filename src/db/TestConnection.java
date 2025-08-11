package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/game_db"; // nom de ta BDD
        String user = "root"; // ton utilisateur MySQL
        String password = "Eminem01210!"; // ton mot de passe MySQL

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie !");
            conn.close();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
    }
}
