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
        @JsonSubTypes.Type(value = Gobelin.class, name = "gobelin"),
})

public class Gobelin extends BoardEnemy {
    public Gobelin() {
        super("👺 Gobelin", 100, 12);
    }

    @Override
    public String toString() {
        return "👺 Gobelin (Vie: " + getHealth() + ", Attaque: " + getAttack() + ")";
    }
}
