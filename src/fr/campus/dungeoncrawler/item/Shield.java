package fr.campus.dungeoncrawler.item;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("shield")
public class Shield extends DefensiveEquipment {

    // Constructeur par défaut requis pour Jackson
    public Shield() {
        super();
    }

    public Shield(String name, int defenseValue) {
        super(name, defenseValue);
    }

    @Override
    public String toString() {
        return name + " (Shield +" + defenseValue + ")";
    }
}