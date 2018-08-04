package org.hupisoft.battleships_core.gamelogicwriter;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.IShip;
import org.hupisoft.battleships_core.ISquare;
import org.hupisoft.battleships_core.Player;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test the GameLogicWriter class.
 */
public class GameLogicWriterTest {

    private static final int AREA_WIDTH = 12;
    private static final int AREA_HEIGHT = 8;

    private IGameArea mArea1;
    private IGameArea mArea2;

    private IGameLogic setUpMocks(Player playerInTurn) {
        ISquare sqr = mock(ISquare.class);
        when(sqr.isHit()).thenReturn(false);

        ArrayList<IShip> ships1 = new ArrayList<>();
        IShip ship1 = mock(IShip.class);
        when(ship1.length()).thenReturn(3);
        when(ship1.orientation()).thenReturn(IShip.Orientation.VERTICAL);
        when(ship1.getBowCoordinates()).thenReturn(new Coordinate(0,0));
        ships1.add(ship1);

        ArrayList<IShip> ships2 = new ArrayList<>();
        IShip ship2 = mock(IShip.class);
        when(ship2.length()).thenReturn(3);
        when(ship2.orientation()).thenReturn(IShip.Orientation.HORIZONTAL);
        when(ship2.getBowCoordinates()).thenReturn(new Coordinate(1,1));
        ships2.add(ship2);

        IGameArea area1 = mock(IGameArea.class);
        when(area1.width()).thenReturn(AREA_WIDTH);
        when(area1.height()).thenReturn(AREA_HEIGHT);
        when(area1.getShips()).thenReturn(ships1);
        when(area1.getSquare(any(Coordinate.class))).thenReturn(sqr);
        mArea1 = area1;

        IGameArea area2 = mock(IGameArea.class);
        when(area1.width()).thenReturn(AREA_WIDTH);
        when(area1.height()).thenReturn(AREA_HEIGHT);
        when(area1.getShips()).thenReturn(ships2);
        when(area2.getSquare(any(Coordinate.class))).thenReturn(sqr);
        mArea2 = area2;

        IGameLogic logic = mock(IGameLogic.class);
        when(logic.getGameArea(Player.PLAYER_1)).thenReturn(area1);
        when(logic.getGameArea(Player.PLAYER_2)).thenReturn(area2);
        when(logic.getCurrentPlayer()).thenReturn(playerInTurn);

        return logic;
    }

    private void verifyMockCalls(IGameLogic logic) {
        verify(logic).getCurrentPlayer();
        verify(logic).getGameArea(Player.PLAYER_1);
        verify(logic).getGameArea(Player.PLAYER_2);
        verifyNoMoreInteractions(logic);
    }

    @Test
    public void writeCorrectStringWithPlayer1InTurn() {
        IGameLogic logic = setUpMocks(Player.PLAYER_1);
        GameLogicWriter writer = new GameLogicWriter();

        String str = writer.logicToString(logic);
        assertNotNull(str);
        GameAreaWriter areaWriter = new GameAreaWriter();
        String expected = String.format("GameLogic{player:PLAYER_1, area1:%s, area2:%s}",
                                        areaWriter.gameAreaToString(mArea1),
                                        areaWriter.gameAreaToString(mArea2));

        assertEquals(expected, str);
    }

    @Test
    public void writeCorrectStringWithPlayer2InTurn() {
        IGameLogic logic = setUpMocks(Player.PLAYER_2);
        GameLogicWriter writer = new GameLogicWriter();

        String str = writer.logicToString(logic);
        assertNotNull(str);
        GameAreaWriter areaWriter = new GameAreaWriter();
        String expected = String.format("GameLogic{player:PLAYER_2, area1:%s, area2:%s}",
                areaWriter.gameAreaToString(mArea1),
                areaWriter.gameAreaToString(mArea2));

        assertEquals(expected, str);
    }

}