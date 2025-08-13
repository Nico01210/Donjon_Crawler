package fr.campus.dungeoncrawler.Board;

import fr.campus.dungeoncrawler.enemy.*;
import fr.campus.dungeoncrawler.item.*;
import fr.campus.dungeoncrawler.Board.*;

import java.util.*;

public class Board {

    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private final int FINAL_POSITION = WIDTH * HEIGHT;

    private int id;
    private String name;
    private List<Cell> cells;
    private Cell[][] grid;

    public Board(boolean empty) {
        this.cells = new ArrayList<>(Collections.nCopies(FINAL_POSITION, null));
        grid = new Cell[WIDTH][HEIGHT];

        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < FINAL_POSITION; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);

        int index = 0;

        // Exemple d’utilisation
        int pos = positions.get(index++);
        int x = pos % WIDTH;
        int y = pos / WIDTH;

            // Ajout des 24 ennemis - ✅ CORRECTION: Créer de nouvelles instances à chaque fois

            // 4 Dragons - ✅ Chaque Dragon est une NOUVELLE instance
            for (int i = 0; i < 4; i++) {
                pos = positions.get(index++);
                x = pos % WIDTH;
                y = pos / WIDTH;
                BoardEnemy dragon = new BoardEnemy("Dragon", 15, 4);
                boardSetCell(x, y, new EnemyCell(this, x, y, dragon)); // ✅ Nouveau Dragon
            }

            // 10 Sorciers - ✅ Chaque Sorcier est une NOUVELLE instance
            for (int i = 0; i < 10; i++) {
                 pos = positions.get(index++);
                x = pos % WIDTH;
                y = pos / WIDTH;
                BoardEnemy sorcier = new BoardEnemy("Sorcier", 9, 2);
                boardSetCell(x, y, new EnemyCell(this, x, y, sorcier)); // ✅ Nouveau Sorcier
            }

            // 10 Gobelins - ✅ Chaque Gobelin est une NOUVELLE instance
            for (int i = 0; i < 10; i++) {
                pos = positions.get(index++);
                x = pos % WIDTH;
                y = pos / WIDTH;
                BoardEnemy gobelin = new BoardEnemy("Gobelin", 6, 1);
                boardSetCell(x, y, new EnemyCell(this, x, y, gobelin)); // ✅ Nouveau Gobelin
            }
        // 4 Orcs - ✅ Chaque Orc est une NOUVELLE instance
        for (int i = 0; i < 4; i++) {
            pos = positions.get(index++);
            x = pos % WIDTH;
            y = pos / WIDTH;
            BoardEnemy orc = new BoardEnemy("Orc", 12, 3);
            boardSetCell(x, y, new EnemyCell(this, x, y, orc)); // ✅ Nouveau Orc
        }
        // 5 mauvais esprits - ✅ Chaque mauvais esprit est une NOUVELLE instance
        for (int i = 0; i < 5; i++) {
            pos = positions.get(index++);
            x = pos % WIDTH;
            y = pos / WIDTH;
            BoardEnemy mauvaisEsprit = new BoardEnemy("Mauvais Esprit", 8, 2);
            boardSetCell(x, y, new EnemyCell(this, x, y, mauvaisEsprit)); // ✅ Nouveau Mauvais Esprit
        }

            // Ajout des bonus (24) - ✅ Nouvelles instances aussi

            // 5 Massues
            for (int i = 0; i < 5; i++) {
                pos = positions.get(index++);
                x = pos % WIDTH;
                y = pos / WIDTH;
                boardSetCell(x, y, new WeaponCell(this, x, y, new Weapon("Massue", 3))); // ✅ Nouvelle Massue
            }

            // 4 Épées
            for (int i = 0; i < 4; i++) {
                pos = positions.get(index++);
                x = pos % WIDTH;
                y = pos / WIDTH;
                boardSetCell(x, y, new WeaponCell(this, x, y, new Weapon("Épée", 5))); // ✅ Nouvelle Épée
            }

            // 5 Sorts Éclair
            for (int i = 0; i < 5; i++) {
                pos = positions.get(index++);
                x = pos % WIDTH;
                y = pos / WIDTH;
                boardSetCell(x, y, new SpellCell(new Spell("Éclair", 2))); // ✅ Nouveau Sort
            }

            // 2 Sorts Boule de feu
            for (int i = 0; i < 2; i++) {
                pos = positions.get(index++);
                x = pos % WIDTH;
                y = pos / WIDTH;
                boardSetCell(x, y, new SpellCell(new Spell("Boule de feu", 7))); // ✅ Nouveau Sort
            }

            // 6 Potions standards
            for (int i = 0; i < 6; i++) {
                pos = positions.get(index++);
                x = pos % WIDTH;
                y = pos / WIDTH;
                boardSetCell(x, y, new PotionCell(new Potion("Potion standard", 2))); // ✅ Nouvelle Potion
            }

            // 2 Grandes potions
            for (int i = 0; i < 2; i++) {
                pos = positions.get(index++);
                x = pos % WIDTH;
                y = pos / WIDTH;
                boardSetCell(x, y, new PotionCell(new Potion("Grande potion", 5))); // ✅ Nouvelle Potion
            }

            // Reste des cases → cases vides
            while (index < FINAL_POSITION) {
                pos = positions.get(index++);
                x = pos % WIDTH;
                y = pos / WIDTH;
                boardSetCell(x, y, new EmptyCell(this, x, y)); // ✅ Nouvelle EmptyCell
            }
        }

        // Utilise cette méthode pour placer une cellule dans le plateau et garder la cohérence
        private void boardSetCell ( int x, int y, Cell cell){
            int idx = y * WIDTH + x;
            cells.set(idx, cell);
        }

        public int getFinalPosition () {
            return FINAL_POSITION;
        }

        public int getWidth () {
            return WIDTH;
        }

        public int getHeight () {
            return HEIGHT;
        }

        public int getSize () {
            return WIDTH * HEIGHT;
        }

        public String getName () {
            return name;
        }

        public void setName (String name){
            this.name = name;
        }

        public int getId () {
            return id;
        }

        public void setId ( int id){
            this.id = id;
        }

        public boolean isEmpty () {
            return cells == null || cells.isEmpty();
        }

        public List<Cell> getCells () {
            return cells;
        }

        public void setCells (List < Cell > cells) {
            this.cells = cells;
            grid = new Cell[WIDTH][HEIGHT];
            for (Cell cell : cells) {
                if (cell != null) { // ✅ Vérification de sécurité ajoutée
                    int x = cell.getX();
                    int y = cell.getY();
                    grid[x][y] = cell;
                    cell.setBoard(this);
                }
            }
        }

        public void setCell ( int x, int y, Cell cell){
            int index = y * WIDTH + x;
            cells.set(index, cell);
            grid[x][y] = cell;
            cell.setBoard(this);
        }

        public Cell getCell ( int x, int y){
            int index = y * WIDTH + x;
            return cells.get(index);
        }

        // Affiche le plateau dans la console avec des emojis
        public void displayBoard () {
            System.out.println("┌───────────────────────────────┐");
            for (int y = 0; y < HEIGHT; y++) {
                System.out.print("│ ");
                for (int x = 0; x < WIDTH; x++) {
                    Cell cell = getCell(x, y);

                    if (cell instanceof EnemyCell enemyCell) {
                        if (enemyCell.isDefeated()) {
                            System.out.print("⬜ ");
                        } else {
                            System.out.print("👹 ");
                        }
                    } else if (cell instanceof PotionCell) {
                        System.out.print("🧪 ");
                    } else if (cell instanceof SpellCell) {
                        System.out.print("✨ ");
                    } else if (cell instanceof WeaponCell) {
                        System.out.print("🗡️ ");
                    } else if (cell instanceof EmptyCell) {
                        System.out.print("⬜ ");
                    } else {
                        System.out.print("❓ ");
                    }
                }
                System.out.println("│");
            }
            System.out.println("└───────────────────────────────┘");
        }
    }