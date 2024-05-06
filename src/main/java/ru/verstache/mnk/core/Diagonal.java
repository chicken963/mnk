package ru.verstache.mnk.core;

import java.util.List;

public class Diagonal implements Line {

    List<Cell> cells;

    Diagonal(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Cell> getCells() {
        return cells;
    }
}
