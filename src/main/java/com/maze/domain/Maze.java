package com.maze.domain;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data @RequiredArgsConstructor
public class Maze {
    @NonNull
    private final Cell[][] grid;
    @NonNull
    private List<Wall> walls;

    @NonNull
    private final int width;
    @NonNull
    private final int height;

    @NonNull
    private final Cell start;
    @NonNull
    private final Cell end;
}
