package com.maze.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Wall {
    private Cell cellA;
    private Cell cellB;
}
