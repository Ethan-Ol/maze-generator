package com.maze.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data @AllArgsConstructor
public class Cell {
    private Type type;

    private final Position position;

    private Set<Cell> neighbors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return position == cell.getPosition();
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}
