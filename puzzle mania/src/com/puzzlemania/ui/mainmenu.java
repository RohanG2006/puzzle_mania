package com.puzzlemania.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class mainmenu extends JPanel {

    public mainmenu(JPanel cards) {
        setLayout(new BorderLayout());
        setBackground(new Color(3, 80, 20)); // rich cricket field green

        // TITLE
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(3, 80, 20));

        JLabel title = new JLabel("🏏 PUZZLE MANIA 🏏", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 42));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Cricket + Brain = Championship!", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        subtitle.setForeground(new Color(255, 255, 180));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(Box.createVerticalStrut(60));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(subtitle);
        titlePanel.add(Box.createVerticalStrut(40));

        add(titlePanel, BorderLayout.NORTH);

        // BUTTON PANEL 
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(3, 80, 20));
        buttonPanel.setLayout(new GridLayout(3, 1, 25, 25)); // equal spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 200, 80, 200));

        JButton wordSearchBtn = createMenuButton("🏏  Play Word Search");
        JButton picturePuzzleBtn = createMenuButton("🧩  Play Picture Puzzle");
        JButton exitBtn = createMenuButton("🚪  Exit");

        // Actions
        wordSearchBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "wordsearch");
        });

        picturePuzzleBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "picturepuzzle");
        });

        exitBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(wordSearchBtn);
        buttonPanel.add(picturePuzzleBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.CENTER);

        //  FOOTER 
        JLabel footer = new JLabel("✨ Powered by Team ROSI — Always on the pitch! ✨", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        footer.setForeground(new Color(240, 240, 200));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        add(footer, BorderLayout.SOUTH);
    }

    // Helper Method to Style Buttons 
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 128, 64)); // rich green
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 3),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(320, 70));

        //  Hover Effect 
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(34, 139, 34)); // lighter green
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.YELLOW, 3),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0, 128, 64)); // back to default
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 3),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });

        return btn;
    }
}
