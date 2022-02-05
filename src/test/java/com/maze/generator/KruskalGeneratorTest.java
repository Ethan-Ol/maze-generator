package com.maze.generator;

import com.maze.domain.*;
import com.maze.solver.CustomMazeSolver;
import com.maze.solver.MazeSolver;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;


//TODO tester les cas aux limite (max INT pour width et height)
class KruskalGeneratorTest {

    public static final int EXPECTED_HEIGHT = 10;
    public static final int EXPECTED_WIDTH = 12;
    private static final Position START = new Position(0, 0);
    private static final Position END = new Position(EXPECTED_WIDTH - 1, EXPECTED_HEIGHT - 1);

    @Test
    void shouldGenerateMaze() {
        MazeGenerator generator = new KruskalGenerator();
        Maze maze = generator.generate(EXPECTED_WIDTH, EXPECTED_HEIGHT, START, END);
        Assertions.assertNotNull(maze);
    }

    @Test
    void shouldGenerateMazeWithCorrectSize() {
        MazeGenerator generator = new KruskalGenerator();
        Maze maze = generator.generate(EXPECTED_WIDTH, EXPECTED_HEIGHT, START, END);
        Assertions.assertEquals(EXPECTED_WIDTH, maze.getWidth());
        Assertions.assertEquals(EXPECTED_HEIGHT, maze.getHeight());

        Assertions.assertEquals(EXPECTED_WIDTH, maze.getGrid().length);
        for (int x = 0; x < EXPECTED_WIDTH; x++) {
            Assertions.assertEquals(EXPECTED_HEIGHT, maze.getGrid()[x].length);
        }
    }

    @Test
    void shouldGenerateMazeWithoutNullCell() {
        MazeGenerator generator = new KruskalGenerator();
        Maze maze = generator.generate(EXPECTED_WIDTH, EXPECTED_HEIGHT, START, END);

        for (Cell[] cells : maze.getGrid()) {
            for (Cell cell : cells) {
                Assertions.assertNotNull(cell);
            }
        }
    }

    @Test
    void shouldGenerateMazeCellWithCoordinates() {
        MazeGenerator generator = new KruskalGenerator();
        Maze maze = generator.generate(EXPECTED_WIDTH, EXPECTED_HEIGHT, START, END);
        Cell[][] grid = maze.getGrid();

        for (int x = 0; x < maze.getWidth(); x++) {
            for (int y = 0; y < maze.getHeight(); y++) {
                Cell cell = grid[x][y];
                Assertions.assertEquals(x, cell.getPosition().getX());
                Assertions.assertEquals(y, cell.getPosition().getY());
            }
        }
    }

    @Test
    void shouldGenerateMazeWithOnlyOneStartCell() {
        MazeGenerator generator = new KruskalGenerator();
        Maze maze = generator.generate(EXPECTED_WIDTH, EXPECTED_HEIGHT, START, END);

        Assertions.assertEquals(1L, getNumberOfCellsByType(maze, Type.START));
    }

    @Test
    void shouldGenerateMazeWithOnlyOneEndCell() {
        MazeGenerator generator = new KruskalGenerator();
        Maze maze = generator.generate(EXPECTED_WIDTH, EXPECTED_HEIGHT, START, END);

        Assertions.assertEquals(1L, getNumberOfCellsByType(maze, Type.END));
    }

    @Test
    void shouldGenerateMazeWithAllCellsConnected() {
        MazeGenerator generator = new KruskalGenerator();
        Maze maze = generator.generate(EXPECTED_WIDTH, EXPECTED_HEIGHT, START, END);

        Set<Cell> visitedCells = new HashSet<>();
        Set<Cell> notVisitedCells = new HashSet<>();

        ArrayList<Cell> currentsCells;
        notVisitedCells.add(maze.getStart());

        do {
            currentsCells = new ArrayList<>(notVisitedCells);
            visitedCells.addAll(currentsCells);
            notVisitedCells.clear();
            for (Cell currentCell : currentsCells) {
                for (Cell neighbor : currentCell.getNeighbors()) {
                    if (!visitedCells.contains(neighbor)) {
                        notVisitedCells.add(neighbor);
                    }
                }
            }
        } while (!notVisitedCells.isEmpty());

        Assertions.assertEquals(maze.getHeight() * maze.getWidth(), visitedCells.size());
    }

    private long getNumberOfCellsByType(Maze maze, Type type) {
        return Arrays.stream(maze.getGrid()).flatMap(Arrays::stream).filter(cell -> cell.getType() == type).count();
    }

}
