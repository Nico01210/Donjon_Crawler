package fr.campus.dungeoncrawler.ui;

import db.DatabaseManager;
import fr.campus.dungeoncrawler.Board.*;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.enemy.Dragon;
import fr.campus.dungeoncrawler.enemy.Gobelin;
import fr.campus.dungeoncrawler.enemy.Sorcier;
import fr.campus.dungeoncrawler.enemy.MauvaisEsprit;
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
    
    // 🎯 Valeurs maximales pour la création de héros
    private static final int MAX_HEALTH = 150;
    private static final int MAX_ATTACK = 50;
    private static final int MAX_STRENGTH = 30;
    
    // 🎯 Valeurs minimales pour la création de héros
    private static final int MIN_HEALTH = 50;
    private static final int MIN_ATTACK = 10;
    private static final int MIN_STRENGTH = 5;
    
    // 🎯 Système de points à répartir
    private static final int TOTAL_POINTS = 200;
    
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
                System.out.println("4. Modifier un héros existant");
                System.out.println("5. Quitter");
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
                            for (Character hero : heroes) {
                                String className = hero instanceof Wizard ? "🧙‍♂️ Magicien" : "⚔️ Guerrier";
                                System.out.printf("- %s (%s - Vie: %d, Attaque: %d, Force: %d)%n", 
                                    hero.getName(), className, hero.getHealth(), hero.getAttack(), hero.getStrength());
                            }
                            System.out.println("💡 Rappel : Force = dégâts supplémentaires en combat");
                            System.out.println("");
                            
                            System.out.print("Entrez le nom du héros pour lancer la partie (ou 'retour' pour revenir) : ");
                            String heroName = scanner.nextLine().trim();

                            if (!heroName.equalsIgnoreCase("retour")) {
                                Character selectedHero = null;
                                
                                // Recherche du héros par nom (insensible à la casse)
                                for (Character hero : heroes) {
                                    if (hero.getName().equalsIgnoreCase(heroName)) {
                                        selectedHero = hero;
                                        break;
                                    }
                                }
                                
                                if (selectedHero != null) {
                                    System.out.println("✅ Héros sélectionné : " + selectedHero.getName());
                                    
                                    // ✅ recrée un plateau neuf à chaque lancement de partie
                                    Board freshBoard = createCustomBoard();

                                    startGame(selectedHero, dbManager);
                                } else {
                                    System.out.println("❌ Aucun héros trouvé avec le nom : " + heroName);
                                    System.out.println("💡 Vérifiez l'orthographe ou choisissez un nom dans la liste.");
                                }
                            }
                        }
                    }

                    case 2 -> {
                        System.out.print("Nom du héros : ");
                        String name = scanner.nextLine();

                        // Choix de la classe avec validation
                        String classType = null;
                        while (classType == null) {
                            System.out.print("Classe (warrior/wizard) : ");
                            String input = scanner.nextLine().toLowerCase().trim();
                            
                            if (input.equals("wizard") || input.equals("magicien") || input.equals("mage") || input.equals("w")) {
                                classType = "wizard";
                                System.out.println("✅ Magicien sélectionné !");
                            } else if (input.equals("warrior") || input.equals("guerrier") || input.equals("guerre") || input.equals("g")) {
                                classType = "warrior";
                                System.out.println("✅ Guerrier sélectionné !");
                            } else {
                                System.out.println("❌ Classe non reconnue. Tapez 'wizard' (ou 'w') pour magicien, 'warrior' (ou 'g') pour guerrier.");
                            }
                        }

                        // 🎯 Choix du mode de création
                        System.out.println("\n=== Mode de création ===");
                        System.out.println("1. Saisie libre (avec limites)");
                        System.out.println("2. Répartition de points (" + TOTAL_POINTS + " points à répartir)");
                        System.out.print("Choix : ");
                        int modeCreation = scanner.nextInt();
                        scanner.nextLine(); // consommer retour

                        Character newHero;

                        if (modeCreation == 2) {
                            // 🎯 Mode répartition de points
                            newHero = creerHeroAvecPoints(scanner, name, classType);
                        } else {
                            // 🎯 Mode saisie libre avec validation des limites
                            System.out.println("\n=== Création des statistiques du héros ===");
                            System.out.println("📋 Explication des statistiques :");
                            System.out.println("   • Vie : Points de vie du héros");
                            System.out.println("   • Attaque : Dégâts de base des armes/sorts");
                            System.out.println("   • Force : Dégâts supplémentaires ajoutés en combat");
                            System.out.println("");
                            
                            int health = saisirValeurAvecLimites(scanner, "Points de vie", MIN_HEALTH, MAX_HEALTH);
                            int attack = saisirValeurAvecLimites(scanner, "Attaque", MIN_ATTACK, MAX_ATTACK);
                            int strength = saisirValeurAvecLimites(scanner, "Force (dégâts bonus)", MIN_STRENGTH, MAX_STRENGTH);

                            if (classType.equals("wizard")) {
                                // 🧙‍♂️ Magicien avec sort par défaut
                                Spell defaultSpell = new Spell("Sort basique", 8);
                                newHero = new Wizard(0, name, health, attack, strength, defaultSpell);
                            } else {
                                // ⚔️ Guerrier avec épée par défaut
                                Weapon sword = new Weapon("Épée standard", 10);
                                newHero = new Warrior(0, name, health, attack, strength, sword);
                            }
                        }

                        dbManager.createHero(newHero);
                        System.out.println("✅ Héros créé avec succès !");
                    }

                    case 3 -> {
                        customBoard = createCustomBoard();
                        // ✅ Affichage uniquement lors de la création manuelle du plateau
                        System.out.println("\n🗺️ Plateau personnalisé créé :\n");
                        customBoard.displayBoard();
                        System.out.println("✅ Plateau personnalisé prêt !");
                    }

                    case 4 -> {
                        List<Character> heroes = dbManager.getHeroesList();
                        if (heroes.isEmpty()) {
                            System.out.println("Aucun héros trouvé.");
                        } else {
                            System.out.println("Héros existants :");
                            for (Character hero : heroes) {
                                String className = hero instanceof Wizard ? "🧙‍♂️ Magicien" : "⚔️ Guerrier";
                                System.out.printf("- %s (%s - Vie: %d, Attaque: %d, Force: %d)%n", 
                                    hero.getName(), className, hero.getHealth(), hero.getAttack(), hero.getStrength());
                            }
                            System.out.println("💡 Rappel : Force = dégâts supplémentaires en combat");
                            System.out.println("");
                            
                            System.out.print("Entrez le nom du héros à modifier (ou 'retour' pour revenir) : ");
                            String heroName = scanner.nextLine().trim();

                            if (!heroName.equalsIgnoreCase("retour")) {
                                Character selectedHero = null;
                                
                                // Recherche du héros par nom
                                for (Character hero : heroes) {
                                    if (hero.getName().equalsIgnoreCase(heroName)) {
                                        selectedHero = hero;
                                        break;
                                    }
                                }
                                
                                if (selectedHero != null) {
                                    System.out.println("✅ Héros trouvé : " + selectedHero.getName());
                                    String currentClass = selectedHero instanceof Wizard ? "Magicien" : "Guerrier";
                                    String newClass = selectedHero instanceof Wizard ? "Guerrier" : "Magicien";
                                    
                                    System.out.printf("Classe actuelle : %s\n", currentClass);
                                    System.out.printf("Voulez-vous changer la classe en %s ? (o/n) : ", newClass);
                                    
                                    String confirm = scanner.nextLine().trim();
                                    if (confirm.equalsIgnoreCase("o")) {
                                        // Supprimer l'ancien héros
                                        dbManager.deleteHeroById(selectedHero.getId());
                                        
                                        // Créer le nouveau héros avec la classe opposée
                                        Character newHero;
                                        if (selectedHero instanceof Wizard) {
                                            // Convertir en guerrier
                                            Weapon sword = new Weapon("Épée standard", 10);
                                            newHero = new Warrior(0, selectedHero.getName(), 
                                                selectedHero.getHealth(), selectedHero.getAttack(), 
                                                selectedHero.getStrength(), sword);
                                        } else {
                                            // Convertir en magicien
                                            Spell defaultSpell = new Spell("Sort basique", 8);
                                            newHero = new Wizard(0, selectedHero.getName(), 
                                                selectedHero.getHealth(), selectedHero.getAttack(), 
                                                selectedHero.getStrength(), defaultSpell);
                                        }
                                        
                                        dbManager.createHero(newHero);
                                        System.out.printf("✅ %s est maintenant un %s !\n", 
                                            selectedHero.getName(), newClass);
                                    } else {
                                        System.out.println("❌ Modification annulée.");
                                    }
                                } else {
                                    System.out.println("❌ Aucun héros trouvé avec le nom : " + heroName);
                                }
                            }
                        }
                    }

                    case 5 -> running = false;

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

    /**
     * Méthode pour saisir une valeur entière avec validation des limites
     */
    private static int saisirValeurAvecLimites(Scanner scanner, String nomAttribut, int min, int max) {
        int valeur;
        do {
            System.out.printf("%s (%d-%d) : ", nomAttribut, min, max);
            while (!scanner.hasNextInt()) {
                System.out.println("❌ Veuillez entrer un nombre valide !");
                System.out.printf("%s (%d-%d) : ", nomAttribut, min, max);
                scanner.next();
            }
            valeur = scanner.nextInt();
            
            if (valeur < min || valeur > max) {
                System.out.printf("❌ La valeur doit être entre %d et %d ! Réessayez.\n", min, max);
            }
        } while (valeur < min || valeur > max);
        
        return valeur;
    }

    /**
     * Méthode pour créer un héros avec un système de répartition de points
     */
    private static Character creerHeroAvecPoints(Scanner scanner, String name, String classType) {
        System.out.println("\n=== Système de répartition de points ===");
        System.out.printf("Vous avez %d points à répartir entre vos statistiques.\n", TOTAL_POINTS);
        System.out.printf("Minimums requis : Vie=%d, Attaque=%d, Force=%d\n", MIN_HEALTH, MIN_ATTACK, MIN_STRENGTH);
        System.out.println("\n📋 Rappel des statistiques :");
        System.out.println("   • Vie : Points de vie du héros");
        System.out.println("   • Attaque : Dégâts de base des armes/sorts");
        System.out.println("   • Force : Dégâts supplémentaires ajoutés en combat");
        System.out.println("");
        
        int pointsRestants = TOTAL_POINTS;
        
        // Allocation minimale obligatoire
        int health = MIN_HEALTH;
        int attack = MIN_ATTACK;
        int strength = MIN_STRENGTH;
        pointsRestants -= (MIN_HEALTH + MIN_ATTACK + MIN_STRENGTH);
        
        System.out.printf("\nValeurs de base : Vie=%d, Attaque=%d, Force=%d\n", health, attack, strength);
        System.out.printf("Points restants à répartir : %d\n\n", pointsRestants);
        
        // Répartition des points restants
        while (pointsRestants > 0) {
            System.out.printf("Points restants : %d\n", pointsRestants);
            System.out.printf("Stats actuelles : Vie=%d (max %d), Attaque=%d (max %d), Force=%d (max %d)\n", 
                health, MAX_HEALTH, attack, MAX_ATTACK, strength, MAX_STRENGTH);
            
            System.out.println("Où voulez-vous ajouter des points ?");
            System.out.println("1. Vie (+1 point)");
            System.out.println("2. Attaque (+1 point)");
            System.out.println("3. Force - dégâts bonus (+1 point)");
            System.out.println("4. Terminer la répartition");
            System.out.print("Choix : ");
            
            int choix = scanner.nextInt();
            
            switch (choix) {
                case 1 -> {
                    if (health < MAX_HEALTH) {
                        health++;
                        pointsRestants--;
                        System.out.println("✅ +1 Vie");
                    } else {
                        System.out.println("❌ Vie déjà au maximum !");
                    }
                }
                case 2 -> {
                    if (attack < MAX_ATTACK) {
                        attack++;
                        pointsRestants--;
                        System.out.println("✅ +1 Attaque");
                    } else {
                        System.out.println("❌ Attaque déjà au maximum !");
                    }
                }
                case 3 -> {
                    if (strength < MAX_STRENGTH) {
                        strength++;
                        pointsRestants--;
                        System.out.println("✅ +1 Force");
                    } else {
                        System.out.println("❌ Force déjà au maximum !");
                    }
                }
                case 4 -> {
                    if (pointsRestants > 0) {
                        System.out.printf("⚠️ Il vous reste %d points non utilisés. Continuer quand même ? (o/n) : ", pointsRestants);
                        scanner.nextLine(); // consommer le retour
                        String confirm = scanner.nextLine();
                        if (confirm.equalsIgnoreCase("o")) {
                            pointsRestants = 0;
                        }
                    } else {
                        pointsRestants = 0;
                    }
                }
                default -> System.out.println("❌ Choix invalide !");
            }
            System.out.println();
        }
        
        // Création du héros
        Character newHero;
        
        if (classType.equals("wizard")) {
            // 🧙‍♂️ Magicien avec sort par défaut
            Spell defaultSpell = new Spell("Sort basique", 8);
            newHero = new Wizard(0, name, health, attack, strength, defaultSpell);
        } else {
            // ⚔️ Guerrier avec épée par défaut
            Weapon sword = new Weapon("Épée standard", 10);
            newHero = new Warrior(0, name, health, attack, strength, sword);
        }

        System.out.printf("\n✅ Héros créé : %s (Vie=%d, Attaque=%d, Force=%d)\n", 
            name, health, attack, strength);
        
        return newHero;
    }

    private static void startGame(Character hero, DatabaseManager dbManager) {
        System.out.println("\n=== Lancement de la partie avec le héros : " + hero.getName() + " ===");

        Scanner gameScanner = new Scanner(System.in);
        try {
            Game game = new Game(hero, dbManager, gameScanner);

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
        customBoard.setCell(0, 0, new EnemyCell(customBoard, 0, 0, new BoardEnemy("Gobelin", 6, 1)));
        customBoard.setCell(0, 2, new EnemyCell(customBoard, 0, 2, new BoardEnemy("Sorcier", 9, 2)));
        customBoard.setCell(1, 3, new EnemyCell(customBoard, 1, 3, new BoardEnemy("Dragon", 15, 4)));
        // Ajout de MauvaisEsprit qui n'attaquent que les magiciens
        customBoard.setCell(2, 1, new EnemyCell(customBoard, 2, 1, new MauvaisEsprit()));
        customBoard.setCell(3, 3, new EnemyCell(customBoard, 3, 3, new MauvaisEsprit()));

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

        // ✅ Plateau créé sans affichage automatique
        // L'affichage se fera uniquement quand nécessaire dans le jeu
        
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
