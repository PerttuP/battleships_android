package org.hupisoft.battleships_core;
import static org.mockito.Mockito.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class SquareTest {

    @Test
    public void constructorTest() {
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                Coordinate c = new Coordinate(x,y);
                Square sqr = new Square(c);
                assertEquals(c, sqr.getLocation());
                assertFalse(sqr.isHit());
                assertNull(sqr.getShip());
            }
        }
    }

    @Test
    public void hitEmptySquare() {
        Square sqr = new Square(new Coordinate(1,2));
        assertFalse(sqr.isHit());
        assertNull(sqr.getShip());
        assertEquals(ISquare.HitResult.EMPTY, sqr.hit());
        assertTrue(sqr.isHit());
        assertNull(sqr.getShip());
    }

    @Test
    public void hitOccupiedSquare() {
        IShip mockShip = mock(IShip.class);
        when(mockShip.hit()).thenReturn(false);

        Square sqr = new Square(new Coordinate(4,5));
        sqr.setShip(mockShip);
        assertTrue(sqr.getShip() == mockShip);
        assertFalse(sqr.isHit());

        assertEquals(ISquare.HitResult.HIT_SHIP, sqr.hit());
        verify(mockShip).hit();
        assertTrue(sqr.isHit());
    }

    @Test
    public void hitAndDestroyShip() {
        IShip mockShip = mock(IShip.class);
        when(mockShip.hit()).thenReturn(true);

        Square sqr = new Square(new Coordinate(5,6));
        sqr.setShip(mockShip);
        assertTrue(sqr.getShip() == mockShip);
        assertFalse(sqr.isHit());

        assertEquals(ISquare.HitResult.DESTROYED_SHIP, sqr.hit());
        assertTrue(sqr.isHit());
        verify(mockShip).hit();
    }
}