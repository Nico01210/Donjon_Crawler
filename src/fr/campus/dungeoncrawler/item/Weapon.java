package fr.campus.dungeoncrawler.item;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("weapon")
public class Weapon extends OffensiveEquipment {

    // Constructeur par défaut requis pour Jackson
    public Weapon() {
        super();
    }

    public Weapon(String name, int power) {
        super(name, power);
    }

    public int getAttackPower() {
        return power; // hérité de OffensiveEquipment
    }

    public String getName() {
        return name; // hérité de OffensiveEquipment
    }

    @Override
    public String toString() {
        return name + " (Weapon +" + power + ")";
    }
}
