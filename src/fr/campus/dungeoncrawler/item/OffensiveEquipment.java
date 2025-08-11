package fr.campus.dungeoncrawler.item;

public abstract class OffensiveEquipment extends Item {
    protected int power;

    public OffensiveEquipment(String name, int power) {
        super(name);
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    @Override
    public abstract String toString();
}