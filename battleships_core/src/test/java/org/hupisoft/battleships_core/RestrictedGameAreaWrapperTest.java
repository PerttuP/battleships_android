package org.hupisoft.battleships_core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Unit tests for RestrictedGameAreaWrapper class.
 */
public class RestrictedGameAreaWrapperTest {

    private IGameArea mMockArea;
    private IRestrictedGameArea mRestrictedArea;

    @Before
    public void setUp()
    {
        mMockArea = mock(IGameArea.class);
        mRestrictedArea = new RestrictedGameAreaWrapper(mMockArea);
    }

    @Test
    public void areaDimensionsMatchActualArea()
    {
        when(mMockArea.width()).thenReturn(12);
        when(mMockArea.height()).thenReturn(8);

        assertEquals(12, mRestrictedArea.width());
        assertEquals(8, mRestrictedArea.height());

        verify(mMockArea).width();
        verify(mMockArea).height();
    }

    @Test
    public void restrictedAreaReturnsEmptyListIfNoShipsAreDestroyed()
    {
        ArrayList<IShip> allShips = new ArrayList<>();
        allShips.add(mock(IShip.class));
        allShips.add(mock(IShip.class));
        allShips.add(mock(IShip.class));
        for (IShip ship : allShips) {
            when(ship.isDestroyed()).thenReturn(false);
        }
        when(mMockArea.getShips()).thenReturn(allShips);

        List<IShip> destroyedShips = mRestrictedArea.destroyedShips();
        assertEquals(0, destroyedShips.size());

        verify(mMockArea).getShips();
        for (IShip ship : allShips) {
            verify(ship).isDestroyed();
        }
    }

    @Test
    public void restrictedAreaRevealsOnlyDestroyedShips()
    {
        IShip ship1 = mock(IShip.class);
        when(ship1.isDestroyed()).thenReturn(false);
        IShip ship2 = mock(IShip.class);
        when(ship2.isDestroyed()).thenReturn(true);
        IShip ship3 = mock(IShip.class);
        when(ship3.isDestroyed()).thenReturn(false);
        ArrayList<IShip> allShip = new ArrayList<>();
        allShip.add(ship1);
        allShip.add(ship2);
        allShip.add(ship3);
        when(mMockArea.getShips()).thenReturn(allShip);

        List<IShip> destroyedShips = mRestrictedArea.destroyedShips();
        assertEquals(1, destroyedShips.size());
        assertEquals(ship2, destroyedShips.get(0));

        verify(mMockArea).getShips();
        for (IShip ship : allShip) {
            verify(ship).isDestroyed();
        }
    }

    @Test
    public void remainingShipCountReturnsNumberOfNonDestroyedShips()
    {
        when(mMockArea.remainingShipCount()).thenReturn(2);
        assertEquals(2, mRestrictedArea.remainingShipCount());
        verify(mMockArea).remainingShipCount();
    }

    @Test
    public void remainingShipLengthsContainsOnlyNonDestroyedShipLengths()
    {
        IShip ship1 = mock(IShip.class);
        when(ship1.isDestroyed()).thenReturn(false);
        when(ship1.length()).thenReturn(2);
        IShip ship2 = mock(IShip.class);
        when(ship2.isDestroyed()).thenReturn(true);
        when(ship2.length()).thenReturn(3);
        IShip ship3 = mock(IShip.class);
        when(ship3.isDestroyed()).thenReturn(false);
        when(ship3.length()).thenReturn(5);
        ArrayList<IShip> allShip = new ArrayList<>();
        allShip.add(ship1);
        allShip.add(ship2);
        allShip.add(ship3);
        when(mMockArea.getShips()).thenReturn(allShip);

        List<Integer> remainingLengths = mRestrictedArea.remainingShipLengths();
        assertEquals(2, remainingLengths.size());
        assertTrue(remainingLengths.contains(2));
        assertTrue(remainingLengths.contains(5));

        verify(mMockArea).getShips();
        for (IShip ship : allShip) {
            verify(ship).isDestroyed();
        }
        verify(ship1).length();
        verify(ship3).length();
    }

    @Test
    public void hitCountMatchesRealAreaHitCount()
    {
        when(mMockArea.hitCount()).thenReturn(42);
        assertEquals(42, mRestrictedArea.hitCount());
        verify(mMockArea).hitCount();
    }

    @Test
    public void nonHitLocationsMatchRealAreaNonHitLocations()
    {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(1,2));
        coordinates.add(new Coordinate(2,3));
        coordinates.add(new Coordinate(3,4));
        when(mMockArea.getNonHitLocations()).thenReturn(coordinates);

        List<Coordinate> unhitCoordinates = mRestrictedArea.getNonHitLocations();
        assertEquals(coordinates, unhitCoordinates);

        verify(mMockArea).getNonHitLocations();
    }

    @Test
    public void isHitReturnsFalseIfLocationDoesNotExist()
    {
        Coordinate target = new Coordinate(-1,-1);
        when(mMockArea.getSquare(target)).thenReturn(null);
        assertFalse(mRestrictedArea.isHit(target));
        verify(mMockArea).getSquare(target);
    }

    @Test
    public void isHitReturnsFalseIfSquareIsNotHit()
    {
        Coordinate target = new Coordinate(3,4);
        ISquare mockSquare = mock(ISquare.class);
        when(mockSquare.isHit()).thenReturn(false);
        when(mMockArea.getSquare(target)).thenReturn(mockSquare);
        assertFalse(mRestrictedArea.isHit(target));
        verify(mMockArea).getSquare(target);
        verify(mockSquare).isHit();
    }

    @Test
    public void isHitReturnsTrueIfSquareIsHit()
    {
        Coordinate target = new Coordinate(3,4);
        ISquare mockSquare = mock(ISquare.class);
        when(mockSquare.isHit()).thenReturn(true);
        when(mMockArea.getSquare(target)).thenReturn(mockSquare);
        assertTrue(mRestrictedArea.isHit(target));
        verify(mMockArea).getSquare(target);
        verify(mockSquare).isHit();
    }

}