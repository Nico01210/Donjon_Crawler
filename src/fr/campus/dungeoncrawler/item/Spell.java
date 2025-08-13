package fr.campus.dungeoncrawler.item;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("spell")
public class Spell extends OffensiveEquipment {

    // Constructeur par défaut requis pour Jackson
    public Spell() {
        super();
    }

    public Spell(String name, int power) {
        super(name, power);
    }

    public int getAttackPower() {
        return getPower();
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return name + " (Spell +" + power + ")";
    }
}