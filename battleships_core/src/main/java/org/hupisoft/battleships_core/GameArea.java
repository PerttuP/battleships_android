package org.hupisoft.battleships_core;

import java.util.ArrayList;

/**
 * Implements the IGameArea interface.
 * @see IGameArea
 */
class GameArea implements IGameArea {

    private ArrayList<ArrayList<ISquare>> mSquares = null;
    private ArrayList<IShip> mShips = null;

    /**
     * Constructor. Squares and ships are expected to be initialized previously.
     * @param squares Squares in array. Outer array represents column (x-coordinates) and innter array rows(y-coordinate).
     *                There must be at least one row and one column.
     *                All rows must have same length. All columns must have same length.
     * @param ships List of ships.
     */
    GameArea(ArrayList<ArrayList<ISquare>> squares, ArrayList<IShip> ships) {
        mSquares = squares;
        mShips = ships;
    }

    @Override
    public int width() {
        return mSquares.size();
    }

    @Override
    public int height() {
        return mSquares.get(0).size();
    }

    @Override
    public ISquare getSquare(Coordinate location) {
        try {
            return mSquares.get(location.x()).get(location.y());
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public ArrayList<IShip> getShips() {
        return mShips;
    }

    @Override
    public HitResult hit(Coordinate location) {
        ISquare sqr = getSquare(location);
        HitResult result = null;
        if (sqr != null) {
            result = sqr.hit();
        }
        return result;
    }

    @Override
    public int remainingShipCount() {
        int remaining = mShips.size();
        for (IShip ship : mShips) {
            if (ship.isDestroyed()) {
                --remaining;
            }
        }
        return remaining;
    }

    @Override
    public int hitCount() {
        int hitCount = 0;
        for (int x = 0; x < width(); ++x) {
            for (int y = 0; y < height(); ++y) {
                if (mSquares.get(x).get(y).isHit()) {
                    ++hitCount;
                }
            }
        }
        return  hitCount;
    }

    @Override
    public ArrayList<Coordinate> getUnHitLocations() {
        ArrayList<Coordinate> unhit = new ArrayList<>();
        for (int x = 0; x < width(); ++x) {
            for (int y = 0; y < height(); ++y) {
                Coordinate c = new Coordinate(x,y);
                if (!getSquare(c).isHit()){
                    unhit.add(c);
                }
            }
        }
        return unhit;
    }
}
