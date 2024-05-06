package ru.verstache.mnk.manager;

import ru.verstache.mnk.core.Cell;
import ru.verstache.mnk.core.Field;

public class StrokeLinesManager {

    public int countStruckLines(Field field, Cell... nextCells) {
        return (int) field.getLinesStream()
                .filter(line -> line.isStruck(field.getNumberInRow(), nextCells))
                .count();
    }

}
