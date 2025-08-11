package fr.campus.dungeoncrawler.utils;


import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();

    public static int rollDice(int sides) {
        return random.nextInt(sides) + 1;
    }
}