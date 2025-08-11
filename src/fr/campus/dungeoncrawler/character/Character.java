package fr.campus.dungeoncrawler.character;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.campus.dungeoncrawler.dice.Dice;
import fr.campus.dungeoncrawler.dice.Dice20;
import fr.campus.dungeoncrawler.exception.OutOfBoardException;
import fr.campus.dungeoncrawler.item.Item;
import fr.campus.dungeoncrawler.item.Potion;
import fr.campus.dungeoncrawler.item.Shield;
import fr.campus.dungeoncrawler.item.Spell;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite qui représente le personnage
 * Attributs et comportements de base communs à tous les personnages
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public abstract class Character {

    protected int id;
    protected String name;
    protected int health;
    protected int attack;
    protected int strength;
    protected Item offensiveItem;
    protected int position = 1;
    protected int maxHealth;

    // Effet temporaire : Coup de tonnerre
    private boolean thunderStrikeActive = false;

    // Bonus temporaire d'attaque (ex : Arc, Invisibilité)
    private int attackBonus = 0;

    // Inventaire simplifié de potions
    private List<Potion> potions = new ArrayList<>();

    /**
     * Constructeur d'un personnage
     */
    public Character(int id, String name, int health, int attack, int strength, Item offensiveItem) {
        this.id = id;
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.strength = strength;
        this.offensiveItem = offensiveItem;
        this.maxHealth = health;
    }

    // === Logique de combat ===
    public int getAttackPower() {
        Dice d20 = new Dice20();
        int roll = d20.roll();
        // System.out.println("🎲 Jet de D20 du joueur : " + roll);

        if (roll == 1) {
            // System.out.println("❌ Échec critique du joueur ! Attaque échouée.");
            return 0;
        } else if (roll == 20) {
            System.out.println("✅ Réussite critique du joueur ! +2 de force !");
            return this.strength + 2;
        }
        return this.strength;
    }

    public void increaseAttack(int bonus) {
        this.attackBonus += bonus;
    }

    /**
     * Active ou désactive le Coup de tonnerre
     */
    public void setThunderStrikeActive(boolean active) {
        this.thunderStrikeActive = active;
    }

    /**
     * Retourne l'attaque effective, tenant compte du Coup de tonnerre et des bonus
     */
    public int getEffectiveAttack() {
        int baseAttack = this.getAttackPower() + attackBonus;
        if (thunderStrikeActive) {
            return baseAttack * 2;
        }
        return baseAttack;
    }

    /**
     * Réinitialise les effets temporaires (à appeler après chaque combat)
     */
    public void afterCombat() {
        thunderStrikeActive = false;
        attackBonus = 0;
    }

    // === Gestion potions ===

    /**
     * Ajoute une potion à l'inventaire
     */
    public void addPotion(Potion potion) {
        potions.add(potion);
        System.out.println("🍷 Potion ajoutée : " + potion.getName());
    }

    /**
     * Vérifie si le personnage possède une potion par nom (insensible à la casse)
     */
    public boolean hasPotion(String potionName) {
        for (Potion p : potions) {
            if (p.getName().equalsIgnoreCase(potionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Supprime une potion de l'inventaire par nom (consommée)
     */
    public void removePotion(String potionName) {
        potions.removeIf(p -> p.getName().equalsIgnoreCase(potionName));
    }

    /**
     * Utilise une potion : applique l'effet selon le nom, puis la retire de l'inventaire
     */
    public void usePotion(String potionName) {
        if (!hasPotion(potionName)) {
            System.out.println("❌ Vous ne possédez pas cette potion.");
            return;
        }

        if (potionName.equalsIgnoreCase("Coup de tonnerre")) {
            setThunderStrikeActive(true);
            System.out.println("⚡ Coup de tonnerre activé !");
        } else {
            // Par défaut, on cherche la potion et applique son effet de soin
            for (Potion p : potions) {
                if (p.getName().equalsIgnoreCase(potionName)) {
                    this.health += p.getHealAmount();
                    if (this.health > maxHealth) {
                        this.health = maxHealth;
                    }
                    System.out.println("🩺 Vous avez utilisé une potion de soin, vie à " + this.health);
                    break;
                }
            }
        }

        removePotion(potionName);
    }

    public abstract void attackEnemy();

    // === Mouvement ===
    public void move(int steps) throws OutOfBoardException {
        position += steps;
        if (position > 64) {
            int overstep = position - 64;
            position = 64 - overstep;
            System.out.println("💥 Vous avez dépassé la case finale ! Rebond à la case " + position);
        } else {
            System.out.println("📍 Vous avancez à la case " + position);
        }
    }

    public void moveBackward(int steps) throws OutOfBoardException {
        position -= steps;
        if (position < 1) {
            position = 1;
            System.out.println("⛔ Vous êtes déjà au début du plateau, vous ne pouvez pas reculer plus.");
        } else {
            System.out.println("📍 Vous reculez à la case " + position);
        }
    }

    public void resetPosition() {
        position = 1;
    }

    // === Gestion de la vie ===
    public void changeLifePoints(int amount) {
        this.health += amount;
        if (this.health > maxHealth) {
            this.health = maxHealth;
        } else if (this.health < 0) {
            this.health = 0;
        }
        System.out.println("💖 Vie mise à jour : " + this.health);
    }

    public void receiveDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public void setHealth(int health) {
        this.health = Math.min(Math.max(health, 0), maxHealth);
    }

    // === Objets et équipements ===
    public void receivePotion(Potion potion) {
        // On ajoute la potion à l'inventaire (ne soigne pas directement ici)
        System.out.println("🍷 Potion reçue : " + potion.getName());
        addPotion(potion);
    }

    public void addSpell(Spell spell) {
        System.out.println("🪄 " + spell.getName() + " ajouté à votre grimoire !");
    }

    public void setOffensiveItem(Item item) {
        this.offensiveItem = item;
    }

    public void setDefensiveEquipment(Shield shield) {
        // Implémentation possible dans une sous-classe
    }

    // === Getters / Setters ===
    public int getId() { return id; }
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getAttack() { return attack; }
    public int getStrength() { return strength; }
    public Item getOffensiveItem() { return offensiveItem; }
    public int getPosition() { return position; }
    public int getMaxHealth() { return maxHealth; }

    public void setId(int id) { this.id = id; }
    public void setPosition(int position) { this.position = position; }
}
