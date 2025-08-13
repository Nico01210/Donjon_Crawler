package fr.campus.dungeoncrawler.dice;

import java.util.Random;

public class Dice6 implements Dice {
    private final Random random = new Random();

    @Override
    public int roll() {
        int result = random.nextInt(6) + 1;
        System.out.println("🎲 Lancer de dé à 6 faces : " + result);
        return result;
    }
}
