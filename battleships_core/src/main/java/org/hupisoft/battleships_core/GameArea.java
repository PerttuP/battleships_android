package org.hupisoft.battleships_core;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the IGameArea interface.
 * @see IGameArea
 */
class GameArea implements IGameArea {

    private List<List<ISquare>> mSquares;
    private List<IShip> mShips;
    private IGameAreaLogger mLogger;

    /**
     * Constructor. Squares and ships are expected to be initialized previously.
     * @param squares Squares in array. Outer array represents column (x-coordinates) and innter array rows(y-coordinate).
     *                There must be at least one row and one column.
     *                All rows must have same length. All columns must have same length.
     * @param ships List of ships.
     * @param logger Game area logger to record order of hits.
     */
    GameArea(List<List<ISquare>> squares, List<IShip> ships, IGameAreaLogger logger) {
        mSquares = squares;
        mShips = ships;
        mLogger = logger;
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
    public List<IShip> getShips() {
        return mShips;
    }

    @Override
    public HitResult hit(Coordinate location) {
        ISquare sqr = getSquare(location);
        HitResult result = null;
        if (sqr != null) {
            result = sqr.hit();
            if (result != HitResult.ALREADY_HIT) {
                // Only record actual hits.
                mLogger.recordAction(location, result);
            }
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
    public List<Coordinate> getNonHitLocations() {
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

    @Override
    public IRestrictedGameArea getRestrictedInstance() {
        return new RestrictedGameAreaWrapper(this);
    }

    @Override
    public IGameAreaLogger getLogger() {
        return mLogger;
    }
}
