package ru.verstache.mnk.core;

import java.util.Arrays;
import java.util.List;

public interface Strikable {

    List<Cell> getCells();

    default int length() {
        return getCells().size();
    }

    default List<Cell> getCellsOrdered() {
        Cell[] cellsSnapshot = getCells().toArray(new Cell[length()]);
        Arrays.sort(cellsSnapshot, Cell::compareTo);
        return Arrays.asList(cellsSnapshot);
    }
}
