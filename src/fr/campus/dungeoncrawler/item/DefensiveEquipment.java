package fr.campus.dungeoncrawler.item;

public abstract class DefensiveEquipment extends Item {
    protected int defenseValue;

    public DefensiveEquipment(String name, int defenseValue) {
        super(name);
        this.defenseValue = defenseValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    @Override
    public abstract String toString();
}