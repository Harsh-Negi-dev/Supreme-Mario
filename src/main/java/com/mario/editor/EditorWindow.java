package com.mario.editor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Main window for the level editor
 */
public class EditorWindow extends JFrame {
    private LevelEditor editor;

    public EditorWindow() {
        setTitle("Supreme Mario - Level Editor (Phase 5)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        editor = new LevelEditor();
        setSize(1400, 800);
        setContentPane(editor);
        setLocationRelativeTo(null);
        setVisible(true);

        // Start editor
        editor.requestFocus();
    }

    public static void main(String[] args) {
        new EditorWindow();
    }
}
