package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestQuery {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ma_base?allowPublicKeyRetrieval=true&useSSL=false";
        String user = "root";
        String password = "Eminem01210!";

        try {
            // Charger explicitement le driver MySQL (optionnel avec les versions récentes)
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            // Note les backticks autour de Character
            ResultSet rs = stmt.executeQuery("SELECT * FROM `Character`");

            while (rs.next()) {
                System.out.println("Id: " + rs.getInt("Id"));
                System.out.println("Type: " + rs.getString("Type"));
                System.out.println("Name: " + rs.getString("Name"));
                System.out.println("LifePoints: " + rs.getInt("LifePoints"));
                System.out.println("Strength: " + rs.getInt("Strength"));
                System.out.println("OffensiveEquipment: " + rs.getString("OffensiveEquipment"));
                System.out.println("DefensiveEquipment: " + rs.getString("DefensiveEquipment"));
                System.out.println("----------");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
