package com.maze.generator;

import com.maze.domain.Maze;
import com.maze.domain.Position;

public interface MazeGenerator {
    Maze generate(int width, int height, Position start, Position end);
}
