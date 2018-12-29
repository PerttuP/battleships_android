package org.hupisoft.battleships_core;

import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

import static org.junit.Assert.*;

/**
 * Unit tests for GameLogicJsonWriter class.
 */
public class GameLogicJsonWriterTest {

    private void compareAreas(JSONObject areaObj, IGameArea expectedArea)
    {
        assertNotNull(areaObj);
        assertEquals(expectedArea.width(), areaObj.getInt(GameLogicJsonDefinitions.WIDTH_TAG));
        assertEquals(expectedArea.height(), areaObj.getInt(GameLogicJsonDefinitions.HEIGHT_TAG));

        // Compare ships
        List<IShip> expectedShips = expectedArea.getShips();
        JSONArray shipsArray = areaObj.getJSONArray(GameLogicJsonDefinitions.SHIPS_TAG);
        assertEquals(expectedShips.size(), shipsArray.length());

        for (int i = 0; i < expectedShips.size(); ++i)
        {
            JSONObject shipObj = shipsArray.getJSONObject(i);
            assertNotNull(shipObj);
            assertEquals(expectedShips.get(i).length(), shipObj.getInt(GameLogicJsonDefinitions.LENGTH_TAG));
            IShip.Orientation orientation = IShip.Orientation.valueOf(shipObj.getString(GameLogicJsonDefinitions.ORIENTATION_TAG));
            assertEquals(expectedShips.get(i).orientation(), orientation);

            JSONObject bowObj = shipObj.getJSONObject(GameLogicJsonDefinitions.BOW_TAG);
            assertNotNull(bowObj);
            int x = bowObj.getInt(GameLogicJsonDefinitions.X_TAG);
            int y = bowObj.getInt(GameLogicJsonDefinitions.Y_TAG);
            assertEquals(expectedShips.get(i).getBowCoordinates(), new Coordinate(x,y));
        }

        // Compare hits
        JSONArray hitsArray = areaObj.getJSONArray(GameLogicJsonDefinitions.HITS_TAG);
        IGameAreaLogger logger = expectedArea.getLogger();
        assertNotNull(hitsArray);
        assertEquals(expectedArea.hitCount(), hitsArray.length());
        assertEquals(logger.numberOfPerformedActions(), hitsArray.length());

        for (int i = 0; i < hitsArray.length(); ++i)
        {
            JSONObject hitObj = hitsArray.getJSONObject(i);
            assertNotNull(hitObj);
            int x = hitObj.getInt(GameLogicJsonDefinitions.X_TAG);
            int y = hitObj.getInt(GameLogicJsonDefinitions.Y_TAG);
            Coordinate hitLocation = new Coordinate(x,y);
            assertTrue(expectedArea.getSquare(hitLocation).isHit());
            assertEquals(logger.getAction(i).location(), hitLocation);
        }
    }

    @Test
    public void writeGameLogicWithoutHits()
    {
        IGameLogic logic = new GameLogicBuilder().createNewGame(12, 8, new int[]{2,3,5});
        String jsonStr = new GameLogicJsonWriter().writeJsonString(logic);
        assertNotNull(jsonStr);

        JSONObject root = new JSONObject(jsonStr);

        JSONObject gameLogicObj = root.getJSONObject(GameLogicJsonDefinitions.GAME_LOGIC_TAG);
        assertNotNull(gameLogicObj);

        Player currentPlayer = Player.valueOf(gameLogicObj.getString(GameLogicJsonDefinitions.CURRENT_PLAYER_TAG));
        assertEquals(logic.getCurrentPlayer(), currentPlayer);

        JSONObject gameAreaObj1 = gameLogicObj.getJSONObject(GameLogicJsonDefinitions.PLAYER1_GAME_AREA_TAG);
        compareAreas(gameAreaObj1, logic.getGameArea(Player.PLAYER_1));
        JSONObject gameAreaObj2 = gameLogicObj.getJSONObject(GameLogicJsonDefinitions.PLAYER2_GAME_AREA_TAG);
        compareAreas(gameAreaObj2, logic.getGameArea(Player.PLAYER_2));
    }

    @Test
    public void writeGameLogicWithHits()
    {
        IGameLogic logic = new GameLogicBuilder().createNewGame(12, 8, new int[]{2,3,5});
        logic.playerAction(new Coordinate(0,1));
        logic.playerAction(new Coordinate(11, 7));
        logic.playerAction(new Coordinate(1,2));
        logic.playerAction(new Coordinate(10, 6));
        logic.playerAction(new Coordinate(2,3));
        String jsonStr = new GameLogicJsonWriter().writeJsonString(logic);
        assertNotNull(jsonStr);

        JSONObject root = new JSONObject(jsonStr);

        JSONObject gameLogicObj = root.getJSONObject(GameLogicJsonDefinitions.GAME_LOGIC_TAG);
        assertNotNull(gameLogicObj);

        Player currentPlayer = Player.valueOf(gameLogicObj.getString(GameLogicJsonDefinitions.CURRENT_PLAYER_TAG));
        assertEquals(logic.getCurrentPlayer(), currentPlayer);

        JSONObject gameAreaObj1 = gameLogicObj.getJSONObject(GameLogicJsonDefinitions.PLAYER1_GAME_AREA_TAG);
        compareAreas(gameAreaObj1, logic.getGameArea(Player.PLAYER_1));
        JSONObject gameAreaObj2 = gameLogicObj.getJSONObject(GameLogicJsonDefinitions.PLAYER2_GAME_AREA_TAG);
        compareAreas(gameAreaObj2, logic.getGameArea(Player.PLAYER_2));
    }

}