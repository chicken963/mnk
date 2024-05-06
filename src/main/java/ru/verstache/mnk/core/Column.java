package ru.verstache.mnk.core;

import java.util.List;

public class Column implements Line {
    private final List<Cell> cells;

    Column(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Cell> getCells() {
        return cells;
    }
}
