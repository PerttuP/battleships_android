package org.hupisoft.battleships_core.gamelogicwriter;

import org.hupisoft.battleships_core.Coordinate;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the CoordinateWriter class.
 */
public class CoordinateWriterTest {

    @Test
    public void writeCorrectCoordinateString() {
        Coordinate c1 = new Coordinate(0,0);
        Coordinate c2 = new Coordinate(2,3);
        Coordinate c3 = new Coordinate(-5, -7);
        CoordinateWriter writer = new CoordinateWriter();

        String s1 = writer.coordinateToString(c1);
        String s2 = writer.coordinateToString(c2);
        String s3 = writer.coordinateToString(c3);

        assertNotNull(s1);
        assertNotNull(s2);
        assertNotNull(s3);

        String expected1 = "Coordinate{0,0}";
        String expected2 = "Coordinate{2,3}";
        String expected3 = "Coordinate{-5,-7}";

        assertEquals(expected1, s1);
        assertEquals(expected2, s2);
        assertEquals(expected3, s3);
    }
}