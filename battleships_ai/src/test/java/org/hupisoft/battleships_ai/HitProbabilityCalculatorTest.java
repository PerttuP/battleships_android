package org.hupisoft.battleships_ai;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IRestrictedGameArea;
import org.hupisoft.battleships_core.IShip;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HitProbabilityCalculatorTest {

    private static final int AREA_WIDTH = 12;
    private static final int AREA_HEIGHT = 8;
    private IRestrictedGameArea mMockArea;
    private HitProbabilityCalculator mCalculator;

    @Before
    public void setUp()
    {
        mCalculator = new HitProbabilityCalculator();
        mMockArea = mock(IRestrictedGameArea.class);
        when(mMockArea.width()).thenReturn(AREA_WIDTH);
        when(mMockArea.height()).thenReturn(AREA_HEIGHT);
    }

    private void expectHit(Coordinate c)
    {
        when(mMockArea.isHit(c)).thenReturn(true);
    }

    private void expectNotHit(Coordinate c)
    {
        when(mMockArea.isHit(c)).thenReturn(false);
    }

    private void setHitExpectations(Coordinate[] hit, Coordinate[] notHit)
    {
        for (Coordinate c : hit){
            expectHit(c);
        }
        for (Coordinate c : notHit)
        {
            expectNotHit(c);
        }
    }

    private void setShipLengths(int[] lengths)
    {
        List<Integer> ships = new ArrayList<>();
        for (int l : lengths) {
            ships.add(l);
        }
        when(mMockArea.remainingShipLengths()).thenReturn(ships);
    }

    void setRestrictedSquares(Coordinate[] restricted)
    {
        IShip destroyed = mock(IShip.class);
        List<Coordinate> squares = new ArrayList<>();
        for (Coordinate c : restricted) {
            squares.add(c);
        }
        when(destroyed.getRestrictedCoordinates()).thenReturn(squares);
        List<IShip> destroyedShips = new ArrayList<>();
        destroyedShips.add(destroyed);
        when(mMockArea.destroyedShips()).thenReturn(destroyedShips);
    }

    @Test
    public void probabilityForOutOfBoundsCoordinatesIsZero()
    {
        int expectedWidthCallsCount = 0;
        int expectedHeightCallsCount = 0;

        for (int x = -1; x <= AREA_WIDTH; ++x) {
            assertEquals(0, mCalculator.getProbabilityFactor(mMockArea, new Coordinate(x, -1)));
            assertEquals(0, mCalculator.getProbabilityFactor(mMockArea, new Coordinate(x, AREA_HEIGHT)));
            expectedWidthCallsCount += 2;
        }
        expectedHeightCallsCount += AREA_WIDTH;

        for (int y = -1; y <= AREA_HEIGHT; ++y) {
            assertEquals(0, mCalculator.getProbabilityFactor(mMockArea, new Coordinate(-1, y)));
            assertEquals(0, mCalculator.getProbabilityFactor(mMockArea, new Coordinate(AREA_WIDTH, y)));
        }
        expectedWidthCallsCount += AREA_HEIGHT;

        verify(mMockArea, times(expectedWidthCallsCount)).width();
        verify(mMockArea, times(expectedHeightCallsCount)).height();
    }

    @Test
    public void probabilityIsZeroForAlreadyHitLocations()
    {
        Coordinate target = new Coordinate(3,4);
        when(mMockArea.isHit(target)).thenReturn(true);
        assertEquals(0, mCalculator.getProbabilityFactor(mMockArea, target));
        verify(mMockArea).isHit(target);
    }

    @Test
    public void probabilityIsZeroIfNoEnoughSpace()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,4);
        Coordinate[] hit = new Coordinate[]{
                new Coordinate(2,4), new Coordinate(3,3), new Coordinate(5,4), new Coordinate(3,6)
        };
        Coordinate[] notHit = new Coordinate[]{
                target, new Coordinate(4,4), new Coordinate(3,5)
        };
        setHitExpectations(hit, notHit);
        setRestrictedSquares(new Coordinate[]{});

        assertEquals(0, mCalculator.getProbabilityFactor(mMockArea, target));
    }

    @Test
    public void probabilityIsZeroIfLocationIsRestrictedByDestroyedShip()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,4);
        setRestrictedSquares(new Coordinate[]{target});

        assertEquals(0, mCalculator.getProbabilityFactor(mMockArea, target));
    }

    @Test
    public void factorIs1IfOnlyOneShipFitsOneWayLeft()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,4);
        Coordinate[] hit = new Coordinate[]{
                new Coordinate(0,4), new Coordinate(4,4), new Coordinate(3,3), new Coordinate(3,5)
        };
        Coordinate[] notHit = new Coordinate[] {
                target, new Coordinate(2,4), new Coordinate(1,4)
        };
        setHitExpectations(hit, notHit);
        setRestrictedSquares(new Coordinate[]{});

        assertEquals(1, mCalculator.getProbabilityFactor(mMockArea, target));
    }

    @Test
    public void factorIs1IfOnlyOneShipFitsOneWayRight()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,4);
        Coordinate[] hit = new Coordinate[] {
                new Coordinate(2,4), new Coordinate(3,3), new Coordinate(3,5), new Coordinate(6,4)
        };
        Coordinate[] notHit = new Coordinate[]{
                target, new Coordinate(4,4), new Coordinate(5,4)
        };
        setHitExpectations(hit, notHit);
        setRestrictedSquares(new Coordinate[]{});

        assertEquals(1, mCalculator.getProbabilityFactor(mMockArea, target));
    }

    @Test
    public void factorIs1IfOnlyOneShipFitsOneWayHorizontally()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,4);
        Coordinate[] hit = new Coordinate[] {
                new Coordinate(1,4), new Coordinate(3,3), new Coordinate(3,5), new Coordinate(5,4)
        };
        Coordinate[] notHit = new Coordinate[]{
                target, new Coordinate(2,4), new Coordinate(4,4)
        };
        setHitExpectations(hit, notHit);
        setRestrictedSquares(new Coordinate[]{});

        assertEquals(1, mCalculator.getProbabilityFactor(mMockArea, target));
    }

    @Test
    public void factorsAreCombinedIfMultipleShipsFitHorizontally()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,4);
        Coordinate[] hit = new Coordinate[] {
                new Coordinate(0,4), new Coordinate(3,3), new Coordinate(3,5), new Coordinate(6,4)
        };
        Coordinate[] notHit = new Coordinate[]{
                target, new Coordinate(2,4), new Coordinate(1,4), new Coordinate(4,4), new Coordinate(5,4)
        };
        setHitExpectations(hit, notHit);
        setRestrictedSquares(new Coordinate[]{});

        assertEquals(4, mCalculator.getProbabilityFactor(mMockArea, target));
    }

    @Test
    public void factorIs1IfOnlyOneShipFitsUp()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,4);
        Coordinate[] hit = new Coordinate[] {
                new Coordinate(2,4), new Coordinate(4,4), new Coordinate(3,5), new Coordinate(3,1)
        };
        Coordinate[] notHit = new Coordinate[]{
                target, new Coordinate(3,3), new Coordinate(3,2)
        };
        setHitExpectations(hit, notHit);
        setRestrictedSquares(new Coordinate[]{});

        assertEquals(1, mCalculator.getProbabilityFactor(mMockArea, target));
    }

    @Test
    public void factorIs1IfOnlyOneShipFitsDown()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,4);
        Coordinate[] hit = new Coordinate[] {
                new Coordinate(2,4), new Coordinate(4,4), new Coordinate(3,3), new Coordinate(3,7)
        };
        Coordinate[] notHit = new Coordinate[]{
                target, new Coordinate(3,5), new Coordinate(3,6)
        };
        setHitExpectations(hit, notHit);
        setRestrictedSquares(new Coordinate[]{});

        assertEquals(1, mCalculator.getProbabilityFactor(mMockArea, target));
    }

    @Test
    public void factorIs1IfOnlyOneShipFitsVertically()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,4);
        Coordinate[] hit = new Coordinate[] {
                new Coordinate(2,4), new Coordinate(4,4), new Coordinate(3,2), new Coordinate(3,6)
        };
        Coordinate[] notHit = new Coordinate[]{
                target, new Coordinate(3,5), new Coordinate(3,3)
        };
        setHitExpectations(hit, notHit);
        setRestrictedSquares(new Coordinate[]{});

        assertEquals(1, mCalculator.getProbabilityFactor(mMockArea, target));
    }

    @Test
    public void factorsAreCombinedIfMultipleShipsFitVertically()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,3);
        Coordinate[] hit = new Coordinate[] {
                new Coordinate(2,3), new Coordinate(4,3), new Coordinate(3,1), new Coordinate(3,7)
        };
        Coordinate[] notHit = new Coordinate[]{
                target, new Coordinate(3,5), new Coordinate(3,4), new Coordinate(3,6), new Coordinate(3,2)
        };
        setHitExpectations(hit, notHit);
        setRestrictedSquares(new Coordinate[]{});

        assertEquals(3, mCalculator.getProbabilityFactor(mMockArea, target));
    }

    @Test
    public void factorsAreCombinedIfMultipleShipsFitInBothDirections()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,3);
        Coordinate[] hit = new Coordinate[] {};
        Coordinate[] notHit = new Coordinate[]{
                target, new Coordinate(3,2), new Coordinate(3,1), new Coordinate(3,0),
                new Coordinate(3,4), new Coordinate(3,5), new Coordinate(3,6), new Coordinate(3,7),
                new Coordinate(2,3), new Coordinate(1,3), new Coordinate(0,3),
                new Coordinate(4,3), new Coordinate(5,3), new Coordinate(6,3), new Coordinate(7,3)
        };
        setHitExpectations(hit, notHit);
        setRestrictedSquares(new Coordinate[]{});

        assertEquals(14, mCalculator.getProbabilityFactor(mMockArea, target));
    }

    @Test
    public void destroyedShipsLimitPossibleShipLocations()
    {
        setShipLengths(new int[]{3,5});
        Coordinate target = new Coordinate(3,3);
        Coordinate[] restricted = new Coordinate[]{new Coordinate(0,3), new Coordinate(3,0)};
        Coordinate[] hit = new Coordinate[] {};
        Coordinate[] notHit = new Coordinate[]{
                target, new Coordinate(3,2), new Coordinate(3,1), new Coordinate(3,0),
                new Coordinate(3,4), new Coordinate(3,5), new Coordinate(3,6), new Coordinate(3,7),
                new Coordinate(2,3), new Coordinate(1,3), new Coordinate(0,3),
                new Coordinate(4,3), new Coordinate(5,3), new Coordinate(6,3), new Coordinate(7,3)
        };
        setHitExpectations(hit, notHit);
        setRestrictedSquares(restricted);

        assertEquals(12, mCalculator.getProbabilityFactor(mMockArea, target));
    }
}