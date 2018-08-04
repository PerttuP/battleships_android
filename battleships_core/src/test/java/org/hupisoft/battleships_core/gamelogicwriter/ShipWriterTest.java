package org.hupisoft.battleships_core.gamelogicwriter;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IShip;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test the ShipWriter class.
 */
public class ShipWriterTest {

    private static final String TEMPLATE = "Ship{length:%d, bow:%s, orientation:%s}";

    private IShip setUpShip(int length, Coordinate bow, IShip.Orientation orientation) {
        IShip ship = mock(IShip.class);
        when(ship.length()).thenReturn(length);
        when(ship.getBowCoordinates()).thenReturn(bow);
        when(ship.orientation()).thenReturn(orientation);
        return ship;
    }

    private void verifyMockCalls(IShip ship) {
        verify(ship).length();
        verify(ship).getBowCoordinates();
        verify(ship).orientation();
        verifyNoMoreInteractions(ship);
    }

    @Test
    public void writeCorrectVerticalShipString() {
        Coordinate bowCoordinate = new Coordinate(3,4);
        int shipLength = 3;
        IShip.Orientation orientation = IShip.Orientation.VERTICAL;
        IShip ship = setUpShip(shipLength, bowCoordinate, orientation);
        ShipWriter writer = new ShipWriter();

        String shipString = writer.shipToString(ship);
        String bowStr = new CoordinateWriter().coordinateToString(bowCoordinate);
        String expected = String.format("Ship{length:3, bow:%s, orientation:VERTICAL}", bowStr);
        assertEquals(expected, shipString);

        verifyMockCalls(ship);
    }

    @Test
    public void writeCorrectHorisontalShipString() {
        Coordinate bowCoordinate = new Coordinate(5,8);
        int shipLength = 5;
        IShip.Orientation orientation = IShip.Orientation.HORIZONTAL;
        IShip ship = setUpShip(shipLength, bowCoordinate, orientation);
        ShipWriter writer = new ShipWriter();

        String shipString = writer.shipToString(ship);
        String bowStr = new CoordinateWriter().coordinateToString(bowCoordinate);
        String expected = String.format("Ship{length:5, bow:%s, orientation:HORIZONTAL}", bowStr);
        assertEquals(expected, shipString);

        verifyMockCalls(ship);
    }

}