package com.maze.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data @AllArgsConstructor
public class CellsGroup {
    private Set<Cell> cells;
}
