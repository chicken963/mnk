package ru.verstache.mnk.core;

import java.util.Arrays;

public interface SubLine extends Strikable {

    default boolean isStruck() {
        return getCells().stream().allMatch(Cell::isStruck);
    }


    default boolean isStruckWith(Cell... nextCells) {
        return getCells().stream()
                .filter(cell -> Arrays.stream(nextCells)
                        .noneMatch(nextCell -> nextCell.compareTo(cell) == 0))
                .allMatch(Cell::isStruck);
    }
}
