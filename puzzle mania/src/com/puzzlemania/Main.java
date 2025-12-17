package com.puzzlemania;

import javax.swing.*;
import java.awt.*;
import com.puzzlemania.ui.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Puzzle Mania");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);
            frame.setLocationRelativeTo(null);

            JPanel cards = new JPanel(new CardLayout());

            // Add all your screens with matching names
            mainmenu menuPanel = new mainmenu(cards);
            wordsearchpanel wordPanel = new wordsearchpanel();
            picturepuzzlepanel puzzlePanel = new picturepuzzlepanel();

            cards.add(menuPanel, "menu");
            cards.add(wordPanel, "wordsearch");
            cards.add(puzzlePanel, "picturepuzzle");

            frame.add(cards);
            frame.setVisible(true);
        });
    }
}
/*cd "/Users/siddhisuryavanshi/java project/puzzle mania"
rm -rf out
javac -d out $(find "src" -name "*.java")
cp -r src/com/puzzlemania/assets out/com/puzzlemania/
java -cp out com.puzzlemania.Main
 */