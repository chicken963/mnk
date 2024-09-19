package ru.verstache.mnk.core;

public interface Cell extends Comparable<Cell> {
    int getRowIndex();

    int getColumnIndex();

    boolean isStruck();

    default boolean equals(Cell another) {
        return getRowIndex() == another.getRowIndex() && getColumnIndex() == another.getColumnIndex();
    }

    @Override
    default int compareTo(Cell o) {
        if (getRowIndex() != o.getRowIndex()) {
            return getRowIndex() - o.getRowIndex();
        }
        return getColumnIndex() - o.getColumnIndex();
    }

}
