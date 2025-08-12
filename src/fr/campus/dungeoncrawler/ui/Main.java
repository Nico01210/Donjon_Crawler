package fr.campus.dungeoncrawler.ui;

import db.DatabaseManager;
import fr.campus.dungeoncrawler.Board.*;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.enemy.Dragon;
import fr.campus.dungeoncrawler.enemy.Gobelin;
import fr.campus.dungeoncrawler.enemy.Sorcier;
import fr.campus.dungeoncrawler.item.*;
import fr.campus.dungeoncrawler.game.Game;
import fr.campus.dungeoncrawler.exception.OutOfBoardException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final int FINAL_POSITION = 64;
    private static Board customBoard;

    public static void main(String[] args) {
        // 🎬 Afficher le titre ASCII avec effet machine à écrire
        afficherTitreAnime("src/ressources/title.txt");

        DatabaseManager dbManager = new DatabaseManager();
        Scanner scanner = new Scanner(System.in);

        try {
            dbManager.connect();

            boolean running = true;

            while (running) {
                System.out.println("\n=== MENU PRINCIPAL ===");
                System.out.println("1. Afficher et choisir un héros");
                System.out.println("2. Créer un nouveau héros");
                System.out.println("3. Créer un plateau personnalisé");
                System.out.println("4. Quitter");
                System.out.print("Choix : ");

                int choix = scanner.nextInt();
                scanner.nextLine(); // Consommer le retour à la ligne

                switch (choix) {
                    case 1 -> {
                        List<Character> heroes = dbManager.getHeroesList();
                        if (heroes.isEmpty()) {
                            System.out.println("Aucun héros trouvé.");
                        } else {
                            System.out.println("Héros existants :");
                            for (int i = 0; i < heroes.size(); i++) {
                                System.out.printf("%d - %s%n", i + 1, heroes.get(i).getName());
                            }
                            System.out.print("Choisissez un héros (numéro) pour lancer la partie, ou 0 pour revenir : ");
                            int heroChoice = scanner.nextInt();
                            scanner.nextLine();

                            if (heroChoice > 0 && heroChoice <= heroes.size()) {
                                Character selectedHero = heroes.get(heroChoice - 1);

                                // ✅ recrée un plateau neuf à chaque lancement de partie
                                Board freshBoard = createCustomBoard();

                                startGame(selectedHero, dbManager);
                            }
                        }
                    }

                    case 2 -> {
                        System.out.print("Nom du héros : ");
                        String name = scanner.nextLine();

                        // Choix de la classe
                        System.out.print("Classe (warrior/wizard) : ");
                        String classType = scanner.nextLine().toLowerCase();

                        System.out.print("Points de vie : ");
                        int health = scanner.nextInt();

                        System.out.print("Attaque : ");
                        int attack = scanner.nextInt();

                        System.out.print("Force : ");
                        int strength = scanner.nextInt();
                        scanner.nextLine(); // consommer retour

                        Weapon sword = new Weapon("Épée standard", 10);
                        Character newHero;

                        if (classType.equals("wizard")) {
                            newHero = new Wizard(0, name, health, attack, strength, sword);
                        } else {
                            newHero = new Warrior(0, name, health, attack, strength, sword);
                        }

                        dbManager.createHero(newHero);
                        System.out.println("✅ Héros créé avec succès !");
                    }

                    case 3 -> {
                        customBoard = createCustomBoard();
                        System.out.println("✅ Plateau personnalisé prêt !");
                    }

                    case 4 -> running = false;

                    default -> System.out.println("❌ Choix invalide !");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } finally {
            try {
                dbManager.close();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture de la BDD.");
            }
        }

        System.out.println("👋 Fin du programme.");
        scanner.close();
    }

    private static void startGame(Character hero, DatabaseManager dbManager) {
        System.out.println("\n=== Lancement de la partie avec le héros : " + hero.getName() + " ===");

        try {
            Game game = new Game(hero, dbManager);

            // ⛔️ Ne pas réutiliser le board existant
            // ✅ Recrée une copie neuve du plateau à chaque partie
            Board freshBoard = Main.createCustomBoard();
            game.setBoard(freshBoard);

            game.play();
        } catch (OutOfBoardException e) {
            System.out.println("Erreur de jeu : " + e.getMessage());
        }
    }

    public static Board createCustomBoard() {
        Board customBoard = new Board(true);

        // Placer ennemis, potions, armes, sorts
        customBoard.setCell(0, 0, new EnemyCell(customBoard, 0, 0, new Gobelin()));
        customBoard.setCell(0, 2, new EnemyCell(customBoard, 0, 2, new Sorcier()));
        customBoard.setCell(1, 3, new EnemyCell(customBoard, 1, 3, new Dragon()));

        customBoard.setCell(1, 1, new PotionCell(new Potion("potion", 5)));
        customBoard.setCell(2, 2, new PotionCell(new GrandePotion()));
        customBoard.setCell(3, 0, new WeaponCell(customBoard, 3, 0, new Epee()));
        customBoard.setCell(3, 1, new WeaponCell(customBoard, 3, 1, new Massue()));
        customBoard.setCell(4, 0, new SpellCell(new Eclair()));
        customBoard.setCell(4, 1, new SpellCell(new BouleDeFeu()));

        // Remplir le reste avec EmptyCell
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (customBoard.getCell(x, y) == null) {
                    customBoard.setCell(x, y, new EmptyCell(customBoard, x, y));
                }
            }
        }

        // Afficher le plateau
        System.out.println("\n🗺️ Plateau personnalisé :\n");
        customBoard.displayBoard();

        return customBoard;
    }

    /**
     * Affiche le titre ASCII en lisant un fichier et en affichant caractère par caractère
     */
    private static void afficherTitreAnime(String cheminFichier) {
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                for (char c : ligne.toCharArray()) {
                    System.out.print(c);
                    Thread.sleep(2); // vitesse d’affichage
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du titre : " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
