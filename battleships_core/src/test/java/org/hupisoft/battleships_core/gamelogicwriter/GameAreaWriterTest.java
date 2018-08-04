package org.hupisoft.battleships_core.gamelogicwriter;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.IShip;
import org.hupisoft.battleships_core.ISquare;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test the GameAreaWriter class.
 */
public class GameAreaWriterTest {

    private static final String AREA_STRING_TEMPLATE = "GameArea{width:%d, height:%d, ships:[%s], hits:[%s]}";
    private static final int AREA_WIDTH = 12;
    private static final int AREA_HEIGHT = 8;

    private ArrayList<IShip> mShips = null;
    private ArrayList<ArrayList<ISquare>> mSquares = null;

    private IGameArea setUpGameArea(ArrayList<Coordinate> hitLocations) {
        mShips = new ArrayList<>();

        IShip ship1 = mock(IShip.class);
        when(ship1.length()).thenReturn(5);
        when(ship1.getBowCoordinates()).thenReturn(new Coordinate(0,0));
        when(ship1.orientation()).thenReturn(IShip.Orientation.HORIZONTAL);
        mShips.add(ship1);

        IShip ship2 = mock(IShip.class);
        when(ship2.length()).thenReturn(3);
        when(ship2.getBowCoordinates()).thenReturn(new Coordinate(2,3));
        when(ship2.orientation()).thenReturn(IShip.Orientation.VERTICAL);
        mShips.add(ship2);

        mSquares = new ArrayList<>();
        for (int x = 0; x < AREA_WIDTH; ++x) {
            ArrayList<ISquare> col = new ArrayList<>();
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                Coordinate c = new Coordinate(x, y);
                ISquare sqr = mock(ISquare.class);
                when(sqr.isHit()).thenReturn(hitLocations.contains(c));
                col.add(sqr);
            }
            mSquares.add(col);
        }

        IGameArea area = mock(IGameArea.class);
        when(area.width()).thenReturn(AREA_WIDTH);
        when(area.height()).thenReturn(AREA_HEIGHT);
        when(area.getShips()).thenReturn(mShips);

        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                Coordinate c = new Coordinate(x,y);
                when(area.getSquare(c)).thenReturn(mSquares.get(x).get(y));
            }
        }

        return  area;
    }

    private void verifyMockCalls(IGameArea area) {
        verify(area).getShips();
        verify(area).width();
        verify(area).height();

        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                Coordinate c = new Coordinate(x,y);
                verify(area).getSquare(c);
            }
        }
        verifyNoMoreInteractions(area);
    }

    private String getShipString() {
        ShipWriter writer = new ShipWriter();
        return String.format("%s, %s",
                writer.shipToString(mShips.get(0)),
                writer.shipToString(mShips.get(1)));
    }

    @Test
    public void writeCorrectGameAreaStringWithNoHits() {
        IGameArea area = setUpGameArea(new ArrayList<Coordinate>());

        String areaStr = new GameAreaWriter().gameAreaToString(area);
        assertNotNull(areaStr);
        String expected = String.format(AREA_STRING_TEMPLATE,
                                        AREA_WIDTH, AREA_HEIGHT, getShipString(), "");
        assertEquals(expected, areaStr);
        verifyMockCalls(area);
    }

    @Test
    public void writeCorrectGameAreaStringWithHits() {
        ArrayList<Coordinate> hits = new ArrayList<>();
        hits.add(new Coordinate(0,0));
        hits.add(new Coordinate(5,4));
        IGameArea area = setUpGameArea(hits);

        String areaStr = new GameAreaWriter().gameAreaToString(area);
        assertNotNull(areaStr);
        CoordinateWriter coordinateWriter = new CoordinateWriter();

        String hitString = String.format("%s, %s",
                coordinateWriter.coordinateToString(hits.get(0)),
                coordinateWriter.coordinateToString(hits.get(1)));

        String expected = String.format(AREA_STRING_TEMPLATE,
                AREA_WIDTH, AREA_HEIGHT, getShipString(),
                hitString);

        assertEquals(expected, areaStr);
        verifyMockCalls(area);
    }
}