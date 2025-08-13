package fr.campus.dungeoncrawler.dice;

import java.util.Random;

public class Dice20 implements fr.campus.dungeoncrawler.dice.Dice {
    private final Random random = new Random();

    @Override
    public int roll() {
        int result = random.nextInt(20) + 1;
        return result;
    }
}