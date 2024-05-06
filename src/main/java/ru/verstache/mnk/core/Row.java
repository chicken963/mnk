package ru.verstache.mnk.core;

import java.util.List;

public class Row implements Line {

    private final List<Cell> cells;

    Row(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Cell> getCells() {
        return cells;
    }
}
