package org.hupisoft.battleships_core;

import java.util.ArrayList;

class Ship implements IShip {

    private int mLength;
    private Coordinate mBowCoordinate;
    private Orientation mOrientation;
    private int mHitCount;

    Ship(int length) {
        this(length, new Coordinate(0,0), Orientation.VERTICAL);
    }

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
    public int length() {
        return mLength;
    }

    @Override
    public Coordinate getBowCoordinates() {
        return mBowCoordinate;
    }

    @Override
    public Coordinate getRearCoordinates() {
        int x = mBowCoordinate.x();
        int y = mBowCoordinate.y();

        if (mOrientation == Orientation.HORISONTAL) {
            x = x + length() - 1;
        } else {
            y = y + length() - 1;
        }

        return new Coordinate(x, y);
    }

    @Override
    public ArrayList<Coordinate> getOccupiedCoordinates() {
        int lastX = mBowCoordinate.x();
        int lastY = mBowCoordinate.y();

        if (mOrientation == Orientation.HORISONTAL) {
            lastX = lastX + length() - 1;
        } else {
            lastY = lastY + length() - 1;
        }

        return gatherCoordinates(mBowCoordinate.x(), lastX, mBowCoordinate.y(), lastY);
    }

    @Override
    public ArrayList<Coordinate> getRestrictedCoordinates() {
        int lastX = mBowCoordinate.x() + 1;
        int lastY = mBowCoordinate.y() + 1;

        if (mOrientation == Orientation.HORISONTAL) {
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

    private ArrayList<Coordinate> gatherCoordinates(int firstX, int lastX, int firstY, int lastY) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();

        for (int x = firstX; x <= lastX; ++x) {
            for (int y = firstY; y <= lastY; ++y) {
                coordinates.add(new Coordinate(x,y));
            }
        }

        return  coordinates;
    }
}
