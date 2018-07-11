package org.hupisoft.battleships_core;

import java.util.ArrayList;

/**
 * Utility class for expressing locations on game area.
 */
public class Coordinate {

    private int mX;
    private int mY;

    /**
     * Constructor.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Coordinate(int x, int y) {
        mX = x;
        mY = y;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Coordinate) {
            Coordinate otherCoord = (Coordinate)other;
            return otherCoord.x() == mX && otherCoord.y() == mY;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Coordinate{" + x() + "," + y() + "}";
    }

    /**
     * Get x-coordinate.
     * @return X-coordinate.
     */
    public int x() {
        return mX;
    }

    /**
     * Get y-coordinate.
     * @return Y-coordinate.
     */
    public int y() {
        return mY;
    }

    /**
     * Get neigbor coordinates (horisontal, vertical and diagonal).
     * @return List of neighbour cooordinates.
     */
    public ArrayList<Coordinate> neighbours() {
        ArrayList<Coordinate> neighbours = new ArrayList<>();
        for (int x = mX - 1; x <= mX+1; ++x) {
            for (int y = mY - 1; y <= mY + 1; ++y) {
                if (x == mX && y == mY) {
                    continue;
                } else {
                    neighbours.add(new Coordinate(x, y));
                }
            }
        }
        return  neighbours;
    }

    /**
     * Check if other coordinate is a neigbour horisontally, vertically or diagonally.
     * @param other Other coordinate.
     * @return True, if other is a neighbor.
     */
    public boolean isNeighbour(Coordinate other) {
        return neighbours().contains(other);
    }
}
