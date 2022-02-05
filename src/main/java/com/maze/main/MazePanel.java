package com.maze.main;

import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    private final transient Image image;

    public MazePanel(Image image) {
        this.image = image;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}
