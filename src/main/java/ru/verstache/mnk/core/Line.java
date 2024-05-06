package ru.verstache.mnk.core;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface Line extends Strikable {

    default List<SubLine> getSubLines() {
        return getSubLines(length());
    }

    default List<SubLine> getSubLines(int k) {
        List<Cell> cellsOrdered = getCellsOrdered();
        return IntStream.range(0, length() - k + 1)
                .mapToObj(i -> (SubLine) () -> cellsOrdered.subList(i, i + k))
                .collect(Collectors.toList());
    }

    default boolean isStruck(Cell... nextCells) {
        return isStruck(length(), nextCells);
    }

    default boolean isStruck(int k, Cell... nextCells) {
        return getSubLines(k)
                .stream().anyMatch(subLine -> subLine.isStruckWith(nextCells));
    }

    default List<SubLine> getStruckSubLines(int k, Cell... nextCells) {
        return getSubLines(k).stream()
                .filter(subLine -> subLine.isStruckWith(nextCells))
                .toList();
    }

}
