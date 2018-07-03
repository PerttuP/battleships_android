package org.hupisoft.battleships_core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ShipTest {

    @Test
    public void firstConstructorTest() {
        for (int length = 1; length <= 4; ++length) {
            Ship ship = new Ship(length);
            assertEquals(IShip.Orientation.VERTICAL, ship.orientation());
            assertEquals(new Coordinate(0,0), ship.getBowCoordinates());
            assertEquals(new Coordinate(0, length-1), ship.getRearCoordinates());
            assertEquals(length, ship.length());
            assertEquals(0, ship.hitCount());
            assertFalse(ship.isDestroyed());

            // Check occupied coordinates.
            assertEquals(length, ship.getOccupiedCoordinates().size());
            for (int i = 0; i < length; ++i) {
                assertEquals(new Coordinate(0, i), ship.getOccupiedCoordinates().get(i));
            }

            // Check restricted coordinates.
            assertEquals(3*(length+2), ship.getRestrictedCoordinates().size());
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= length; ++y) {
                    Coordinate c = new Coordinate(x, y);
                    assertTrue(ship.getRestrictedCoordinates().contains(c));
                }
            }
        }
    }

    @Test
    public void secondConstruction_createVerticalLongShip() {
        Ship ship = new Ship(5, new Coordinate(2,3), IShip.Orientation.HORISONTAL);
        assertEquals(5, ship.length());
        assertEquals(IShip.Orientation.HORISONTAL, ship.orientation());
        assertEquals(0, ship.hitCount());
        assertFalse(ship.isDestroyed());
        assertEquals(new Coordinate(2,3), ship.getBowCoordinates());
        assertEquals(new Coordinate(6, 3), ship.getRearCoordinates());

        // Check occupied coordinates.
        assertEquals(5, ship.getOccupiedCoordinates().size());
        for (int i = 0; i < 5; ++i) {
            assertEquals(new Coordinate(2+i, 3), ship.getOccupiedCoordinates().get(i));
        }

        // Check restricted coordinates.
        assertEquals(3*7, ship.getRestrictedCoordinates().size());
        for (int x = 1; x <= 7; ++x) {
            for (int y = 2; y <= 4; ++y) {
                Coordinate c = new Coordinate(x, y);
                assertTrue(ship.getRestrictedCoordinates().contains(c));
            }
        }
    }

    @Test
    public void hitTest() {
        Ship ship = new Ship(3, new Coordinate(5,4), IShip.Orientation.VERTICAL);

        assertEquals(0, ship.hitCount());
        assertFalse(ship.isDestroyed());

        for (int i = 1; i < 3; ++i) {
            ship.hit();
            assertEquals(i, ship.hitCount());
            assertFalse(ship.isDestroyed());
        }

        ship.hit();
        assertEquals(3, ship.hitCount());
        assertTrue(ship.isDestroyed());
    }
}