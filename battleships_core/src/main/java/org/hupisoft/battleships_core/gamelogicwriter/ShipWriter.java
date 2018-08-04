package org.hupisoft.battleships_core.gamelogicwriter;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IShip;

/**
 * Create string representation for ship.
 */
class ShipWriter {

    private static final String TEMPLATE = "Ship{length:%d, bow:%s, orientation:%s}";

    /**
     * Create string representation for ship.
     * @param ship Ship.
     * @return String representation.
     */
    public String shipToString(IShip ship) {
        int length = ship.length();
        Coordinate bow = ship.getBowCoordinates();
        IShip.Orientation orientation = ship.orientation();
        String coordinateStr = new CoordinateWriter().coordinateToString(bow);
        return String.format(TEMPLATE, length, coordinateStr, orientation);
    }
}
