package com.maze.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @EqualsAndHashCode @NoArgsConstructor
public class Position {

    /***
     * The horizontal coordinate
     */
    private int x;

    /***
     * The vertical coordinate
     */
    private int y;
}
