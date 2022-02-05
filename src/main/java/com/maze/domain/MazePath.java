package com.maze.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data @AllArgsConstructor
public class MazePath {
    private List<Cell> cells;
}
