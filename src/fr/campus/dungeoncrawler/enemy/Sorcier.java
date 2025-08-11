package fr.campus.dungeoncrawler.enemy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.campus.dungeoncrawler.Board.BoardEnemy;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "enemyType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Sorcier.class, name = "sorcier")
})

public class Sorcier extends BoardEnemy {
    public Sorcier() {
        super("Sorcier", 65,18);
    }

    @Override
    public String toString() {
        return "🔮 Sorcier (Vie: " + getHealth() + ", Attaque: " + getAttack() + ")";
    }
}
