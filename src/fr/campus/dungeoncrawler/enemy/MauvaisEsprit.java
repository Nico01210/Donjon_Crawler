package fr.campus.dungeoncrawler.enemy;

import fr.campus.dungeoncrawler.Board.BoardEnemy;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.character.Wizard;
import fr.campus.dungeoncrawler.game.GameStatus;
import fr.campus.dungeoncrawler.item.Item;

public class MauvaisEsprit extends BoardEnemy {

    public MauvaisEsprit() {
        // id = 0 (par défaut), nom = "Mauvais Esprit", 15 PV, 4 ATQ, 0 force (non utilisé ici), pas d'item offensif
        super( "Mauvais Esprit", 15, 4);
    }

    @Override
    public void interact(Character character) {
        System.out.println("👻 Un Mauvais Esprit apparaît !");

        // Le Mauvais Esprit n'attaque que les Magiciens
        if (character instanceof Wizard) {
            attack(character);
        } else {
            System.out.println("Le Mauvais Esprit ne prête aucune attention à " + character.getName() + ".");
        }
    }
}
