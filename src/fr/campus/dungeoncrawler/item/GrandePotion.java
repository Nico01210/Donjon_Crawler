package fr.campus.dungeoncrawler.item;

public class GrandePotion extends Potion {
    public GrandePotion() {
        super("Grande Potion", 10);
    }

    @Override
    public String toString() {
        return "🍷 Grande Potion (+10 vie)";
    }
}
