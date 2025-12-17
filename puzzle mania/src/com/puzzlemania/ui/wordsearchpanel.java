package com.puzzlemania.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class wordsearchpanel extends JPanel {

    private static final int GRID_SIZE = 10;
    private static final String[] WORDS = { "KOHLI", "INDIA", "BAT", "BALL", "BOWL", "RUN" };

    private final JButton[][] gridButtons = new JButton[GRID_SIZE][GRID_SIZE];
    private char[][] grid = new char[GRID_SIZE][GRID_SIZE];
    private final Set<String> foundWords = new HashSet<>();
    private final DefaultListModel<String> wordListModel = new DefaultListModel<>();

    private final java.util.List<Point> selectedPath = new ArrayList<>();
    private Point direction = null;

    private JPanel gridPanel;
    private JPanel bottomPanel;

    public wordsearchpanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 255, 240)); 

        JLabel title = new JLabel("🏏 Word Search : Score runs in any direction");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 3, 3)); // increased gaps
        gridPanel.setBackground(new Color(200, 230, 200)); // pale greenish border between cells
        add(gridPanel, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(200, 0));
        JLabel listLabel = new JLabel("📜 Words to Find:", SwingConstants.CENTER);
        listLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sidePanel.add(listLabel, BorderLayout.NORTH);

        for (String word : WORDS) wordListModel.addElement(word);
        JList<String> wordList = new JList<>(wordListModel);
        wordList.setFont(new Font("Consolas", Font.BOLD, 16));
        sidePanel.add(new JScrollPane(wordList), BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        bottomPanel = new JPanel();
        JButton backButton = new JButton("🏠 Back to Menu");
        styleButton(backButton, new Color(34, 139, 34));
        backButton.addActionListener(e -> {
            Container parent = getParent();
            if (parent.getLayout() instanceof CardLayout layout) {
                layout.show(parent, "menu");
            }
        });
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        generateGrid();
        createButtons();
    }

    /*  GRID GENERATION */
    private void generateGrid() {
        grid = new char[GRID_SIZE][GRID_SIZE];
        for (int r = 0; r < GRID_SIZE; r++)
            Arrays.fill(grid[r], '.');

        Random rand = new Random();
        int[][] dirs = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (String word : WORDS) {
            boolean placed = false;
            for (int tries = 0; tries < 300 && !placed; tries++) {
                int row = rand.nextInt(GRID_SIZE);
                int col = rand.nextInt(GRID_SIZE);
                int[] dir = dirs[rand.nextInt(dirs.length)];
                if (canPlace(word, row, col, dir[0], dir[1])) {
                    placeWord(word, row, col, dir[0], dir[1]);
                    placed = true;
                }
            }
            if (!placed) {
                for (int r = 0; r < GRID_SIZE && !placed; r++) {
                    for (int c = 0; c + word.length() <= GRID_SIZE && !placed; c++) {
                        placeWord(word, r, c, 0, 1);
                        placed = true;
                    }
                }
            }
        }

        for (int r = 0; r < GRID_SIZE; r++)
            for (int c = 0; c < GRID_SIZE; c++)
                if (grid[r][c] == '.')
                    grid[r][c] = (char) ('A' + rand.nextInt(26));
    }

    private boolean canPlace(String word, int row, int col, int dr, int dc) {
        for (int i = 0; i < word.length(); i++) {
            int nr = row + i * dr, nc = col + i * dc;
            if (nr < 0 || nr >= GRID_SIZE || nc < 0 || nc >= GRID_SIZE)
                return false;
            if (grid[nr][nc] != '.' && grid[nr][nc] != word.charAt(i))
                return false;
        }
        return true;
    }

    private void placeWord(String word, int row, int col, int dr, int dc) {
        for (int i = 0; i < word.length(); i++) {
            grid[row + i * dr][col + i * dc] = word.charAt(i);
        }
    }

    /*  BUTTON CREATION */
    private void createButtons() {
        gridPanel.removeAll();

        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                JButton btn = new JButton(String.valueOf(grid[r][c]));
                btn.setFont(new Font("Segoe UI", Font.BOLD, 26)); // larger font
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                btn.setOpaque(true);
                btn.setBackground(new Color(245, 255, 245)); // soft minty white
                btn.setForeground(new Color(0, 102, 0)); // dark green text
                btn.setPreferredSize(new Dimension(65, 65)); // slightly bigger buttons
                btn.setBorder(BorderFactory.createLineBorder(new Color(180, 220, 180), 2));

                int row = r, col = c;
                btn.addActionListener(e -> handleCellClick(row, col));

                gridButtons[r][c] = btn;
                gridPanel.add(btn);
            }
        }

        revalidate();
        repaint();
    }

    /* CLICK LOGIC  */
    private void handleCellClick(int row, int col) {
        Point p = new Point(row, col);
        if (gridButtons[row][col].getText().isEmpty()) return;

        if (selectedPath.isEmpty()) {
            selectedPath.add(p);
            highlight(row, col);
            return;
        }

        if (selectedPath.size() == 1) {
            Point first = selectedPath.get(0);
            int dr = row - first.x;
            int dc = col - first.y;

            if (dr == 0 && dc == 0) return;

            if (dr != 0) dr /= Math.abs(dr);
            if (dc != 0) dc /= Math.abs(dc);

            direction = new Point(dr, dc);
            selectedPath.add(p);
            highlight(row, col);
            return;
        }

        Point last = selectedPath.get(selectedPath.size() - 1);
        if (direction != null &&
                row == last.x + direction.x &&
                col == last.y + direction.y) {

            selectedPath.add(p);
            highlight(row, col);

            String word = buildWord(selectedPath);
            String reverse = new StringBuilder(word).reverse().toString();

            for (String w : WORDS) {
                if (word.equals(w) || reverse.equals(w)) {
                    consumeWord(w);
                    return;
                }
            }

            if (!isPossiblePrefix(word)) resetSelection(false);

        } else {
            resetSelection(false);
        }
    }

    private void highlight(int row, int col) {
        JButton btn = gridButtons[row][col];
        btn.setBackground(new Color(255, 215, 0)); // gold highlight
        btn.setForeground(Color.BLACK);
        btn.repaint();
    }

    private String buildWord(java.util.List<Point> points) {
        StringBuilder sb = new StringBuilder();
        for (Point p : points)
            sb.append(grid[p.x][p.y]);
        return sb.toString();
    }

    private boolean isPossiblePrefix(String current) {
        for (String w : WORDS) {
            if (w.startsWith(current) || new StringBuilder(w).reverse().toString().startsWith(current))
                return true;
        }
        return false;
    }

    private void resetSelection(boolean keepHighlight) {
        if (!keepHighlight) {
            for (Point p : selectedPath) {
                JButton btn = gridButtons[p.x][p.y];
                if (!btn.getText().isEmpty()) {
                    btn.setBackground(new Color(245, 255, 245));
                    btn.setForeground(new Color(0, 102, 0));
                    btn.repaint();
                }
            }
        }
        selectedPath.clear();
        direction = null;
    }

    private void consumeWord(String word) {
        foundWords.add(word);
        int idx = wordListModel.indexOf(word);
        if (idx != -1)
            wordListModel.set(idx, "✔ " + word);

        for (Point p : selectedPath) {
            JButton b = gridButtons[p.x][p.y];
            b.setText("");
            b.setBackground(new Color(220, 240, 220));
            b.repaint();
        }

        selectedPath.clear();
        direction = null;

        if (foundWords.size() == WORDS.length) {
            showCompletionButtons();
        } else {
            JOptionPane.showMessageDialog(this,
                    "💥 Nice Shot! You found \"" + word + "\"!",
                    "Well Played",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    /* GAME END */
    private void showCompletionButtons() {
        JOptionPane.showMessageDialog(this,
                "🏆 You found all words! Kohli would be proud! 🏏",
                "Victory!",
                JOptionPane.INFORMATION_MESSAGE);

        gridPanel.setEnabled(false);
        for (JButton[] row : gridButtons)
            for (JButton b : row)
                b.setEnabled(false);

        bottomPanel.removeAll();

        JButton menuButton = new JButton("🏠 Back to Menu");
        styleButton(menuButton, new Color(46, 139, 87));
        menuButton.addActionListener(e -> {
            Container parent = getParent();
            if (parent.getLayout() instanceof CardLayout layout) {
                layout.show(parent, "menu");
            }
        });

        JButton restartButton = new JButton("🔁 Play Again");
        styleButton(restartButton, new Color(30, 144, 255));
        restartButton.addActionListener(e -> restartGame());

        bottomPanel.add(menuButton);
        bottomPanel.add(restartButton);
        revalidate();
        repaint();
    }

    private void restartGame() {
        foundWords.clear();
        wordListModel.clear();
        for (String w : WORDS)
            wordListModel.addElement(w);

        generateGrid();
        createButtons();

        bottomPanel.removeAll();
        JButton backButton = new JButton("🏠 Back to Menu");
        styleButton(backButton, new Color(34, 139, 34));
        backButton.addActionListener(e -> {
            Container parent = getParent();
            if (parent.getLayout() instanceof CardLayout layout) {
                layout.show(parent, "menu");
            }
        });
        bottomPanel.add(backButton);
        revalidate();
        repaint();
    }

    /* BUTTON STYLING  */
    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
    }
}
