package fr.campus.dungeoncrawler.item;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "itemType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Weapon.class, name = "weapon"),
        @JsonSubTypes.Type(value = Spell.class, name = "spell"),
        @JsonSubTypes.Type(value = Potion.class, name = "potion"),
        @JsonSubTypes.Type(value = Shield.class, name = "shield"),
        @JsonSubTypes.Type(value = OffensiveEquipment.class, name = "offensive"),
        @JsonSubTypes.Type(value = DefensiveEquipment.class, name = "defensive")
})
public abstract class Item {
    protected String name;
    private int id;
    private String type;
    private int attackPower;

    // Constructeur par défaut requis pour Jackson
    public Item() {}

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Chaque sous-classe devra définir son propre toString()
    @Override
    public abstract String toString();

    public Object getId() {
    return id;
    }
}

