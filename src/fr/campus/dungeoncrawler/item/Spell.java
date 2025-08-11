package fr.campus.dungeoncrawler.item;

public class Spell extends OffensiveEquipment {

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