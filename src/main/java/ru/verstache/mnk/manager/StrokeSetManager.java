package ru.verstache.mnk.manager;

import ru.verstache.mnk.core.Cell;
import ru.verstache.mnk.core.Field;
import ru.verstache.mnk.core.Line;
import ru.verstache.mnk.core.SubLine;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StrokeSetManager {

    public Set<Cell> getStrokeSet(Field field, Cell... nextCells) {
        return new HashSet<>(field.getLinesStream()
                .filter(line -> line.isStruck(field.getNumberInRow(), nextCells))
                .map(line -> line.getStruckSubLines(field.getNumberInRow(), nextCells).get(0))
                .map(SubLine::getCells)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        cell -> cell.getY() * field.getWidth() + cell.getX(),
                        Function.identity(),
                        (a, b) -> b))
                .values());
    }
}
