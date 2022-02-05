package com.maze.renderer;

import com.maze.domain.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MazeRenderer {

    private final int cellSize;

    public MazeRenderer(int cellSize) {
        this.cellSize = cellSize;
    }

    public BufferedImage generateImage(Maze maze, MazePath path) {
        int imageWidth = maze.getWidth() * cellSize + 1;
        int imageHeight = maze.getHeight() * cellSize + 1;

        BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = bufferedImage.createGraphics();
        generateMazeGraphics(graphics, maze, path);
        graphics.dispose();

        return bufferedImage;
    }

    private void generateMazeGraphics(Graphics2D graphics, Maze maze, MazePath mazePath) {
        printCells(graphics, maze);
        printMazePath(graphics, mazePath);
        printWalls(graphics, maze);
    }

    private void printWalls(Graphics2D graphics, Maze maze) {
        graphics.setColor(Color.black);
        int x0 = 0;
        int y0 = 0;
        int xN = (maze.getWidth()) * cellSize;
        int yN = (maze.getHeight())  * cellSize;
        graphics.drawLine(x0, y0, xN, y0);
        graphics.drawLine(x0, y0, x0, yN);
        graphics.drawLine(xN, y0, xN, yN);
        graphics.drawLine(x0, yN, xN, yN);

        for (Wall wall : maze.getWalls()) {
            if (wall.getCellA().getPosition().getX() < wall.getCellB().getPosition().getX()) {
                graphics.drawLine(wall.getCellB().getPosition().getX()  * cellSize, wall.getCellB().getPosition().getY() * cellSize, wall.getCellB().getPosition().getX() * cellSize, wall.getCellB().getPosition().getY() * cellSize + cellSize);
            } else {
                graphics.drawLine(wall.getCellB().getPosition().getX() * cellSize, wall.getCellB().getPosition().getY() * cellSize, wall.getCellB().getPosition().getX() * cellSize + cellSize, wall.getCellB().getPosition().getY() * cellSize);
            }
        }
    }

    private void printMazePath(Graphics2D graphics, MazePath mazePath) {
        for (Cell cell : mazePath.getCells()) {
            graphics.setColor(Color.MAGENTA);
            graphics.fillRect(cell.getPosition().getX() * cellSize, cell.getPosition().getY() * cellSize, cellSize, cellSize); // fill new rectangle with color blue
        }
    }

    private void printCells(Graphics2D graphics, Maze maze) {
        for (int x = 0; x < maze.getWidth(); x++) {
            for (int y = 0; y < maze.getHeight(); y++) {
                Cell cell = maze.getGrid()[x][y];
                graphics.setColor(getColor(cell.getType()));
                graphics.fillRect(x * cellSize, y * cellSize, cellSize, cellSize); // fill new rectangle with color blue
            }
        }
    }

    private static Color getColor(Type type) {
        return switch (type) {
            case START -> Color.blue;
            case END -> Color.green;
            default -> Color.white;
        };
    }
}
