package fr.campus.dungeoncrawler.character;

import fr.campus.dungeoncrawler.item.Item;
import fr.campus.dungeoncrawler.item.Spell;

public class Wizard extends Character {

    private Spell spell;

    public Wizard(int id, String name, int health, int attack, int strength, Item offensiveItem) {
        super(id, name, health, attack, strength, offensiveItem);
    }

    public Wizard(String name) {
        // Magicien par défaut : vie=80, attaque=25, force=5, sort par défaut
        this(0, name, 80, 25, 5, new Spell("Boule de feu", 20));
    }

    public Spell getSpell() {
        return this.spell;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
        this.offensiveItem = spell;  // Met à jour aussi l'équipement offensif
    }

    @Override
    public int getAttackPower() {
        // Utilise le getAttackPower de Character (avec jet de D20)
        return super.getAttackPower();
    }

    @Override
    public void attackEnemy() {
        System.out.println("🪄 Le Magicien lance un sort puissant !");
        // Ici tu peux ajouter ta logique d’attaque magique
    }

    @Override
    public String toString() {
        return "🧙‍♂️ Magicien : " + name +
                "\n❤️ PV : " + health +
                "\n🗡️ Attaque : " + attack +
                "\n💪 Force : " + strength +
                "\n📍 Position : " + position +
                "\n✨ Sort équipé : " + (offensiveItem != null ? offensiveItem.getName() : "Aucun");
    }
}
