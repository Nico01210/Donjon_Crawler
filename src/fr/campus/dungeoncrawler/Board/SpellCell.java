package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.game.GameStatus;
import fr.campus.dungeoncrawler.item.Spell;

@JsonTypeName("enemy")
public class SpellCell extends Cell {

    private Spell spell;
    private boolean used = false;

    public SpellCell(Spell spell) {
        this.spell = spell;
    }

    @Override
    public void interact(Character character, GameStatus gameStatus) {
        if (character instanceof Wizard) {
            Spell currentSpell = ((Wizard) character).getSpell();
            if (currentSpell == null || spell.getAttackPower() > currentSpell.getAttackPower()) {
                ((Wizard) character).setSpell(spell);
                System.out.println("✅ Nouveau sort équipé : " + spell.getName());
            } else {
                System.out.println("❌ Sort moins puissant que celui que vous possédez.");
            }
        } else {
            System.out.println("❌ Seuls les magiciens peuvent utiliser ce sort.");
        }
    }
}