package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.game.GameStatus;
import fr.campus.dungeoncrawler.item.Item;
import fr.campus.dungeoncrawler.item.Weapon;

@JsonTypeName("weapon")
public class WeaponCell extends Cell {

    private Weapon weapon;

    // Constructeur par défaut requis pour Jackson
    public WeaponCell() {
        super();
    }

    public WeaponCell(Board board, int x, int y, Weapon weapon) {
        super(board, x, y);
        this.weapon = weapon;
    }

    // Getter et setter pour Jackson
    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public void interact(Character character, GameStatus gameStatus) {
        if (!(character instanceof Warrior)) {
            System.out.println("❌ Vous ne pouvez pas utiliser cette arme.");
            return;
        }

        // Récupérer l'arme équipée actuelle (si c'est une Weapon)
        Item currentItem = character.getOffensiveItem();
        Weapon currentWeapon = null;
        if (currentItem instanceof Weapon) {
            currentWeapon = (Weapon) currentItem;
        }

        // Comparer les puissances d'attaque
        if (currentWeapon == null || weapon.getAttackPower() > currentWeapon.getAttackPower()) {
            character.setOffensiveItem(weapon);
            System.out.println("✅ Nouvelle arme équipée : " + weapon.getName());
        } else {
            System.out.println("❌ Arme moins puissante que celle que vous possédez.");
        }
    }
}
