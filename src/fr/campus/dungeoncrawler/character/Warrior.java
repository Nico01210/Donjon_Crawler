package fr.campus.dungeoncrawler.character;

import fr.campus.dungeoncrawler.item.Item;
import fr.campus.dungeoncrawler.item.Weapon;

public class Warrior extends Character {

    public Warrior(int id, String name, int health, int attack, int strength, Item offensiveItem) {
        super(id, name, health, attack, strength, offensiveItem);
    }

    public Warrior(String name) {
        // Guerrier par défaut : vie = 100, attaque = 20, force = 10, arme par défaut : Épée
        this(0, name, 100, 20, 10, new Weapon("Épée", 10));
    }
    public void equipWeapon(Weapon weapon, Character character) {
        Item currentItem = character.getOffensiveItem();
        if (currentItem instanceof Weapon) {
            Weapon currentWeapon = (Weapon) currentItem;
            if (currentWeapon == null || weapon.getAttackPower() > currentWeapon.getAttackPower()) {
                character.setOffensiveItem(weapon);
                System.out.println("✅ Nouvelle arme équipée : " + weapon.getName());
            }
        } else {
            character.setOffensiveItem(weapon);
            System.out.println("✅ Nouvelle arme équipée : " + weapon.getName());
        }
    }
    @Override
    public String toString() {
        return "🛡️ Guerrier : " + name +
                "\n❤️ PV : " + health +
                "\n🗡️ Attaque : " + attack +
                "\n💪 Force : " + strength +
                "\n📍 Position : " + position +
                "\n🔪 Équipement : " + (offensiveItem != null ? offensiveItem.getName() : "Aucun");
    }

    @Override
    public int getAttackPower() {
        return super.getAttackPower(); // Utilise le jet de D20 et la force
    }

    @Override
    public void attackEnemy() {
        System.out.println("⚔️ Le Guerrier attaque avec puissance !");
        // Ici, tu pourras appeler un ennemi, calculer les dégâts, etc.
    }
}
