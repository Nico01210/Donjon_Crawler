package fr.campus.dungeoncrawler.Board;

import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.game.GameStatus;
import fr.campus.dungeoncrawler.item.Spell;

@JsonTypeName("spell")
public class SpellCell extends Cell {

    private Spell spell;
    private boolean used = false;

    // Constructeur par défaut requis pour Jackson
    public SpellCell() {
        super();
    }

    public SpellCell(Spell spell) {
        super();
        this.spell = spell;
    }

    // Getters et setters pour Jackson
    public Spell getSpell() {
        return spell;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public void interact(Character character, GameStatus gameStatus) {
        if (used) {
            System.out.println("📜 Ce grimoire a déjà été utilisé.");
            return;
        }
        
        if (character instanceof Wizard wizard) {
            Spell currentSpell = wizard.getSpell();
            
            System.out.println("✨ Vous trouvez un grimoire contenant le sort : " + spell.getName() + 
                              " (Puissance: " + spell.getAttackPower() + ")");
            
            if (currentSpell == null) {
                wizard.setSpell(spell);
                System.out.println("✅ Premier sort appris : " + spell.getName());
                used = true;
            } else if (spell.getAttackPower() > currentSpell.getAttackPower()) {
                System.out.println("🔄 Sort actuel : " + currentSpell.getName() + 
                                  " (Puissance: " + currentSpell.getAttackPower() + ")");
                wizard.setSpell(spell);
                System.out.println("✅ Sort amélioré ! Nouveau sort équipé : " + spell.getName());
                used = true;
            } else {
                System.out.println("❌ Sort moins puissant que celui que vous possédez (" + 
                                  currentSpell.getName() + " - Puissance: " + currentSpell.getAttackPower() + ").");
            }
        } else {
            System.out.println("❌ Seuls les magiciens peuvent déchiffrer ces anciens grimoires magiques.");
        }
    }
}