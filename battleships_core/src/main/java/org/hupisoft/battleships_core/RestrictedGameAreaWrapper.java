package org.hupisoft.battleships_core;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper to restrict access to game area.
 */
class RestrictedGameAreaWrapper implements IRestrictedGameArea {

    private IGameArea mArea;

    /**
     * Constructor.
     * @param area Actual game area.
     */
    RestrictedGameAreaWrapper(IGameArea area) {
        mArea = area;
    }

    @Override
    public int width() {
        return mArea.width();
    }

    @Override
    public int height() {
        return mArea.height();
    }

    @Override
    public List<IShip> destroyedShips() {
        List<IShip> allShips = mArea.getShips();
        ArrayList<IShip> destroyedShips = new ArrayList<>();
        for (IShip ship : allShips) {
            if (ship.isDestroyed()) {
                destroyedShips.add(ship);
            }
        }
        return destroyedShips;
    }

    @Override
    public int remainingShipCount() {
        return mArea.remainingShipCount();
    }

    @Override
    public List<Integer> remainingShipLengths() {
        ArrayList<Integer> lengths = new ArrayList<>();
        List<IShip> allShip = mArea.getShips();
        for (IShip ship : allShip) {
            if (!ship.isDestroyed()) {
                lengths.add(ship.length());
            }
        }
        return lengths;
    }

    @Override
    public int hitCount() {
        return mArea.hitCount();
    }

    @Override
    public List<Coordinate> getNonHitLocations() {
        return mArea.getNonHitLocations();
    }
}
