package fr.campus.dungeoncrawler.Board;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.combat.TurnBasedCombat;
import fr.campus.dungeoncrawler.dice.Dice;
import fr.campus.dungeoncrawler.dice.Dice20;
import fr.campus.dungeoncrawler.enemy.Enemy;
import fr.campus.dungeoncrawler.game.GameStatus;

public class BoardEnemy extends Enemy {

    public BoardEnemy(String name, int health, int attack) {
        super(0, name, health, attack, 0, null);
    }

    @Override
    public int getAttackPower() {
        Dice d20 = new Dice20();
        int roll = d20.roll();
        // Pour ne pas afficher le jet de dé, commente la ligne suivante
        // System.out.println("🎲 Jet de D20 de l’ennemi : " + roll);

        if (roll == 1) {
            // System.out.println("❌ Échec critique de l’ennemi !");
            return 0;
        } else if (roll == 20) {
            // System.out.println("🔥 Réussite critique de l’ennemi ! +2 de force !");
            return this.getAttack() + 2;
        }

        return this.getAttack();
    }

    public void fight(Character player) {
        System.out.println("👹 Combat engagé avec " + getName() + " !");
        while (this.getHealth() > 0 && player.getHealth() > 0) {
            // L'ennemi attaque
            int enemyAttack = this.getAttackPower();
            player.receiveDamage(enemyAttack);
            System.out.println(getName() + " attaque avec " + enemyAttack + " points de dégâts !");
            if (player.getHealth() <= 0) {
                System.out.println(player.getName() + " est mort !");
                break;
            }

            // Le joueur attaque
            int playerAttack = player.getAttackPower();
            this.receiveDamage(playerAttack);
            System.out.println(player.getName() + " riposte avec " + playerAttack + " points de dégâts !");
            if (this.getHealth() <= 0) {
                System.out.println(getName() + " est mort !");
                break;
            }

            System.out.println(player.getName() + " PV restants : " + player.getHealth());
            System.out.println(getName() + " PV restants : " + this.getHealth());
        }
    }


    public boolean isDefeated() {
        return this.getHealth() <= 0;
    }

    public void interact(Character character, GameStatus gameStatus, Enemy enemy, Board board) {
        TurnBasedCombat combat = new TurnBasedCombat(character, enemy, board, gameStatus);
        combat.start();
    }
}
