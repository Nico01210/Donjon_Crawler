package fr.campus.dungeoncrawler.item;

public class Shield extends DefensiveEquipment {

    public Shield(String name, int defenseValue) {
        super(name, defenseValue);
    }

    @Override
    public String toString() {
        return name + " (Shield +" + defenseValue + ")";
    }
}