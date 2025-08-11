package fr.campus.dungeoncrawler.dice;

import java.util.Random;

public class Dice6 implements Dice {
    private final Random random = new Random();

    @Override
    public int roll() {
        return random.nextInt(6) + 1;
    }
}
