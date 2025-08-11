package fr.campus.dungeoncrawler.item;

public class Eclair extends Spell {
    public Eclair() {
        super("Éclair", 12);
    }

    @Override
    public String toString() {
        return "⚡ Éclair (Attaque: 12)";
    }
}
