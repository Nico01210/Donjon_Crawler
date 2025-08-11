package fr.campus.dungeoncrawler.item;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.enemy.Dragon;
import fr.campus.dungeoncrawler.enemy.Enemy;
import fr.campus.dungeoncrawler.item.Weapon;

public class Arc extends Weapon {
    public Arc() {
        super("Arc", 0);
    }

    public void applyEffect(Character user, Enemy target) {
        int bonus;
        if (target instanceof Dragon) {
            bonus = 6;
        } else {
            bonus = 4;
        }
        user.increaseAttack(bonus);
    }
}
