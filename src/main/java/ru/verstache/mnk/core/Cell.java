package ru.verstache.mnk.core;

public interface Cell extends Comparable<Cell> {
    int getX();

    int getY();

    boolean isStruck();

    default boolean equals(Cell another) {
        return getX() == another.getX() && getY() == another.getY();
    }

    @Override
    default int compareTo(Cell o) {
        if (getX() != o.getX()) {
            return getX() - o.getX();
        }
        return getY() - o.getY();
    }

}
