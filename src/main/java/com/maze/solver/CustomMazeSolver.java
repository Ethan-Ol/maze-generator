package com.maze.solver;

import com.maze.domain.Cell;
import com.maze.domain.Maze;
import com.maze.domain.MazePath;
import com.maze.domain.Type;

import java.util.*;

/**
 * This algorithm is a custom implementation to find a path between two cells in a perfect maze
 *
 * It is a recursive algorithm that browse cells from start and set a distance from the start for each cell until it reach the end cell.
 */

public class CustomMazeSolver implements MazeSolver {

    @Override
    public MazePath solve(Maze maze) {
        Cell start = maze.getStart();
        Cell end = maze.getEnd();

        Map<Cell, Integer> cellsdst = initCellDst(start);

        List<Cell> solution = new ArrayList<>();

        Cell current = end;

        while (current != start) {
            Optional<Cell> min = getMinNeighbour(cellsdst, current);
            if (min.isPresent()) {
                current = min.get();
                if (current != start) {
                    solution.add(current);
                }
            } else {
                throw new IllegalStateException("Cell " + current + " should have min neighbor");
            }
        }

        return new MazePath(solution);
    }

    private Optional<Cell> getMinNeighbour(Map<Cell, Integer> cellsdst, Cell current) {
        return current.getNeighbors().stream().filter(cellsdst::containsKey).min(Comparator.comparingInt(cellsdst::get));
    }

    private Map<Cell, Integer> initCellDst(Cell start) {
        Map<Cell, Integer> cellsdst = new HashMap<>();
        int currentDistance = 0;
        browseNeighbors(cellsdst, start, currentDistance);
        return cellsdst;
    }

    private void browseNeighbors(Map<Cell, Integer> cellsdst, Cell current, int currentDistance) {
        if (current.getType() == Type.END) {
            return;
        }
        cellsdst.put(current, currentDistance);
        for (Cell neighbor : current.getNeighbors()) {
            if (!cellsdst.containsKey(neighbor)) {
                browseNeighbors(cellsdst, neighbor, currentDistance + 1);
            }
        }
    }
}
