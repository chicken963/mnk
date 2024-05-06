package ru.verstache.mnk.core;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Field {

    int getHeight();

    int getWidth();

    default int getNumberInRow() {
        return Math.min(getHeight(), getWidth());
    }

    List<Cell> getCells();

    default List<Line> getRows() {
        return getCells().stream()
                .collect(Collectors.groupingBy(Cell::getX))
                .values().stream()
                .map(Row::new)
                .collect(Collectors.toList());
    }

    default List<Line> getColumns() {
        return getCells().stream()
                .collect(Collectors.groupingBy(Cell::getY))
                .values().stream()
                .map(Column::new)
                .collect(Collectors.toList());
    }

    default Set<Line> getDiagonals() {
        return Arrays.stream(DiagonalType.values())
                .map(diagonalType -> getSeedCells(diagonalType)
                        .map(cell -> getDiagonal(cell, diagonalType))
                        .collect(Collectors.toSet()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    default Stream<Line> getLinesStream() {
        return Stream.of(getRows(), getColumns(), getDiagonals())
                .flatMap(Collection::stream);
    }

    default boolean isVertical() {
        return getHeight() > getWidth();
    }

    default boolean isHorizontal() {
        return getWidth() > getHeight();
    }

    default boolean isSquare() {
        return getWidth() == getHeight();
    }

    default int countDiagonals() {
        int mainDiagonalsCount = Math.abs(getWidth() - getHeight()) + 1;
        return 2 * mainDiagonalsCount;
    }

    default Cell getCell(int rowIndex, int columnIndex) {
        if (columnIndex >= getWidth()) {
            throw new IndexOutOfBoundsException(
                    String.format("Invalid row index - value %d exceeds maximum allowed index %d", columnIndex, getWidth() - 1));
        }
        if (rowIndex >= getHeight()) {
            throw new IndexOutOfBoundsException(
                    String.format("Invalid column index - value %d exceeds maximum allowed index %d", rowIndex, getHeight() - 1));
        }
        return getCells().get(rowIndex * getWidth() + columnIndex);
    }

    private int getDiagonalLength() {
        return Math.min(getHeight(), getWidth());
    }

    private Stream<Cell> getSeedCells(DiagonalType type) {
        int seedCornerHeight = getHeight() - getDiagonalLength();
        int seedCornerWidth = getWidth() - getDiagonalLength();

        Stream<Cell> verticalCells = IntStream.range(0, seedCornerHeight + 1)
                .mapToObj(rowIndex -> getCell(rowIndex, type.getSeedColumnIndex(getWidth())));
        Stream<Cell> horizontalCells = IntStream.range(1, seedCornerWidth + 1)
                .mapToObj(columnIndex -> getCell(0, type.getSeedColumnIndex(getWidth(), columnIndex)));
        return Stream.concat(verticalCells, horizontalCells).distinct();
    }

    private Diagonal getDiagonal(Cell seedCell, DiagonalType type) {
        return getDiagonal(seedCell.getX(), seedCell.getY(), type);
    }

    private Diagonal getDiagonal(int seedRowIndex, int seedColumnIndex, DiagonalType type) {
        List<Cell> diagonalCells = IntStream.range(0, getDiagonalLength())
                .mapToObj(i -> getCell(type.shiftRowIndex(seedRowIndex, i), type.shiftColumnIndex(seedColumnIndex, i)))
                .collect(Collectors.toList());
        return new Diagonal(diagonalCells);
    }
}
