package org.hupisoft.battleships_core;

import static org.junit.Assert.*;

/**
 * Common core test utilities.
 */
class CoreTestUtils {

    /**
     * Verify that two game areas have identical state.
     * @param expected Expected area state.
     * @param actual Actual area state.
     */
    static void compareGameAreas(IGameArea expected, IGameArea actual) {
        assertEquals(expected.width(), actual.width());
        assertEquals(expected.height(), actual.height());
        assertEquals(expected.hitCount(), actual.hitCount());
        assertEquals(expected.remainingShipCount(), actual.remainingShipCount());

        compareLoggers(expected.getLogger(), actual.getLogger());

        assertEquals(expected.getShips().size(), actual.getShips().size());
        for (int i = 0; i < expected.getShips().size(); ++i) {
            compareShips(expected.getShips().get(i), actual.getShips().get(i));
        }

        for (int x = 0; x < expected.width(); ++x) {
            for (int y = 0; y < expected.height(); ++y) {
                Coordinate c = new Coordinate(x,y);
                compareSquares(expected.getSquare(c), actual.getSquare(c));
            }
        }
    }

    /**
     * Verify that two squares have equivalent states.
     * @param expected Expected square state.
     * @param actual Actual square state.
     */
    static void compareSquares(ISquare expected, ISquare actual) {
        assertEquals(expected.getLocation(), actual.getLocation());
        assertEquals(expected.isHit(), actual.isHit());
        assertEquals(expected.getShip() != null, actual.getShip() != null);
        if (expected.getShip() != null) {
            compareShips(expected.getShip(), actual.getShip());
        }
    }

    /**
     * Verify that two ships have equivalent states.
     * @param expected Expected ship state.
     * @param actual Actual game state.
     */
    static void compareShips(IShip expected, IShip actual) {
        assertEquals(expected.getBowCoordinates(), actual.getBowCoordinates());
        assertEquals(expected.getRearCoordinates(), actual.getRearCoordinates());
        assertEquals(expected.hitCount(), actual.hitCount());
        assertEquals(expected.isDestroyed(), actual.isDestroyed());
        assertEquals(expected.length(), actual.length());
        assertEquals(expected.orientation(), actual.orientation());
    }

    /**
     * Verify that two loggers have equivalent states.
     * @param expected Expected state.
     * @param actual Actual state.
     */
    static void compareLoggers(IGameAreaLogger expected, IGameAreaLogger actual) {
        assertEquals(expected.numberOfPerformedActions(), actual.numberOfPerformedActions());
        for (int i = 0; i < expected.numberOfPerformedActions(); ++i) {
            compareLoggerActions(expected.getAction(i), actual.getAction(i));
        }
        compareLoggerActions(expected.getLatestAction(), actual.getLatestAction());
    }

    /**
     * Verify two logger actions have equivalent state.
     * @param expected Expected state.
     * @param actual Actual state.
     */
    static void compareLoggerActions(IGameAreaLogger.Action expected, IGameAreaLogger.Action actual) {
        assertEquals(expected == null, actual == null);
        if (expected != null) {
            assertEquals(expected.location(), actual.location());
            assertEquals(expected.result(), actual.result());
        }
    }
}
