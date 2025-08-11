package fr.campus.dungeoncrawler.item;

public class Potion extends Item {
    private final int healAmount;

    public Potion(String name, int healAmount) {
        super("potion");
        this.healAmount = 5;
    }

    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public String toString() {
        return "Potion{" +
                "name='" + getName() + '\'' +
                ", healAmount=" + healAmount +
                '}';
    }
}
