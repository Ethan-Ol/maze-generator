package com.maze.solver;

import com.maze.domain.Maze;
import com.maze.domain.MazePath;

public interface MazeSolver {
    MazePath solve(Maze maze);
}
