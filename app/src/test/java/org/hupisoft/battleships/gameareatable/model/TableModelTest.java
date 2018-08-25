package org.hupisoft.battleships.gameareatable.model;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.ISquare;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TableModelTest {

    private ArrayList<ArrayList<ISquare>> mockSquares;
    private IGameArea mockArea;
    private static final Integer AREA_WIDTH = 8;
    private static final Integer AREA_HEIGHT = 5;
    private TableModel mModel;

    @Before
    public void setUp() {
        mModel = new TableModel();
        // Create mock area and squares.
        mockArea = mock(IGameArea.class);
        mockSquares = new ArrayList<>();
        for (int y = 0; y < AREA_HEIGHT; ++y) {
            ArrayList<ISquare> squares = new ArrayList<>();
            for (int x = 0; x < AREA_WIDTH; ++x) {
                squares.add(mock(ISquare.class));
            }
            mockSquares.add(squares);
        }

        // Setup mock calls
        when(mockArea.width()).thenReturn(AREA_WIDTH);
        when(mockArea.height()).thenReturn(AREA_HEIGHT);
        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                Coordinate c = new Coordinate(x,y);
                when(mockArea.getSquare(c)).thenReturn(mockSquares.get(y).get(x));
            }
        }
    }

    @Test
    public void tableModelReturnsNullListWhenAreaNotSet() {
        assertNull(mModel.getCellModelList());
        assertNull(mModel.getColumnHeaderModelList());
        assertNull(mModel.getRowHeaderModelList());
    }

    @Test
    public void tableModelMatchesWithGameArea() {
        mModel.setGameArea(mockArea);

        // Verify row models
        List<RowHeaderModel> rowHeaders = mModel.getRowHeaderModelList();
        assertNotNull(rowHeaders);
        assertEquals((int)AREA_HEIGHT, rowHeaders.size());
        for (int i = 0; i <AREA_HEIGHT; ++i) {
            assertEquals(Integer.toString(i+1), rowHeaders.get(i).getData());
        }

        // Verify column models
        List<ColumnHeaderModel> columnHeaders = mModel.getColumnHeaderModelList();
        assertNotNull(columnHeaders);
        assertEquals((int)AREA_WIDTH, columnHeaders.size());
        String expected[] = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (int i = 0; i <AREA_WIDTH; ++i) {
            assertEquals(expected[i], columnHeaders.get(i).getData());
        }

        // Verify cell models
        List<List<ISquare>> squares = mModel.getCellModelList();
        for (int y = 0; y < AREA_HEIGHT; ++y) {
            for (int x = 0; x < AREA_WIDTH; ++x) {
                assertEquals(mockSquares.get(y).get(x), squares.get(y).get(x));
            }
        }

        verify(mockArea, times(2)).width();
        verify(mockArea, times(2)).height();
        for (int x = 0; x < AREA_WIDTH; ++x) {
            for (int y = 0; y < AREA_HEIGHT; ++y) {
                Coordinate c = new Coordinate(x,y);
                verify(mockArea).getSquare(c);
                verifyNoMoreInteractions(mockSquares.get(y).get(x));
            }
        }
        verifyNoMoreInteractions(mockArea);
    }

}