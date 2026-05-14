package com.mario.editor;

import com.mario.level.Level;
import com.mario.level.LevelManager;
import com.mario.level.Tile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import javax.swing.JPanel;

/**
 * Main level editor panel
 */
public class LevelEditor extends JPanel implements Runnable, KeyListener {
    private EditorState editorState;
    private EditorGrid editorGrid;
    private EditorUI editorUI;
    private Level currentLevel;
    private boolean running = true;
    private double mouseX, mouseY;
    private boolean mousePressed = false;

    public LevelEditor() {
        this.editorState = new EditorState();
        this.currentLevel = new Level(40, 20, "Custom Level");
        this.editorGrid = new EditorGrid(currentLevel, editorState);
        this.editorUI = new EditorUI(editorState);

        setFocusable(true);
        addKeyListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed = true;
                if (e.getButton() == MouseEvent.BUTTON3) {
                    editorGrid.handleRightClick(e);
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    editorUI.handleMouseClick(e, getWidth() - 200);
                    if (e.getX() < getWidth() - 200) {
                        editorGrid.handleMousePress(e);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mousePressed && e.getButton() == MouseEvent.BUTTON1 && 
                    e.getX() < getWidth() - 200) {
                    editorGrid.handleMouseDrag(e);
                }
            }
        });

        new Thread(this).start();
    }

    @Override
    public void run() {
        while (running) {
            repaint();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw grid
        editorGrid.render(g2d, getWidth() - 200, getHeight());

        // Draw UI panel
        editorUI.render(g2d, getWidth() - 200, getHeight());
    }

    public void saveLevel(String filename) {
        try {
            File dir = new File("levels");
            if (!dir.exists()) {
                dir.mkdir();
            }

            LevelData levelData = new LevelData(currentLevel);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(levelData);

            FileWriter writer = new FileWriter("levels/" + filename + ".json");
            writer.write(json);
            writer.close();

            System.out.println("Level saved: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLevel(String filename) {
        try {
            String content = new String(Files.readAllBytes(new File("levels/" + filename + ".json").toPath()));
            Gson gson = new Gson();
            LevelData levelData = gson.fromJson(content, LevelData.class);
            
            currentLevel = levelData.toLevel();
            editorGrid = new EditorGrid(currentLevel, editorState);
            System.out.println("Level loaded: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearLevel() {
        editorGrid.clearLevel();
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
        this.editorGrid = new EditorGrid(currentLevel, editorState);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
            // Ctrl+S to save
            saveLevel("custom_level");
        } else if (e.getKeyCode() == KeyEvent.VK_L && e.isControlDown()) {
            // Ctrl+L to load
            loadLevel("custom_level");
        } else if (e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
            // Ctrl+C to clear
            clearLevel();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Simple data class for serializing levels to JSON
     */
    public static class LevelData {
        public int[][] tiles;
        public int width;
        public int height;

        public LevelData() {
        }

        public LevelData(Level level) {
            this.width = level.getWidth();
            this.height = level.getHeight();
            this.tiles = new int[height][width];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Tile tile = level.getTile(x, y);
                    this.tiles[y][x] = tile.getType().ordinal();
                }
            }
        }

        public Level toLevel() {
            Level level = new Level(width, height, "Loaded Level");
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Tile.TileType type = Tile.TileType.values()[tiles[y][x]];
                    level.setTile(x, y, type);
                }
            }
            return level;
        }
    }
}
