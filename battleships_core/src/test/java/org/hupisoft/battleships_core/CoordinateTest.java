package org.hupisoft.battleships_core;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Tests the Coordinate class.
 * @see Coordinate
 */
public class CoordinateTest {

    @Test
    public void coordinateIsConstructedWithCorrectXandY() {
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                Coordinate c = new Coordinate(x,y);
                assertEquals(x, c.x());
                assertEquals(y, c.y());
            }
        }
    }

    @Test
    public void equalityTest() {
        Coordinate c1 = new Coordinate(0,0);
        Coordinate c2 = new Coordinate(0,1);
        Coordinate c3 = new Coordinate(1,0);
        Coordinate c4 = new Coordinate(0,0);
        String notCoordinate = "asd";

        assertEquals(c1, c4);
        assertEquals(c1, c1);
        assertEquals(c4, c1);
        assertNotEquals(c1, c2);
        assertNotEquals(c2,c1);
        assertNotEquals(c1, c3);
        assertNotEquals(c3, c4);

        assertNotEquals(c1, notCoordinate);
        assertNotEquals(notCoordinate, c1);
        assertNotEquals(c1, null);
        assertNotEquals(null, c1);
    }

    @Test
    public void getNeighboursTest() {
        Coordinate c = new Coordinate(0,0);

        ArrayList<Coordinate> neighbours = c.neighbours();
        assertEquals(8, neighbours.size());
        assertTrue(neighbours.contains(new Coordinate(-1,-1)));
        assertTrue(neighbours.contains(new Coordinate(-1,0)));
        assertTrue(neighbours.contains(new Coordinate(-1,1)));
        assertTrue(neighbours.contains(new Coordinate(0,-1)));
        assertTrue(neighbours.contains(new Coordinate(0,1)));
        assertTrue(neighbours.contains(new Coordinate(1,-1)));
        assertTrue(neighbours.contains(new Coordinate(1,0)));
        assertTrue(neighbours.contains(new Coordinate(1,1)));
    }

    @Test
    public void isNeigbourTest() {
        Coordinate c = new Coordinate(0,0);
        ArrayList<Coordinate> neighbours = c.neighbours();
        assertEquals(8, neighbours.size());

        for (Coordinate inList : neighbours) {
            assertTrue(c.isNeighbour(inList));
            assertTrue(inList.isNeighbour(c));
        }

        Coordinate c2 = new Coordinate(5,5);
        assertFalse(c.isNeighbour(c2));
        assertFalse(c2.isNeighbour(c));
    }

    @Test
    public void toStringTest() {
        Coordinate c = new Coordinate(3,4);
        assertEquals("Coordinate{3,4}", c.toString());
    }
}