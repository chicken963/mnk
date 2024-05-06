package ru.verstache.mnk.manager;

import ru.verstache.mnk.core.Cell;
import ru.verstache.mnk.core.Field;
import ru.verstache.mnk.core.SubLine;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.verstache.mnk.utils.CombinationUtils.generateCombinations;

public class PercentageManager {

    public String countWinningPercentage(Field field, int targetLinesCount, Cell... cells) {
        double percentage = getAllNLengthSubsets(field, targetLinesCount)
                .map(lineset -> this.countStrokePercentage(lineset, field, cells))
                .max(Double::compareTo).orElse(0d);
        return String.format("%.2f", percentage * 100);
    }


    public String countWinningPercentage(Field field) {
        Set<Cell> fieldCells = new HashSet<>(field.getCells());
        Set<Cell> strokeCells = fieldCells.stream()
                .filter(Cell::isStruck)
                .collect(Collectors.toSet());
        double percentage = (double) strokeCells.size() / fieldCells.size();
        return String.format("%.2f", percentage * 100);
    }


    private Stream<List<SubLine>> getAllNLengthSubsets(Field field, int subsetSize) {
        int k = field.getNumberInRow();
        List<List<SubLine>> sublinesFromLines = field.getLinesStream()
                .map(line -> line.getSubLines(k))
                .toList();
        int n = sublinesFromLines.size();
        if (subsetSize > n) {
            System.out.println("Subset size is greater than the set size.");
            return Stream.of(new ArrayList<>());
        }
        List<List<SubLine>> subsetsFromDifferentLines = generateCombinations(sublinesFromLines, subsetSize);
        return subsetsFromDifferentLines.stream().parallel();
    }


    private double countStrokePercentage(List<SubLine> strokeLines, Field field, Cell... cells) {
        return (double) countStrokeCells(strokeLines, field, cells) / countCells(strokeLines, field, cells);
    }


    private int countCells(List<SubLine> strokeLines, Field field, Cell... cells) {
        return countCellsByCondition(strokeLines, field, Optional.empty(), cells);
    }


    private int countStrokeCells(List<SubLine> strokeLines, Field field, Cell... cells) {
        return countCellsByCondition(strokeLines, field, Optional.of(Cell::isStruck), cells);
    }

    private int countCellsByCondition(List<SubLine> sublines, Field field, Optional<Function<Cell, Boolean>> condition, Cell... cells) {
        return Stream.concat(
                sublines.stream()
                    .map(SubLine::getCells)
                    .flatMap(Collection::stream),
                Arrays.stream(cells)
                )
                .collect(mergingSamePosition(field))
                .values()
                .stream()
                .filter(cell -> condition.isEmpty() || condition.get().apply(cell))
                .collect(mergingSamePosition(field))
                .values()
                .size();
    }

    private static Collector<Cell, ?, Map<Integer, Cell>> mergingSamePosition(Field field) {
        return Collectors.toMap(
                cell -> cell.getY() * field.getWidth() + cell.getX(),
                Function.identity(),
                (a, b) -> b);
    }

}
