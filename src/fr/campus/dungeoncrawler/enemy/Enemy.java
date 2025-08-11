package fr.campus.dungeoncrawler.enemy;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
        @JsonSubTypes.Type(value = Sorcier.class, name = "sorcier")
})

public class Enemy extends Character {


    public Enemy(int id, String name, int health, int attack, int strength, Item offensiveItem) {
        super(id, name, health, attack, strength, offensiveItem);
    }


    public void receiveDamage(int damage) {
        health -= damage;
    }
    public void interact(Character character) {
        // comportement par défaut
    }
    public int getAttackPower() {
        return strength;
    }
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public boolean isDead() {
        return health <= 0;
    }
    @Override
    public void attackEnemy() {
        // Exemple d'implémentation, à adapter selon ta logique de jeu
        System.out.println(name + " attaque !");
        // ou tu peux ajouter la logique d'attaque ici
    }
}