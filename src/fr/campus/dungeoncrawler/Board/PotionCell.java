package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.game.GameStatus;
import fr.campus.dungeoncrawler.item.Potion;

@JsonTypeName("enemy")
public class PotionCell extends Cell {
    private final Potion potion;

    public PotionCell(Potion potion) {
        this.potion = potion;
    }

    @Override
    public void interact(Character character, GameStatus gameStatus) {
        System.out.println("🧪 Vous trouvez une potion qui soigne " + potion.getHealAmount() + " points de vie.");
        int newHealth = Math.min(character.getHealth() + potion.getHealAmount(), character.getMaxHealth());
        character.setHealth(newHealth);
        System.out.println("❤️ Votre santé est maintenant : " + character.getHealth());
    }
}
