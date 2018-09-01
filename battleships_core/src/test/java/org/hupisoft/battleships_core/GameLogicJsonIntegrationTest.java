package org.hupisoft.battleships_core;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Integration tests for JSON reader and writer.
 */
public class GameLogicJsonIntegrationTest {

    private void compareLogics(IGameLogic expected, IGameLogic actual)
    {
        assertEquals(expected.getCurrentPlayer(), actual.getCurrentPlayer());
        assertEquals(expected.getNumberOfHits(Player.PLAYER_1), actual.getNumberOfHits(Player.PLAYER_1));
        assertEquals(expected.getNumberOfHits(Player.PLAYER_2), actual.getNumberOfHits(Player.PLAYER_2));
        assertEquals(expected.isGameOver(), actual.isGameOver());
        assertEquals(expected.getWinner(), actual.getWinner());
        compareAreas(expected.getGameArea(Player.PLAYER_1), actual.getGameArea(Player.PLAYER_1));
        compareAreas(expected.getGameArea(Player.PLAYER_2), actual.getGameArea(Player.PLAYER_2));
    }

    private void compareAreas(IGameArea expected, IGameArea actual)
    {
        assertEquals(expected.width(), actual.width());
        assertEquals(expected.height(), actual.height());
        assertEquals(expected.hitCount(), actual.hitCount());
        assertEquals(expected.remainingShipCount(), actual.remainingShipCount());

        compareShips(expected.getShips(), actual.getShips());
        compareAllSquares(expected, actual);
    }

    private void compareShips(List<IShip> expected, List<IShip> actual)
    {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); ++i) {
            assertEquals(expected.get(i).length(), actual.get(i).length());
            assertEquals(expected.get(i).orientation(), actual.get(i).orientation());
            assertEquals(expected.get(i).getBowCoordinates(), actual.get(i).getBowCoordinates());
            assertEquals(expected.get(i).hitCount(), actual.get(i).hitCount());
            assertEquals(expected.get(i).isDestroyed(), actual.get(i).isDestroyed());
        }
    }

    private void compareAllSquares(IGameArea expected, IGameArea actual)
    {
        for (int x = 0; x < expected.width(); ++x) {
            for (int y = 0; y < expected.height(); ++y) {
                Coordinate c = new Coordinate(x,y);
                compareSquares(expected.getSquare(c), actual.getSquare(c));
            }
        }
    }

    private void compareSquares(ISquare expected, ISquare actual)
    {
        assertEquals(expected.isHit(), actual.isHit());
        assertEquals(expected.getLocation(), actual.getLocation());
        assertEquals(expected.getShip() != null, actual.getShip() != null);
    }

    @Test
    public void saveAndReloadInitialGameLogic()
    {
        IGameLogic logic1 = new GameLogicBuilder().createNewGame(12, 8, new int[]{2,3,5});
        String jsonStr = new GameLogicJsonWriter().writeJsonString(logic1);
        assertNotNull(jsonStr);

        IGameLogic logic2 = new GameLogicJsonReader().readJsonString(jsonStr);
        assertNotNull(logic2);

        compareLogics(logic1, logic2);
    }

    @Test
    public void saveAndReloadOngoingGame()
    {
        IGameLogic logic1 = new GameLogicBuilder().createNewGame(12, 8, new int[]{2,3,5});
        logic1.playerAction(new Coordinate(0,1));
        logic1.playerAction(new Coordinate(11,7));
        logic1.playerAction(new Coordinate(1,2));
        logic1.playerAction(new Coordinate(10,6));
        logic1.playerAction(new Coordinate(2,3));
        String jsonStr = new GameLogicJsonWriter().writeJsonString(logic1);
        assertNotNull(jsonStr);

        IGameLogic logic2 = new GameLogicJsonReader().readJsonString(jsonStr);
        assertNotNull(logic2);

        compareLogics(logic1, logic2);
    }
}
