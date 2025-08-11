package fr.campus.dungeoncrawler.item;

public class BouleDeFeu extends Spell {
    public BouleDeFeu() {
        super("Boule de Feu", 15);
    }

    @Override
    public String toString() {
        return "🔥 Boule de Feu (Attaque: 15)";
    }
}
