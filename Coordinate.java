package com.example;

import java.util.Objects;

public class Coordinate {
    int x;
    int y;
    int dir;
    int indir;
    char value;
    Coordinate parent;

    public Coordinate(int x, int y, char value) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.parent = null;
    }

    public Coordinate(int x, int y, int dir, char value) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.parent = null;
    }

    public Coordinate(int x, int y, int dir, int indir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.indir = indir;
    }

    public Coordinate(int x, int y, char value, Coordinate parent) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.parent = parent;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    char getValue() {
        return value;
    }

    Coordinate getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y && value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, value);
    }
}
