package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.campus.dungeoncrawler.enemy.Enemy;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.Board.Cell;
import fr.campus.dungeoncrawler.Board.BoardEnemy;
import fr.campus.dungeoncrawler.Board.EmptyCell;
import fr.campus.dungeoncrawler.game.GameStatus;

@JsonTypeName("enemy")
public class EnemyCell extends Cell {

    private BoardEnemy enemy;

    public EnemyCell(Board board, int x, int y, BoardEnemy enemy) {
        super(board, x, y);
        this.enemy = enemy;
    }

    public boolean isDefeated() {
        return enemy == null || enemy.isDead();
    }

    public void setEnemy(BoardEnemy enemy) {
        this.enemy = enemy;
    }

    public BoardEnemy getEnemy() {
        return enemy;
    }

    @Override
    public void interact(Character character, GameStatus gameStatus) {
        if (enemy == null || enemy.isDead()) {
            System.out.println("💀 L'ennemi a déjà été vaincu.");
            return;
        }

        System.out.println("⚔️ Un " + enemy.getName() + " apparaît !");
        System.out.println("👹 " + enemy.getName() + " (Vie: " + enemy.getHealth() + ", Attaque: " + enemy.getAttack() + ")");

        // Combat en boucle jusqu’à ce que l’un soit mort
        while (character.getHealth() > 0 && enemy.getHealth() > 0) {

            // Attaque du joueur
            int heroAttackPower = character.getAttackPower();
            System.out.println(character.getName() + " attaque " + enemy.getName() + " avec une force de " + heroAttackPower);
            enemy.takeDamage(heroAttackPower);

            if (enemy.isDead()) {
                System.out.println("✅ Ennemi vaincu !");
                // Supprimer l'ennemi de cette cellule
                enemy = null;
                board.setCell(x, y, new EmptyCell(board, x, y));

                // Vérifier si joueur a atteint la dernière case
                int currentIndex = y * board.getWidth() + x;
                if (currentIndex == board.getFinalPosition() - 1) {
                    gameStatus.endGame("🎉 Félicitations ! Vous avez gagné !");
                }
                return; // fin du combat
            }

            // Riposte de l’ennemi
            int enemyAttackPower = enemy.getAttackPower();
            System.out.println(enemy.getName() + " riposte avec une force de " + enemyAttackPower);
            character.receiveDamage(enemyAttackPower);

            if (character.getHealth() <= 0) {
                System.out.println("💀 " + character.getName() + " a été vaincu. Partie terminée.");
                gameStatus.endGame("☠️ Vous avez perdu !");
                return; // fin du combat
            }

            // Affichage des PV restants
            System.out.println(character.getName() + " a " + character.getHealth() + " points de vie restants.");
            System.out.println(enemy.getName() + " a " + enemy.getHealth() + " points de vie restants.");
        }
    }
}
