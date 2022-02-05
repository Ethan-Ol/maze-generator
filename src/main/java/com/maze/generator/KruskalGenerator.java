package com.maze.generator;

import com.maze.domain.*;

import java.util.*;

/**
 * Generate maze with Kruskal algorithm (https://en.wikipedia.org/wiki/Maze_generation_algorithm#Randomized_Kruskal's_algorithm)
 */
public class KruskalGenerator implements MazeGenerator {
    @Override
    public Maze generate(int width, int height, Position start, Position end) {
        Cell[][] grid = initGrid(width, height);
        List<Wall> walls = initWalls(grid, width, height);

        Cell cellStart = grid[start.getX()][start.getY()];
        cellStart.setType(Type.START);

        Cell cellEnd = grid[end.getX()][end.getY()];
        cellEnd.setType(Type.END);

        Maze maze = new Maze(grid, new ArrayList<>(walls), width, height, cellStart, cellEnd);
        Map<Cell, CellsGroup> groupByCell = new HashMap<>();

        generateRandomMaze(walls, maze, groupByCell);

        return maze;
    }

    private void generateRandomMaze(List<Wall> walls, Maze maze, Map<Cell, CellsGroup> groupByCell) {
        Collections.shuffle(walls);
        for (Wall wall : walls) {
            Cell cellA = wall.getCellA();
            var groupA = groupByCell.get(cellA);
            Cell cellB = wall.getCellB();
            var groupB = groupByCell.get(cellB);

            if (groupA == null) {
                groupA = new CellsGroup(new HashSet<>(List.of(cellA)));
                groupByCell.put(cellA, groupA);
            }

            if (groupA != groupB) {
                if (groupB == null) {
                    groupA.getCells().add(cellB);
                    groupByCell.put(cellB, groupA);
                } else {
                    if (groupA.getCells().size() > groupB.getCells().size()) {
                        mergeGroups(groupByCell, groupA, groupB);
                    } else {
                        mergeGroups(groupByCell, groupB, groupA);
                    }
                }

                cellA.getNeighbors().add(cellB);
                cellB.getNeighbors().add(cellA);
                maze.getWalls().remove(wall);
            }
        }
    }

    private Cell[][] initGrid(int width, int height) {
        Cell[][] grid = new Cell[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell(Type.EMPTY, new Position(x, y), new HashSet<>());
            }
        }

        return grid;
    }

    private void mergeGroups(Map<Cell, CellsGroup> groupByCell, CellsGroup receiver, CellsGroup giver) {
        receiver.getCells().addAll(giver.getCells());
        for (Cell cell : giver.getCells()) {
            groupByCell.put(cell, receiver);
        }
    }

    private List<Wall> initWalls(Cell[][] grid, int width, int height) {
        List<Wall> walls = new ArrayList<>(width * height);

        for (int x = 0; x < width - 1; x++) {
            for (int y = 0; y < height; y++) {
                walls.add(new Wall(grid[x][y], grid[x + 1][y]));
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height - 1; y++) {
                walls.add(new Wall(grid[x][y], grid[x][y + 1]));
            }
        }

        return walls;
    }
}
