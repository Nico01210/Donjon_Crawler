package fr.campus.dungeoncrawler.game;

import db.DatabaseManager;
import fr.campus.dungeoncrawler.Board.*;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.dice.Dice6;
import fr.campus.dungeoncrawler.enemy.Dragon;
import fr.campus.dungeoncrawler.enemy.Gobelin;
import fr.campus.dungeoncrawler.enemy.Sorcier;
import fr.campus.dungeoncrawler.exception.OutOfBoardException;
import fr.campus.dungeoncrawler.item.Potion;
import fr.campus.dungeoncrawler.save.GameState;
import fr.campus.dungeoncrawler.save.SaveManager;
import fr.campus.dungeoncrawler.ui.ConsoleUi;
import fr.campus.dungeoncrawler.Board.BoardEnemy;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Game {

    private Character player;
    private Board board;
    private final Scanner scanner;
    private final int FINAL_POSITION = 64;
    private final DatabaseManager dbManager;
    private GameStatus gameStatus;
    private final Dice6 dice = new Dice6(); // Utilisation de la classe Dice6

    public Game(Character player, DatabaseManager dbManager) {
        this.player = player;
        this.dbManager = dbManager;
        this.board = null;
        this.scanner = new Scanner(System.in);
    }

    public Game(Character player, DatabaseManager dbManager, Scanner scanner) {
        this.player = player;
        this.dbManager = dbManager;
        this.board = null;
        this.scanner = scanner;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
    public void startNewGame() {
        // Crée un joueur, un plateau neuf et un statut de jeu
        player = new Warrior("Héros");  // exemple
        board = new Board(true);
        initializeBoard();
        gameStatus = new GameStatus();
        player.setPosition(0); // position départ
    }

    public void loadGame() {
        GameState loadedState = SaveManager.loadGame();
        if (loadedState != null) {
            player = loadedState.getPlayer();
            List<Cell> cells = loadedState.getBoard();

            board = new Board(true);
            board.setCells(cells); // méthode à créer pour injecter les cellules chargées

            gameStatus = new GameStatus();
            player.setPosition(loadedState.getPosition());
        } else {
            System.out.println("Aucune sauvegarde trouvée, nouvelle partie lancée.");
            startNewGame();
        }
    }
    private void initializeBoard() {
        board = new Board(true);

        for (int i = 0; i < FINAL_POSITION; i++) {
            int x = i % Board.WIDTH;   // coordonnée x
            int y = i / Board.WIDTH;   // coordonnée y

            if ((i + 1) % 13 == 0) {
                board.setCell(x, y, new EnemyCell(board, x, y, new Dragon()));
            } else if ((i + 1) % 10 == 0) {
                board.setCell(x, y, new PotionCell(new Potion("Potion de soin", 5)));
            } else if ((i + 1) % 5 == 0) {
                board.setCell(x, y, new EnemyCell(board, x,y, new Sorcier()));
            } else if ((i + 1) % 3 == 0) {
                board.setCell(x, y, new EnemyCell(board, x, y, new Gobelin()));
            } else {
                board.setCell(x, y, new EmptyCell(board, x, y));
            }
        }
    }

    public void start() throws OutOfBoardException {
        System.out.println("=== Donjon Mystère ===");

        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Nouvelle partie");
            System.out.println("2. Charger une partie");
            System.out.println("3. Quitter");
            System.out.print("Choix : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    initializeBoard();
                    createCharacter();
                    play();
                }
                case 2 -> {
                    GameState saved = SaveManager.loadGame();
                    if (saved != null) {
                        System.out.println("Chargement de la partie sauvegardée...");
                        this.player = saved.getPlayer();

                        Board loadedBoard = new Board(true);
                        loadedBoard.setCells(saved.getBoard());
                        this.board = loadedBoard;

                        this.player.setPosition(saved.getPosition());
                        play();
                    } else {
                        System.out.println("Aucune partie sauvegardée trouvée.");
                    }
                }
                case 3 -> {
                    System.out.println("À bientôt !");
                    running = false;
                }
                default -> System.out.println("Choix invalide !");
            }
        }
    }

    private void createCharacter() {
        System.out.println("Choisissez le type de personnage (warrior/wizard) :");
        String type = scanner.nextLine().toLowerCase();

        System.out.println("Entrez le nom de votre personnage :");
        String name = scanner.nextLine();

        if (type.equals("warrior")) {
            player = new Warrior(name);
        } else if (type.equals("wizard")) {
            player = new Wizard(name);
        } else {
            System.out.println("❌ Type invalide. Création annulée.");
            return;
        }

        System.out.println("✅ Personnage créé avec succès : " + player);

        boolean editing = true;
        while (editing) {
            System.out.println("\n--- MENU PERSONNAGE ---");
            System.out.println("1. Afficher le personnage");
            System.out.println("2. Recréer un personnage");
            System.out.println("3. Lancer la partie");
            System.out.println("4. Retour au menu principal");
            System.out.println("5. Sauvegarder la partie");
            System.out.print("Choix : ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> System.out.println(player);
                case 2 -> createCharacter();
                case 3 -> {
                    try {
                        play();
                    } catch (OutOfBoardException e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                    editing = false;
                }
                case 4 -> editing = false;
                case 5 -> {
                    if (board != null && !board.isEmpty()) {
                        SaveManager.saveGame(player, board.getCells());
                        System.out.println("Partie sauvegardée !");
                    } else {
                        System.out.println("Aucun plateau chargé, impossible de sauvegarder.");
                    }
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }
    public void playerStep() throws OutOfBoardException {
        int cellIndex = player.getPosition();

        if (cellIndex >= 0 && cellIndex < board.getSize()) {
            int x = cellIndex % board.getWidth();
            int y = cellIndex / board.getWidth();

            Cell currentCell = board.getCell(x, y);
            currentCell.interact(player, gameStatus);
        } else {
            throw new OutOfBoardException("Position " + cellIndex + " invalide sur le plateau.");
        }
    }


    public void play() throws OutOfBoardException {
        if (player == null) {
            System.out.println("⚠️ Aucun personnage sélectionné.");
            return;
        }
        if (board == null || board.isEmpty()) {
            System.out.println("Aucun plateau chargé, initialisation du plateau par défaut.");
            return;
        }
        player.resetPosition();

        System.out.println("\n🎲 La partie commence ! Position initiale : case " + player.getPosition() + " / " + FINAL_POSITION);

        while (player.getPosition() < FINAL_POSITION && player.getHealth() > 0) {
            System.out.println("\nAppuyez sur [Entrée] pour lancer le dé...");
            
            // Utiliser la méthode utilitaire pour attendre uniquement Entrée
            waitForEnterOnly();

            int roll = dice.roll();
            System.out.println("🎲 Dé lancé : " + roll);

            player.move(roll);

            if (player.getPosition() > FINAL_POSITION) {
                throw new OutOfBoardException("Position hors plateau !");
            }

            System.out.println("📍 Position actuelle : case " + player.getPosition() + " / " + FINAL_POSITION);

            int cellIndex = player.getPosition() - 1;

            if (cellIndex >= 0 && cellIndex < board.getSize()) {
                int x = cellIndex % board.getWidth();
                int y = cellIndex / board.getWidth();
                GameStatus gameStatus = new GameStatus();


                Cell currentCell = board.getCell(x, y);
                currentCell.interact(player, gameStatus);
            } else {
                System.out.println("⚠️ La position actuelle (" + player.getPosition() + ") est invalide sur le plateau (taille : " + board.getSize() + ").");
                return;
            }

            if (player.getHealth() <= 0) {
                System.out.println("💀 Vous êtes mort ! Fin de la partie.");
                return;
            }

            // Proposer sauvegarde après chaque tour
            System.out.print("Voulez-vous sauvegarder la partie ? (o/n) : ");
            String saveInput = scanner.nextLine();
            if (saveInput.equalsIgnoreCase("o")) {
                SaveManager.saveGame(player, board.getCells());
                System.out.println("Partie sauvegardée !");
            }
        }

        if (player.getHealth() > 0 && player.getPosition() >= FINAL_POSITION) {
            System.out.println("\n🏁 Félicitations, vous avez terminé le donjon !");
        }

        // Menu fin de partie
        boolean endMenu = true;
        while (endMenu) {
            System.out.println("\n--- MENU FIN DE PARTIE ---");
            System.out.println("1. Rejouer avec le même personnage");
            System.out.println("2. Retour au menu principal");
            System.out.println("3. Supprimer le personnage");
            System.out.println("4. Charger une partie sauvegardée");
            System.out.println("5. Quitter");

            System.out.print("Choix : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    play();
                    endMenu = false;
                }
                case 2 -> {
                    System.out.println("Retour au menu principal.");
                    endMenu = false;
                }
                case 3 -> {
                    System.out.println("Suppression du personnage...");
                    try {
                        dbManager.deleteHeroById(player.getId());
                    } catch (SQLException e) {
                        System.out.println("❌ Erreur lors de la suppression du personnage : " + e.getMessage());
                    }
                    endMenu = false;
                }
                case 4 -> {
                    GameState saved = SaveManager.loadGame();
                    if (saved != null) {
                        Character savedHero = saved.getPlayer();
                        Board loadedBoard = new Board(true);
                        loadedBoard.setCells(saved.getBoard());
                        savedHero.setPosition(saved.getPosition());

                        Game game = new Game(savedHero, dbManager);
                        game.setBoard(loadedBoard);
                        game.play();
                    } else {
                        System.out.println("❌ Aucune partie sauvegardée trouvée.");
                    }
                    endMenu = false;
                }
                case 5 -> {
                    System.out.println("Merci d'avoir joué !");
                    System.exit(0);
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    /**
     * Méthode utilitaire pour attendre que l'utilisateur appuie uniquement sur Entrée
     */
    private void waitForEnterOnly() {
        String input;
        do {
            input = scanner.nextLine();
            if (!input.isEmpty()) {
                System.out.println("⚠️ Veuillez appuyer uniquement sur [Entrée] pour continuer.");
                System.out.print("Appuyez sur [Entrée]...");
            }
        } while (!input.isEmpty());
    }
}