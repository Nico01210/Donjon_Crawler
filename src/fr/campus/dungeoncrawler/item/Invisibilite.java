package fr.campus.dungeoncrawler.item;

import fr.campus.dungeoncrawler.enemy.Enemy;
import fr.campus.dungeoncrawler.enemy.MauvaisEsprit;
import fr.campus.dungeoncrawler.character.Character;


public class Invisibilite extends Spell {
    public Invisibilite() {
        super("Invisibilité", 0);
    }

    public void applyEffect(Character user, Enemy target) {
        int bonus;
        if (target instanceof MauvaisEsprit) {
            bonus = 8;
        } else {
            bonus = 5;
        }
        user.increaseAttack(bonus);
    }
}
