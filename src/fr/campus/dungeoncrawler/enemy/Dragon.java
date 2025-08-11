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
        @JsonSubTypes.Type(value = Dragon.class, name = "dragon"),
})

public class Dragon extends BoardEnemy {
    public Dragon() {
        super("🐉 Dragon", 120, 15);    }

    @Override
    public String toString() {
        return "🐉 Dragon (Vie: " + getHealth() + ", Attaque: " + getAttack() + ")";
    }
}
