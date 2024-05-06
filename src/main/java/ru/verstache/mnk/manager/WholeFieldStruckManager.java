package ru.verstache.mnk.manager;

import ru.verstache.mnk.core.Cell;
import ru.verstache.mnk.core.Field;

import java.util.Arrays;

public class WholeFieldStruckManager {

    public boolean wholeFieldStruck(Field field, Cell... nextCells) {
        return field.getCells().stream()
                .filter(cell -> Arrays.stream(nextCells).noneMatch(nextCell -> nextCell == cell))
                .allMatch(Cell::isStruck);
    }

    public boolean wholeFieldStruck(Field field) {
        return field.getCells().stream()
                .allMatch(Cell::isStruck);
    }
}
