package org.hupisoft.battleships_core.gamelogicwriter;

import org.hupisoft.battleships_core.Coordinate;

/**
 * Creates string representation for coordinate objects.
 */
class CoordinateWriter {

    private static final String TEMPLATE = "Coordinate{%d,%d}";

    /**
     * Create string representation for coordinate.
     * @param coordinate Coordinate.
     * @return String representation.
     */
    public String coordinateToString(Coordinate coordinate) {
        return String.format(TEMPLATE, coordinate.x(), coordinate.y());
    }
}
