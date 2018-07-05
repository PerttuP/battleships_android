package org.hupisoft.battleships_core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ShipTest {

    private void checkOccupiedSquares(IShip ship) {
        Coordinate bow = ship.getBowCoordinates();
        if (ship.orientation() == IShip.Orientation.VERTICAL) {
            for (int i = 0; i < ship.length(); ++i) {
                assertTrue(ship.getOccupiedCoordinates().contains(new Coordinate(bow.x(), bow.y()+i)));
            }
        } else if (ship.orientation() == IShip.Orientation.HORISONTAL) {
            for (int i = 0; i < ship.length(); ++i) {
                assertTrue(ship.getOccupiedCoordinates().contains(new Coordinate(bow.x()+i, bow.y())));
            }
        }
    }

    private void checkRestrictedSquares(IShip ship) {
        Coordinate bow = ship.getBowCoordinates();
        if (ship.orientation() == IShip.Orientation.VERTICAL) {
            for (int y = -1; y <= ship.length(); ++y) {
                for (int x = bow.x()-1; x <= bow.x()+1; ++x) {
                    assertTrue(ship.getRestrictedCoordinates().contains(new Coordinate(x, bow.y() + y)));
                }
            }
        } else if (ship.orientation() == IShip.Orientation.HORISONTAL) {
            for (int x = -1; x <= ship.length(); ++x) {
                for (int y = bow.x()-1; y <= bow.x()+1; ++y) {
                    assertTrue(ship.getRestrictedCoordinates().contains(new Coordinate(bow.x() + x, y)));
                }
            }
        }
    }

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
            assertFalse(ship.hit());
            assertEquals(i, ship.hitCount());
            assertFalse(ship.isDestroyed());
        }

        assertTrue(ship.hit());
        assertEquals(3, ship.hitCount());
        assertTrue(ship.isDestroyed());
    }

    @Test
    public void setOrientationTest() {
        Ship ship = new Ship(3);
        assertEquals(IShip.Orientation.VERTICAL, ship.orientation());
        ship.setOrientation(IShip.Orientation.HORISONTAL);
        assertEquals(IShip.Orientation.HORISONTAL, ship.orientation());
        ship.setOrientation(IShip.Orientation.HORISONTAL);
        assertEquals(IShip.Orientation.HORISONTAL, ship.orientation());
        ship.setOrientation(IShip.Orientation.VERTICAL);
        assertEquals(IShip.Orientation.VERTICAL, ship.orientation());
        ship.setOrientation(IShip.Orientation.VERTICAL);
        assertEquals(IShip.Orientation.VERTICAL, ship.orientation());
    }

    @Test
    public void setBowLocationTest() {
        Ship ship = new Ship(3);
        checkOccupiedSquares(ship);
        checkRestrictedSquares(ship);
        assertEquals(new Coordinate(0,0), ship.getBowCoordinates());
        ship.setBowCoordinates(new Coordinate(5,5));
        assertEquals(new Coordinate(5,5), ship.getBowCoordinates());
        checkOccupiedSquares(ship);
        checkRestrictedSquares(ship);
    }
}