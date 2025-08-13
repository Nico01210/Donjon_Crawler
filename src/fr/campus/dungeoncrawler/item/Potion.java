package fr.campus.dungeoncrawler.item;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("potion")
public class Potion extends Item {
    private int healAmount;

    // Constructeur par défaut requis pour Jackson
    public Potion() {
        super();
    }

    public Potion(String name, int healAmount) {
        super(name);
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(int healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public String toString() {
        return "Potion{" +
                "name='" + getName() + '\'' +
                ", healAmount=" + healAmount +
                '}';
    }
}
