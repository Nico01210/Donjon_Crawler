package fr.campus.dungeoncrawler.item;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("defensive")
public abstract class DefensiveEquipment extends Item {
    protected int defenseValue;

    // Constructeur par défaut requis pour Jackson
    public DefensiveEquipment() {
        super();
    }

    public DefensiveEquipment(String name, int defenseValue) {
        super(name);
        this.defenseValue = defenseValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }

    @Override
    public abstract String toString();
}