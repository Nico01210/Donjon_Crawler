package fr.campus.dungeoncrawler.item;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("offensive")
public abstract class OffensiveEquipment extends Item {
    protected int power;

    // Constructeur par défaut requis pour Jackson
    public OffensiveEquipment() {
        super();
    }

    public OffensiveEquipment(String name, int power) {
        super(name);
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public abstract String toString();
}