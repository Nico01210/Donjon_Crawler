package fr.campus.dungeoncrawler.item;

public class Weapon extends OffensiveEquipment {

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
