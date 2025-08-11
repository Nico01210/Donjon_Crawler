package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.game.GameStatus;
import fr.campus.dungeoncrawler.item.Shield;

@JsonTypeName("enemy")
public class ShieldCell extends Cell {
    private final Shield shield;

    public ShieldCell(Shield shield) {
        this.shield = shield;
    }

    @Override
    public void interact(Character player, GameStatus gameStatus) {
        System.out.println("🛡️ Vous trouvez un bouclier !");
        player.setDefensiveEquipment(shield);
    }
}
