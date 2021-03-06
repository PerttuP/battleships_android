package org.hupisoft.battleships_core;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the IShip interface.
 * @see IShip
 */
class Ship implements IShip {

    private int mLength;
    private Coordinate mBowCoordinate;
    private Orientation mOrientation;
    private int mHitCount;

    /**
     * Create ship with only length specified. Other properties are to be set later.
     * @param length Ship length in squares.
     */
    Ship(int length) {
        this(length, new Coordinate(0,0), Orientation.VERTICAL);
    }

    /**
     * Create ship with all properties specified.
     * @param length Ship length in squares.
     * @param bowCoordinate Game area coordinates for the head of the ship.
     * @param orientation Ship orientation.
     */
    Ship(int length, Coordinate bowCoordinate, Orientation orientation) {
        mLength = length;
        mBowCoordinate = bowCoordinate;
        mOrientation = orientation;
        mHitCount = 0;
    }

    @Override
    public boolean isDestroyed() {
        return hitCount() >= length();
    }

    @Override
    public Orientation orientation() {
        return mOrientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        mOrientation = orientation;
    }

    @Override
    public int length() {
        return mLength;
    }

    @Override
    public Coordinate getBowCoordinates() {
        return mBowCoordinate;
    }

    @Override
    public void setBowCoordinates(Coordinate location) {
        mBowCoordinate = location;
    }

    @Override
    public Coordinate getRearCoordinates() {
        int x = mBowCoordinate.x();
        int y = mBowCoordinate.y();

        if (mOrientation == Orientation.HORIZONTAL) {
            x = x + length() - 1;
        } else {
            y = y + length() - 1;
        }

        return new Coordinate(x, y);
    }

    @Override
    public List<Coordinate> getOccupiedCoordinates() {
        int lastX = mBowCoordinate.x();
        int lastY = mBowCoordinate.y();

        if (mOrientation == Orientation.HORIZONTAL) {
            lastX = lastX + length() - 1;
        } else {
            lastY = lastY + length() - 1;
        }

        return gatherCoordinates(mBowCoordinate.x(), lastX, mBowCoordinate.y(), lastY);
    }

    @Override
    public List<Coordinate> getRestrictedCoordinates() {
        int lastX = mBowCoordinate.x() + 1;
        int lastY = mBowCoordinate.y() + 1;

        if (mOrientation == Orientation.HORIZONTAL) {
            lastX = mBowCoordinate.x() + length();
        } else {
            lastY = mBowCoordinate.y() + length();
        }

        return gatherCoordinates(mBowCoordinate.x()-1, lastX, mBowCoordinate.y()-1, lastY);
    }

    @Override
    public int hitCount() {
        return mHitCount;
    }

    @Override
    public boolean hit() {
        ++mHitCount;
        return isDestroyed();
    }

    private List<Coordinate> gatherCoordinates(int firstX, int lastX, int firstY, int lastY) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();

        for (int x = firstX; x <= lastX; ++x) {
            for (int y = firstY; y <= lastY; ++y) {
                coordinates.add(new Coordinate(x,y));
            }
        }

        return  coordinates;
    }
}
