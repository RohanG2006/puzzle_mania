package com.puzzlemania.model;

public class player {
    private String name;
    private int totalScore;

    public player(String name) {
        this.name = name;
        this.totalScore = 0;
    }

    public String getName() { return name; }
    public int getTotalScore() { return totalScore; }
    public void addScore(int points) { totalScore += points; }
}
