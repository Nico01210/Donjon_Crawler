package fr.campus.dungeoncrawler.enemy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.campus.dungeoncrawler.Board.BoardEnemy;
import fr.campus.dungeoncrawler.character.Character;
import fr.campus.dungeoncrawler.item.Item;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "enemyType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Dragon.class, name = "dragon"),
        @JsonSubTypes.Type(value = Gobelin.class, name = "gobelin"),
        @JsonSubTypes.Type(value = Sorcier.class, name = "sorcier"),
        @JsonSubTypes.Type(value = Orc.class, name = "orc"),
        @JsonSubTypes.Type(value = MauvaisEsprit.class, name = "mauvaisEsprit"),
        @JsonSubTypes.Type(value = BoardEnemy.class, name = "boardEnemy")
})
public class Enemy extends Character {

    public Enemy(int id, String name, int health, int attack, int strength, Item offensiveItem) {
        super(id, name, health, attack, strength, offensiveItem);
    }

    // Inflige des dégâts à cet ennemi
    public void receiveDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    // Inflige des dégâts au joueur ciblé
    public void attack(Character target) {
        System.out.println(name + " attaque " + target.getName() + " avec " + attack + " de force !");
        target.receiveDamage(attack);
    }

    public void interact(Character character) {
        // comportement par défaut si on ne surcharge pas
    }

    // Renvoie la puissance d'attaque
    public int getAttackPower() {
        return attack;
    }

    // Réduit les PV de l'ennemi
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    // Vérifie si l'ennemi est mort
    public boolean isDead() {
        return health <= 0;
    }

    @Override
    public void attackEnemy() {
        // Ancienne méthode, garde si utilisée ailleurs
        System.out.println(name + " attaque !");
    }
}
