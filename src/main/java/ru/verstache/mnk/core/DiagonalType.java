package ru.verstache.mnk.core;

import java.util.function.BiFunction;

enum DiagonalType {
    MAIN(Integer::sum, Integer::sum),
    SECONDARY(Integer::sum, (seedY, i) -> seedY - i);

    private final BiFunction<Integer, Integer, Integer> verticalIndexIterator;
    private final BiFunction<Integer, Integer, Integer> horizontalIndexIterator;

    DiagonalType(BiFunction<Integer, Integer, Integer> horizontalIndexIterator,
                 BiFunction<Integer, Integer, Integer> verticalIndexIterator) {
        this.horizontalIndexIterator = horizontalIndexIterator;
        this.verticalIndexIterator = verticalIndexIterator;
    }

    public int shiftRowIndex(int seedRowIndex, int delta) {
        return horizontalIndexIterator.apply(seedRowIndex, delta);
    }

    public int shiftColumnIndex(int seedColumnIndex, int delta) {
        return verticalIndexIterator.apply(seedColumnIndex, delta);
    }

    public int getSeedColumnIndex(int fieldWidth) {
        return getSeedColumnIndex(fieldWidth, 0);
    }

    public int getSeedColumnIndex(int fieldWidth, int columnIndex) {
        return this == DiagonalType.MAIN ? columnIndex : fieldWidth - columnIndex - 1;
    }
}
