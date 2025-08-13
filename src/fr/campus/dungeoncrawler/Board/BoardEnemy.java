package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.combat.TurnBasedCombat;
import fr.campus.dungeoncrawler.dice.Dice;
import fr.campus.dungeoncrawler.dice.Dice20;
import fr.campus.dungeoncrawler.enemy.Enemy;
import fr.campus.dungeoncrawler.game.GameStatus;

@JsonTypeName("boardEnemy")
public class BoardEnemy extends Enemy {

    // Constructeur par défaut requis pour Jackson
    public BoardEnemy() {
        super(0, "", 0, 0, 0, null);
    }

    public BoardEnemy(String name, int health, int attack) {
        super(0, name, health, attack, 0, null);
    }

    @Override
    public int getAttackPower() {
        Dice d20 = new Dice20();
        int roll = d20.roll();

        if (roll == 1) {
            return 0;
        } else if (roll == 20) {
            return this.getAttack() + 2;
        }

        return this.getAttack();
    }

    public void fight(Character player) {
        System.out.println("👹 Combat engagé avec " + getName() + " !");

        // Avant le combat, vérifier si le joueur veut utiliser "Coup de tonnerre"
        if (player.hasPotion("Coup de tonnerre")) {
            player.usePotion("Coup de tonnerre");  // active le bonus et enlève la potion
        }
        while (this.getHealth() > 0 && player.getHealth() > 0) {
            // L'ennemi attaque
            int enemyAttack = this.getAttackPower();
            player.receiveDamage(enemyAttack);
            System.out.println(getName() + " attaque avec " + enemyAttack + " points de dégâts !");
            if (player.getHealth() <= 0) {
                System.out.println(player.getName() + " est mort !");
                break;
            }

            // Le joueur attaque (en tenant compte des bonus et potions)
            int playerAttack = player.getEffectiveAttack(); // ← nouvelle méthode dans Character
            this.receiveDamage(playerAttack);
            System.out.println(player.getName() + " riposte avec " + playerAttack + " points de dégâts !");

            // Désactivation des effets temporaires comme Coup de tonnerre
            player.afterCombat();

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

        // Exemple : si le joueur a choisi d'utiliser Coup de tonnerre
        if (character.hasPotion("Coup de tonnerre")) { // méthode fictive à implémenter
            character.setThunderStrikeActive(true);
            System.out.println("⚡ Coup de tonnerre activé pour le prochain combat !");
            character.removePotion("Coup de tonnerre"); // Consomme la potion
        }

        TurnBasedCombat combat = new TurnBasedCombat(character, enemy, board, gameStatus);
        combat.start();
    }
}
