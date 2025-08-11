package fr.campus.dungeoncrawler.item;

import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.enemy.Enemy;

public class CoupDeTonnerre extends Potion {

    public CoupDeTonnerre() {
        super("Coup de tonnerre", 0);
    }

    public void applyEffect(Character user, Enemy target) {
        user.setThunderStrikeActive(true);
        System.out.println("⚡ Coup de tonnerre activé ! Votre prochaine attaque est doublée.");
    }

    @Override
    public int getHealAmount() {
        // Cette potion ne soigne pas, donc 0
        return 0;
    }
}