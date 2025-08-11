package fr.campus.dungeoncrawler.combat;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.enemy.Enemy;
import fr.campus.dungeoncrawler.Board.Board;
import fr.campus.dungeoncrawler.exception.OutOfBoardException;
import fr.campus.dungeoncrawler.game.GameStatus;
import fr.campus.dungeoncrawler.dice.Dice20;

import java.util.Random;
import java.util.Scanner;

public class TurnBasedCombat {
    private final Character player;
    private final Enemy enemy;
    private final Board board;
    private final GameStatus gameStatus;
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();
    private final Dice20 dice20 = new Dice20();

    public TurnBasedCombat(Character player, Enemy enemy, Board board, GameStatus gameStatus) {
        this.player = player;
        this.enemy = enemy;
        this.board = board;
        this.gameStatus = gameStatus;
    }

    public void start() {
        System.out.println("⚔️ Combat contre " + enemy.getName() + " démarré !");

        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            System.out.println("\nVotre choix : 1 = Attaquer, 2 = Fuir");
            int choix = -1;
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide, veuillez saisir 1 ou 2.");
                continue;
            }

            if (choix == 1) {
                // Attaque du joueur avec jet de dé
                int playerRoll = dice20.roll();
                if (playerRoll == 1) {
                    System.out.println("🎲 Vous avez fait un échec critique ! Votre attaque rate.");
                } else {
                    int playerAttackPower = player.getStrength();
                    if (playerRoll == 20) {
                        playerAttackPower += 2;
                        System.out.println("🎲 Réussite critique ! Votre attaque est renforcée (+2).");
                    } else {
                        System.out.println("🎲 Vous avez lancé un " + playerRoll + ".");
                    }
                    System.out.println("Vous attaquez et infligez " + playerAttackPower + " dégâts.");
                    enemy.takeDamage(playerAttackPower);
                }

                if (enemy.getHealth() <= 0) {
                    System.out.println("💀 Ennemi vaincu !");
                    gameStatus.endGame("🎉 Vous avez gagné le combat !");
                    return;
                }

            } else if (choix == 2) {
                int recul = random.nextInt(6) + 1;
                System.out.println("Vous choisissez de fuir et reculez de " + recul + " cases.");
                try {
                    player.moveBackward(recul);
                } catch (OutOfBoardException e) {
                    System.out.println("⚠️ Impossible de reculer davantage : " + e.getMessage());
                }
                gameStatus.endGame("🏃 Vous avez fui le combat.");
                return;
            } else {
                System.out.println("Choix non reconnu. Veuillez saisir 1 ou 2.");
                continue;
            }

            // Attaque de l'ennemi avec jet de dé
            int enemyRoll = dice20.roll();
            int enemyAttackPower = 0;
            if (enemyRoll == 1) {
                System.out.println("🎲 " + enemy.getName() + " a fait un échec critique ! Il rate son attaque.");
            } else {
                enemyAttackPower = enemy.getStrength();
                if (enemyRoll == 20) {
                    enemyAttackPower += 2;
                    System.out.println("🎲 " + enemy.getName() + " réussit un coup critique (+2 dégâts) !");
                } else {
                    System.out.println("🎲 " + enemy.getName() + " a lancé un " + enemyRoll + ".");
                }
                System.out.println(enemy.getName() + " vous inflige " + enemyAttackPower + " dégâts.");
                player.receiveDamage(enemyAttackPower);
            }

            if (player.getHealth() <= 0) {
                System.out.println("☠️ Vous êtes mort, fin de la partie.");
                gameStatus.endGame("☠️ Vous avez perdu !");
                return;
            }

            // Affichage des points de vie restants
            System.out.println("🔋 Votre vie : " + player.getHealth());
            System.out.println("👹 Vie de l'ennemi : " + enemy.getHealth());
        }
    }
}
