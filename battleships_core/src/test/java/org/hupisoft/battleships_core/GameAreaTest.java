package org.hupisoft.battleships_core;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the GameArea class.
 * @see GameArea
 */
public class GameAreaTest {

    private List<List<ISquare>> mSquares = null;
    private List<IShip> mShips = null;
    private GameArea mArea = null;
    private final int AREA_WIDTH = 10;
    private final int AREA_HEIGHT = 8;
    private final int SHIP_COUNT = 3;

    @Before
    public void setUp() throws Exception {
        // Create mock squares.
        mSquares = new ArrayList<>();
        for (int x = 0; x < AREA_WIDTH; ++x) {
            List<ISquare> col = new ArrayList<>();
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                col.add(mock(ISquare.class));
            }
            mSquares.add(col);
        }

        // Create mock ships
        mShips = new ArrayList<>();
        for (int i = 0; i < SHIP_COUNT; ++i) {
            mShips.add(mock(IShip.class));
        }

        mArea = new GameArea(mSquares, mShips);
    }

    @Test
    public void areaIsConstructedWithCorrectWidthHeight() {
        assertEquals(AREA_WIDTH, mArea.width());
        assertEquals(AREA_HEIGHT, mArea.height());
    }

    @Test
    public void areaIsConstructedWithCorrectShips() {
        List<IShip> actualShips = mArea.getShips();
        assertEquals(SHIP_COUNT, actualShips.size());
        for (IShip expectedShip : mShips) {
            assertTrue(actualShips.contains(expectedShip));
        }
    }

    @Test
    public void areaIsConstructedWithCorrectSquares() {
        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                Coordinate loc = new Coordinate(x, y);
                assertEquals(mSquares.get(x).get(y), mArea.getSquare(loc));
            }
        }
    }

    @Test
    public void getZeroHitCount() {
        // 0 hits
        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                ISquare sqr = mSquares.get(x).get(y);
                when(sqr.isHit()).thenReturn(false);
            }
        }

        assertEquals(0, mArea.hitCount());

        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                ISquare sqr = mSquares.get(x).get(y);
                verify(sqr).isHit();
                verifyNoMoreInteractions(sqr);
            }
        }
    }

    @Test
    public void getOneHitCount() {
        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                ISquare sqr = mSquares.get(x).get(y);
                when(sqr.isHit()).thenReturn(x == 5 && y == 6);
            }
        }

        assertEquals(1, mArea.hitCount());

        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                ISquare sqr = mSquares.get(x).get(y);
                verify(sqr).isHit();
                verifyNoMoreInteractions(sqr);
            }
        }
    }

    @Test
    public void getAllHitCount() {
        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                ISquare sqr = mSquares.get(x).get(y);
                when(sqr.isHit()).thenReturn(true);
            }
        }

        assertEquals(AREA_WIDTH * AREA_HEIGHT, mArea.hitCount());

        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                ISquare sqr = mSquares.get(x).get(y);
                verify(sqr).isHit();
                verifyNoMoreInteractions(sqr);
            }
        }
    }

    @Test
    public void allShipsRemaining()
    {
        for (int i = 0; i < SHIP_COUNT; ++i) {
            IShip ship = mShips.get(i);
            when(ship.isDestroyed()).thenReturn(false);
        }

        assertEquals(SHIP_COUNT, mArea.remainingShipCount());

        for (int i = 0; i < SHIP_COUNT; ++i) {
            IShip ship = mShips.get(i);
            verify(ship).isDestroyed();
            verifyNoMoreInteractions(ship);
        }
    }

    @Test
    public void noShipsRemaining()
    {
        for (int i = 0; i < SHIP_COUNT; ++i) {
            IShip ship = mShips.get(i);
            when(ship.isDestroyed()).thenReturn(true);
        }

        assertEquals(0, mArea.remainingShipCount());

        for (int i = 0; i < SHIP_COUNT; ++i) {
            IShip ship = mShips.get(i);
            verify(ship).isDestroyed();
            verifyNoMoreInteractions(ship);
        }
    }

    @Test
    public void getLocationReturnsNullOutsideGameArea() {
        assertNull(mArea.getSquare(new Coordinate(-1,-1)));
        assertNull(mArea.getSquare(new Coordinate(-1, 0)));
        assertNull(mArea.getSquare(new Coordinate(0, -10)));
        assertNull(mArea.getSquare(new Coordinate(AREA_WIDTH, 0)));
        assertNull(mArea.getSquare(new Coordinate(0, AREA_HEIGHT)));
        assertNull(mArea.getSquare(new Coordinate(AREA_WIDTH, AREA_HEIGHT)));
    }

    @Test
    public void hitEmptySquare()
    {
        ISquare mockSqr = mSquares.get(2).get(3);
        when(mockSqr.hit()).thenReturn(HitResult.EMPTY);
        assertEquals(HitResult.EMPTY, mArea.hit(new Coordinate(2,3)));
        verify(mockSqr).hit();
        verifyNoMoreInteractions(mockSqr);
    }

    @Test
    public void hitShipWithoutDestroyingIt()
    {
        ISquare mockSqr = mSquares.get(2).get(3);
        when(mockSqr.hit()).thenReturn(HitResult.SHIP_HIT);
        assertEquals(HitResult.SHIP_HIT, mArea.hit(new Coordinate(2,3)));
        verify(mockSqr).hit();
        verifyNoMoreInteractions(mockSqr);
    }

    @Test
    public void hitShipAndDestroyIt()
    {
        ISquare mockSqr = mSquares.get(2).get(3);
        when(mockSqr.hit()).thenReturn(HitResult.SHIP_DESTROYED);
        assertEquals(HitResult.SHIP_DESTROYED, mArea.hit(new Coordinate(2,3)));
        verify(mockSqr).hit();
        verifyNoMoreInteractions(mockSqr);
    }

    @Test
    public void hitSquareThatIsAlreadyHit()
    {
        ISquare mockSqr = mSquares.get(2).get(3);
        when(mockSqr.hit()).thenReturn(HitResult.ALREADY_HIT);
        assertEquals(HitResult.ALREADY_HIT, mArea.hit(new Coordinate(2,3)));
        verify(mockSqr).hit();
        verifyNoMoreInteractions(mockSqr);
    }

    @Test
    public void hitSquareOutOfBounds() {
        assertNull(mArea.hit(new Coordinate(-1, -1)));
        assertNull(mArea.hit(new Coordinate(1, -1)));
        assertNull(mArea.hit(new Coordinate(-1, 1)));
        assertNull(mArea.hit(new Coordinate(AREA_WIDTH, 1)));
        assertNull(mArea.hit(new Coordinate(1, AREA_HEIGHT)));
        assertNull(mArea.hit(new Coordinate(AREA_WIDTH, AREA_HEIGHT)));
    }

    @Test
    public void getUnhitLocationsReturnAllWhenNoneHasBeenHit() {
        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                ISquare sqr = mSquares.get(x).get(y);
                when(sqr.isHit()).thenReturn(false);
            }
        }

        List<Coordinate> unhitLocations = mArea.getNonHitLocations();
        assertEquals(AREA_WIDTH*AREA_HEIGHT, unhitLocations.size());

        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                assertTrue(unhitLocations.contains(new Coordinate(x,y)));
                ISquare sqr = mSquares.get(x).get(y);
                verify(sqr).isHit();
                verifyNoMoreInteractions(sqr);
            }
        }
    }

    @Test
    public void getUnhitLocationsReturnAllButOneWhenOneIsHit() {
        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                ISquare sqr = mSquares.get(x).get(y);
                when(sqr.isHit()).thenReturn(x == 2 && y == 3);
            }
        }

        List<Coordinate> unhitLocations = mArea.getNonHitLocations();
        assertEquals(AREA_WIDTH*AREA_HEIGHT-1, unhitLocations.size());

        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                assertEquals((x != 2 ||y != 3), unhitLocations.contains(new Coordinate(x,y)));
                ISquare sqr = mSquares.get(x).get(y);
                verify(sqr).isHit();
                verifyNoMoreInteractions(sqr);
            }
        }
    }

    @Test
    public void getUnhitLocationsReturnNoneWhenAllAreHit() {
        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                ISquare sqr = mSquares.get(x).get(y);
                when(sqr.isHit()).thenReturn(true);
            }
        }

        List<Coordinate> unhitLocations = mArea.getNonHitLocations();
        assertEquals(0, unhitLocations.size());

        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                ISquare sqr = mSquares.get(x).get(y);
                verify(sqr).isHit();
                verifyNoMoreInteractions(sqr);
            }
        }
    }

    @Test
    public void getRestrictedInstanceTest() {
        IRestrictedGameArea restricted = mArea.getRestrictedInstance();
        assertEquals(mArea.width(), restricted.width());
        assertEquals(mArea.height(), restricted.height());
        assertEquals(mArea.hitCount(), restricted.hitCount());
        assertEquals(mArea.getNonHitLocations(), restricted.getNonHitLocations());
        assertEquals(3, restricted.remainingShipCount());
        assertEquals(0, restricted.destroyedShips().size());
        assertEquals(3, restricted.remainingShipLengths().size());
    }
}