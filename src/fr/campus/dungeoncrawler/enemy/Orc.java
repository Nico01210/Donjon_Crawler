package fr.campus.dungeoncrawler.enemy;

import fr.campus.dungeoncrawler.Board.BoardEnemy;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Warrior;
import fr.campus.dungeoncrawler.game.GameStatus;
import fr.campus.dungeoncrawler.item.Item;

public class Orc extends BoardEnemy {

    public Orc() {
        // id = 0 (par défaut), nom = "Orc", 10 PV, 6 ATQ, 0 force (non utilisé ici), pas d'item offensif
        super("Orc", 10, 6);
    }

    @Override
    public void interact(Character character) {
        System.out.println("⚔️ Un Orc apparaît !");

        // L'Orc n'attaque que les Guerriers
        if (character instanceof Warrior) {
            attack(character);
        } else {
            System.out.println("L'Orc ignore " + character.getName() + ".");
        }
    }
}
