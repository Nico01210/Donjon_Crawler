package fr.campus.dungeoncrawler.game;

public class GameStatus {
    private boolean gameOver = false;
    private String gameResult = "";

    public boolean isGameOver() {
        return gameOver;
    }

    public String getGameResult() {
        return gameResult;
    }

    public void endGame(String result) {
        this.gameOver = true;
        this.gameResult = result;
    }
}
