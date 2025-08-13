package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.game.GameStatus;
import fr.campus.dungeoncrawler.item.Shield;

@JsonTypeName("shield")
public class ShieldCell extends Cell {
    private Shield shield;

    // Constructeur par défaut requis pour Jackson
    public ShieldCell() {
        super();
    }

    public ShieldCell(Shield shield) {
        super();
        this.shield = shield;
    }

    // Getters et setters pour Jackson
    public Shield getShield() {
        return shield;
    }

    public void setShield(Shield shield) {
        this.shield = shield;
    }

    @Override
    public void interact(Character player, GameStatus gameStatus) {
        System.out.println("🛡️ Vous trouvez un bouclier !");
        player.setDefensiveEquipment(shield);
    }
}
