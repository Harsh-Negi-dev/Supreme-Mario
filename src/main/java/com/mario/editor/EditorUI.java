package com.mario.editor;

import com.mario.level.Tile.TileType;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * UI panel for the level editor with tile/entity selection and buttons
 */
public class EditorUI {
    private static final int PANEL_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 40;
    private static final int BUTTON_MARGIN = 10;
    private EditorState editorState;
    private List<UIButton> buttons;

    public EditorUI(EditorState editorState) {
        this.editorState = editorState;
        this.buttons = new ArrayList<>();
        initializeButtons();
    }

    private void initializeButtons() {
        int y = BUTTON_MARGIN;

        // Title
        y += BUTTON_MARGIN * 2;

        // Tile selection buttons
        buttons.add(new UIButton("SOLID", BUTTON_MARGIN, y, PANEL_WIDTH - BUTTON_MARGIN * 2, BUTTON_HEIGHT) {
            @Override
            public void onClick() {
                editorState.setCurrentMode(EditorState.EditorMode.TILE_SELECTION);
                editorState.setSelectedTile(TileType.SOLID);
            }
        });
        y += BUTTON_HEIGHT + BUTTON_MARGIN;

        buttons.add(new UIButton("SPIKE", BUTTON_MARGIN, y, PANEL_WIDTH - BUTTON_MARGIN * 2, BUTTON_HEIGHT) {
            @Override
            public void onClick() {
                editorState.setCurrentMode(EditorState.EditorMode.TILE_SELECTION);
                editorState.setSelectedTile(TileType.SPIKE);
            }
        });
        y += BUTTON_HEIGHT + BUTTON_MARGIN;

        buttons.add(new UIButton("ICE", BUTTON_MARGIN, y, PANEL_WIDTH - BUTTON_MARGIN * 2, BUTTON_HEIGHT) {
            @Override
            public void onClick() {
                editorState.setCurrentMode(EditorState.EditorMode.TILE_SELECTION);
                editorState.setSelectedTile(TileType.ICE);
            }
        });
        y += BUTTON_HEIGHT + BUTTON_MARGIN;

        buttons.add(new UIButton("EMPTY", BUTTON_MARGIN, y, PANEL_WIDTH - BUTTON_MARGIN * 2, BUTTON_HEIGHT) {
            @Override
            public void onClick() {
                editorState.setCurrentMode(EditorState.EditorMode.TILE_SELECTION);
                editorState.setSelectedTile(TileType.EMPTY);
            }
        });
        y += BUTTON_HEIGHT + BUTTON_MARGIN;

        // Separator
        y += BUTTON_MARGIN;

        // Clear button
        buttons.add(new UIButton("CLEAR", BUTTON_MARGIN, y, PANEL_WIDTH - BUTTON_MARGIN * 2, BUTTON_HEIGHT) {
            @Override
            public void onClick() {
                // Will be handled by LevelEditor
            }
        });
        y += BUTTON_HEIGHT + BUTTON_MARGIN;

        // Save button
        buttons.add(new UIButton("SAVE LEVEL", BUTTON_MARGIN, y, PANEL_WIDTH - BUTTON_MARGIN * 2, BUTTON_HEIGHT) {
            @Override
            public void onClick() {
                // Will be handled by LevelEditor
            }
        });
        y += BUTTON_HEIGHT + BUTTON_MARGIN;

        // Load button
        buttons.add(new UIButton("LOAD LEVEL", BUTTON_MARGIN, y, PANEL_WIDTH - BUTTON_MARGIN * 2, BUTTON_HEIGHT) {
            @Override
            public void onClick() {
                // Will be handled by LevelEditor
            }
        });
        y += BUTTON_HEIGHT + BUTTON_MARGIN;

        // Test button
        buttons.add(new UIButton("TEST PLAY", BUTTON_MARGIN, y, PANEL_WIDTH - BUTTON_MARGIN * 2, BUTTON_HEIGHT) {
            @Override
            public void onClick() {
                // Will be handled by LevelEditor
            }
        });
    }

    public void render(Graphics2D g, int panelX, int panelHeight) {
        // Draw panel background
        g.setColor(new Color(50, 50, 50));
        g.fillRect(panelX, 0, PANEL_WIDTH, panelHeight);

        // Draw title
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(14f));
        g.drawString("EDITOR", panelX + BUTTON_MARGIN, 30);

        // Draw buttons
        for (UIButton button : buttons) {
            button.render(g, panelX);
        }

        // Draw selected tile info
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(12f));
        g.drawString("Selected:", panelX + BUTTON_MARGIN, panelHeight - 60);
        g.drawString(editorState.getSelectedTile().name(), panelX + BUTTON_MARGIN, panelHeight - 40);
    }

    public void handleMouseClick(MouseEvent e, int panelX) {
        int relativeX = e.getX() - panelX;
        int relativeY = e.getY();

        if (relativeX >= 0 && relativeX < PANEL_WIDTH) {
            for (UIButton button : buttons) {
                if (button.contains(relativeX, relativeY)) {
                    button.onClick();
                }
            }
        }
    }

    public UIButton getButtonByText(String text) {
        for (UIButton button : buttons) {
            if (button.getText().equals(text)) {
                return button;
            }
        }
        return null;
    }

    public abstract static class UIButton {
        private String text;
        private Rectangle bounds;

        public UIButton(String text, int x, int y, int width, int height) {
            this.text = text;
            this.bounds = new Rectangle(x, y, width, height);
        }

        public void render(Graphics2D g, int panelX) {
            Color bgColor = new Color(100, 100, 100);
            g.setColor(bgColor);
            g.fillRect(panelX + bounds.x, bounds.y, bounds.width, bounds.height);

            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(12f));
            int textX = panelX + bounds.x + 10;
            int textY = bounds.y + bounds.height / 2 + 5;
            g.drawString(text, textX, textY);

            // Border
            g.setColor(new Color(150, 150, 150));
            g.drawRect(panelX + bounds.x, bounds.y, bounds.width, bounds.height);
        }

        public boolean contains(int x, int y) {
            return bounds.contains(x, y);
        }

        public String getText() {
            return text;
        }

        public abstract void onClick();
    }
}
