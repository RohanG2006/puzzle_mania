package com.puzzlemania.games;

public abstract class puzzlegame {
    protected String gameName;
    protected int score;

    public puzzlegame(String name) {
        this.gameName = name;
        this.score = 0;
    }

    public abstract void startGame();

    public String getGameName() {
        return gameName;
    }

    public int getScore() {
        return score;
    }
}
