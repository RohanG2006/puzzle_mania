package com.puzzlemania.utils;

import java.io.*;
import java.util.*;

public class scoremanager {
    private static final String FILE_NAME = "scores.txt";

    public static void saveScore(String playerName, int score) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(playerName + " - " + score + "\n");
        } catch (IOException e) {
            System.out.println("Error saving score: " + e.getMessage());
        }
    }

    public static List<String> getScores() {
        List<String> scores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                scores.add(line);
            }
        } catch (IOException e) {
            System.out.println("No scores yet!");
        }
        return scores;
    }
}
