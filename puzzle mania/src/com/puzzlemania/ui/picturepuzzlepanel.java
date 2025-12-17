package com.puzzlemania.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.util.*;

public class picturepuzzlepanel extends JPanel {

    private static final int GRID_SIZE = 5;
    private final java.util.List<ImageIcon> correctOrder = new ArrayList<>();
    private java.util.List<ImageIcon> currentOrder;
    private int firstIndex = -1;
    private JPanel gridPanel;

    public picturepuzzlepanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("🧩 Picture Puzzle Challenge", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Perfect seamless grid
        gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 0, 0));
        gridPanel.setBackground(Color.BLACK);
        add(gridPanel, BorderLayout.CENTER);

        // BOTTOM BUTTON BAR
        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));

        // Make buttons larger, rounder, and more vibrant
        JButton shuffleBtn = makeBigButton("🔄  Shuffle Again", new Color(0, 153, 255));
        JButton backBtn = makeBigButton("🏠  Back to Menu", new Color(255, 102, 102));

        shuffleBtn.addActionListener(e -> shufflePuzzle());
        backBtn.addActionListener(e -> goBackToMenu());

        bottom.add(shuffleBtn);
        bottom.add(Box.createHorizontalStrut(30)); // spacing between buttons
        bottom.add(backBtn);
        add(bottom, BorderLayout.SOUTH);

        loadAndSplitImage();
        shufflePuzzle();
    }

    //  Custom big colorful button style
    private JButton makeBigButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        btn.setOpaque(true);

        // Subtle hover glow
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    private void goBackToMenu() {
        Container parent = getParent();
        if (parent != null && parent.getLayout() instanceof CardLayout) {
            ((CardLayout) parent.getLayout()).show(parent, "menu");
        }
    }

    private void loadAndSplitImage() {
        try {
            URL imgURL = getClass().getResource("/com/puzzlemania/assets/viratkohli.jpg");
            if (imgURL == null)
                throw new RuntimeException("Image not found! Ensure it's in src/com/puzzlemania/assets/");

            BufferedImage image = ImageIO.read(imgURL);
            int finalSize = 600;

            Image scaled = image.getScaledInstance(finalSize, finalSize, Image.SCALE_SMOOTH);
            BufferedImage scaledImg = new BufferedImage(finalSize, finalSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = scaledImg.createGraphics();
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();

            int tileW = scaledImg.getWidth() / GRID_SIZE;
            int tileH = scaledImg.getHeight() / GRID_SIZE;

            correctOrder.clear();
            for (int r = 0; r < GRID_SIZE; r++) {
                for (int c = 0; c < GRID_SIZE; c++) {
                    BufferedImage sub = scaledImg.getSubimage(c * tileW, r * tileH, tileW, tileH);
                    correctOrder.add(new ImageIcon(sub));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shufflePuzzle() {
        currentOrder = new ArrayList<>(correctOrder);
        Collections.shuffle(currentOrder);
        renderTiles();
    }

    private void renderTiles() {
        gridPanel.removeAll();

        for (int i = 0; i < currentOrder.size(); i++) {
            JLabel tile = new JLabel(currentOrder.get(i)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Insets insets = getInsets();
                    g.drawImage(((ImageIcon) getIcon()).getImage(),
                            insets.left, insets.top,
                            getWidth() - insets.left - insets.right,
                            getHeight() - insets.top - insets.bottom, this);
                }
            };
            tile.setBorder(null);
            tile.setOpaque(false);

            int index = i;
            tile.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleTileClick(index, tile);
                }
            });

            gridPanel.add(tile);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void handleTileClick(int index, JLabel tile) {
        if (firstIndex == -1) {
            firstIndex = index;
            tile.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        } else if (firstIndex == index) {
            tile.setBorder(null);
            firstIndex = -1;
        } else {
            JLabel firstTile = (JLabel) gridPanel.getComponent(firstIndex);
            firstTile.setBorder(null);

            ImageIcon temp = currentOrder.get(firstIndex);
            currentOrder.set(firstIndex, currentOrder.get(index));
            currentOrder.set(index, temp);

            renderTiles();
            firstIndex = -1;

            if (isSolved()) {
                JOptionPane.showMessageDialog(
                        this,
                        "🏏 That’s a clean hit! You’ve completed the Kohli puzzle perfectly!",
                        "Champion!",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }

    private boolean isSolved() {
        for (int i = 0; i < currentOrder.size(); i++) {
            if (currentOrder.get(i) != correctOrder.get(i))
                return false;
        }
        return true;
    }
}
