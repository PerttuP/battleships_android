package org.hupisoft.battleships_core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Tests the GameAreaBuilder class.
 * @see GameAreaBuilder
 */
public class GameAreaBuilderTest {

    private GameAreaBuilder mBuilder = null;
    private Random mRng = null;

    private void setDefaultRandomness() {
        when(mRng.nextInt(anyInt())).thenAnswer(new Answer<Integer>() {
            Random rng = new Random();
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return rng.nextInt((Integer) invocation.getArguments()[0]);
            }
        });
    }

    private void checkShipsDoNotOverlap(IGameArea area) {
        List<IShip> ships = area.getShips();
        List<Coordinate> restricted = ships.get(0).getRestrictedCoordinates();
        for (int i = 1; i < ships.size(); ++i) {
            List<Coordinate> occupied = ships.get(i).getOccupiedCoordinates();
            for (Coordinate c : occupied) {
                assertFalse(restricted.contains(c));
            }
            restricted.addAll(ships.get(i).getRestrictedCoordinates());
        }
    }

    private void checkShipsOccupyCorrectSquares(IGameArea area) {
        List<IShip> ships = area.getShips();
        for (IShip ship : ships) {
            List<Coordinate> occupied = ship.getOccupiedCoordinates();
            assertEquals(ship.length(), occupied.size());
            for (Coordinate c : occupied) {
                assertEquals(ship, area.getSquare(c).getShip());
            }
        }
    }

    private void checkAreasNotSameButEquivalent(IGameArea expected, IGameArea actual) {
        CoreTestUtils.compareGameAreas(expected, actual);

        // Verify that members are deep copies too.
        for (int i = 0; i < expected.getShips().size(); ++i) {
            assertNotSame(expected.getShips().get(i), actual.getShips().get(i));
        }
        for (int x = 0; x < expected.width(); ++x) {
            for (int y = 0; y < expected.height(); ++y) {
                Coordinate c = new Coordinate(x,y);
                assertNotSame(expected.getSquare(c), actual.getSquare(c));
            }
        }
        assertNotSame(expected.getLogger(), actual.getLogger());
    }

    @Before
    public void setUp() {
        mRng = mock(Random.class);
        mBuilder = new GameAreaBuilder(mRng);
    }

    @Test
    public void gameAreaHasCorrectWithAndHeight() {
        setDefaultRandomness();
        IGameArea area = mBuilder.createInitialGameArea(10,8, new int[]{2,3,5});
        assertNotNull(area);
        assertEquals(10, area.width());
        assertEquals(8, area.height());
        checkShipsDoNotOverlap(area);
        checkShipsOccupyCorrectSquares(area);
    }

    @Test
    public void gameAreaHasShipsWithCorrectLengths() {
        setDefaultRandomness();
        IGameArea area = mBuilder.createInitialGameArea(10,8, new int[]{2,3,5});
        assertNotNull(area);
        List<IShip> ships = area.getShips();
        assertEquals(3, ships.size());
        assertEquals(2, ships.get(0).length());
        assertEquals(3, ships.get(1).length());
        assertEquals(5, ships.get(2).length());
        checkShipsDoNotOverlap(area);
        checkShipsOccupyCorrectSquares(area);
    }

    @Test
    public void initialGameAreaHasCorrectNonHitSquares() {
        setDefaultRandomness();
        IGameArea area = mBuilder.createInitialGameArea(10,8, new int[]{2});
        assertNotNull(area);

        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 8; ++y) {
                Coordinate c = new Coordinate(x,y);
                ISquare sqr = area.getSquare(c);
                assertEquals(c, sqr.getLocation());
                assertFalse(sqr.isHit());
            }
        }

        checkShipsDoNotOverlap(area);
        checkShipsOccupyCorrectSquares(area);
    }

    @Test
    public void horizontalShipIsPlacedRandomly() {
        when(mRng.nextBoolean()).thenReturn(true);
        when(mRng.nextInt(6)).thenReturn(3);
        when(mRng.nextInt(8)).thenReturn(5);

        IGameArea area = mBuilder.createInitialGameArea(10, 8, new int[]{5});
        assertNotNull(area);
        assertEquals(1, area.getShips().size());
        IShip ship = area.getShips().get(0);
        assertEquals(IShip.Orientation.HORIZONTAL, ship.orientation());
        assertEquals(new Coordinate(3,5), ship.getBowCoordinates());
        checkShipsDoNotOverlap(area);
        checkShipsOccupyCorrectSquares(area);

        verify(mRng).nextBoolean();
        verify(mRng).nextInt(6);
        verify(mRng).nextInt(8);
        verifyNoMoreInteractions(mRng);
    }

    @Test
    public void verticalShipIsPlacedRandomly() {
        when(mRng.nextBoolean()).thenReturn(false);
        when(mRng.nextInt(10)).thenReturn(9);
        when(mRng.nextInt(4)).thenReturn(2);

        IGameArea area = mBuilder.createInitialGameArea(10, 8, new int[]{5});
        assertNotNull(area);
        assertEquals(1, area.getShips().size());
        IShip ship = area.getShips().get(0);
        assertEquals(IShip.Orientation.VERTICAL, ship.orientation());
        assertEquals(new Coordinate(9,2), ship.getBowCoordinates());
        checkShipsDoNotOverlap(area);
        checkShipsOccupyCorrectSquares(area);

        verify(mRng).nextBoolean();
        verify(mRng).nextInt(10);
        verify(mRng).nextInt(4);
        verifyNoMoreInteractions(mRng);
    }

    @Test
    public void shipIsPlacedAgainIfOverlapOccurs() {
        when(mRng.nextBoolean()).thenAnswer(new Answer<Boolean>() {
            boolean[] results = {true, false, true};
            int i = 0;
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return results[i++];
            }
        });

        when(mRng.nextInt(6)).thenAnswer(new Answer<Integer>() {
            int[] results = {3,4};
            int i = 0;
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return results[i++];
            }
        });
        when(mRng.nextInt(8)).thenAnswer(new Answer<Integer>() {
            int[] results = {5,1,3};
            int i = 0;
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return results[i++];
            }
        });
        when(mRng.nextInt(10)).thenReturn(2);

        IGameArea area = mBuilder.createInitialGameArea(10, 8, new int[]{5, 3});
        assertNotNull(area);
        assertEquals(2, area.getShips().size());
        IShip ship1 = area.getShips().get(0);
        assertEquals(IShip.Orientation.HORIZONTAL, ship1.orientation());
        assertEquals(new Coordinate(3,5), ship1.getBowCoordinates());
        IShip ship2 = area.getShips().get(1);
        assertEquals(IShip.Orientation.HORIZONTAL, ship2.orientation());
        assertEquals(new Coordinate(1,3), ship2.getBowCoordinates());
        checkShipsDoNotOverlap(area);
        checkShipsOccupyCorrectSquares(area);

        InOrder order = inOrder(mRng);
        order.verify(mRng).nextBoolean();
        order.verify(mRng).nextInt(6);
        order.verify(mRng).nextInt(8);
        order.verify(mRng).nextBoolean();
        order.verify(mRng).nextInt(10);
        order.verify(mRng).nextInt(6);
        order.verify(mRng).nextBoolean();
        order.verify(mRng, times(2)).nextInt(8);
    }

    @Test
    public void copyIsNullIfOriginalIsNull() {
        assertNull(mBuilder.createCopy(null));
    }

    @Test
    public void copyMatchesTheOriginal() {
        GameAreaBuilder builder = new GameAreaBuilder();
        IGameArea original = builder.createInitialGameArea(12, 8, new int[]{2,3,5});

        // Perform some actions:
        original.hit(new Coordinate(2,3));
        original.hit(new Coordinate(11,7));
        original.hit(new Coordinate(5,6));
        original.hit(new Coordinate(0,0));
        original.hit(new Coordinate(0,7));
        original.hit(new Coordinate(11,0));

        // Create copy
        IGameArea copy = builder.createCopy(original);
        checkAreasNotSameButEquivalent(original, copy);
    }

    @Test
    public void snapshotIsEmptyIfHitsOutOfRange() {
        GameAreaBuilder builder = new GameAreaBuilder();
        IGameArea original = builder.createInitialGameArea(12, 8, new int[]{2,3,5});

        // Perform some actions:
        original.hit(new Coordinate(2,3));
        original.hit(new Coordinate(11,7));
        original.hit(new Coordinate(5,6));
        original.hit(new Coordinate(0,0));
        original.hit(new Coordinate(0,7));
        original.hit(new Coordinate(11,0));

        assertNull(builder.createSnapshot(original, -1));
        assertNull(builder.createSnapshot(original, 7));
    }

    @Test
    public void createsCorrectSnapshots() {
        GameAreaBuilder builder = new GameAreaBuilder();
        IGameArea original = builder.createInitialGameArea(12, 8, new int[]{2,3,5});
        List<IGameArea> expectedSnapshots = new ArrayList<>();

        // Perform some actions:
        expectedSnapshots.add(builder.createCopy(original));
        original.hit(new Coordinate(2,3));
        expectedSnapshots.add(builder.createCopy(original));
        original.hit(new Coordinate(11,7));
        expectedSnapshots.add(builder.createCopy(original));
        original.hit(new Coordinate(5,6));
        expectedSnapshots.add(builder.createCopy(original));
        original.hit(new Coordinate(0,0));
        expectedSnapshots.add(builder.createCopy(original));
        original.hit(new Coordinate(0,7));
        expectedSnapshots.add(builder.createCopy(original));
        original.hit(new Coordinate(11,0));
        expectedSnapshots.add(builder.createCopy(original));

        for (int i = 0; i <= original.getLogger().numberOfPerformedActions(); ++i) {
            IGameArea snapshot = builder.createSnapshot(original, i);
            checkAreasNotSameButEquivalent(expectedSnapshots.get(i), snapshot);
        }

    }
}