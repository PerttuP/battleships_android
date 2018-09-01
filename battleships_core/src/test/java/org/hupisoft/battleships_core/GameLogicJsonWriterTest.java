package org.hupisoft.battleships_core;

import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import static org.junit.Assert.*;

/**
 * Unit tests for GameLogicJsonWriter class.
 */
public class GameLogicJsonWriterTest {

    private void compareAreas(JsonObject areaObj, IGameArea expectedArea)
    {
        assertNotNull(areaObj);
        assertEquals(expectedArea.width(), areaObj.getInt(GameLogicJsonDefinitions.WIDTH_TAG));
        assertEquals(expectedArea.height(), areaObj.getInt(GameLogicJsonDefinitions.HEIGHT_TAG));

        // Compare ships
        List<IShip> expectedShips = expectedArea.getShips();
        JsonArray shipsArray = areaObj.getJsonArray(GameLogicJsonDefinitions.SHIPS_TAG);
        assertEquals(expectedShips.size(), shipsArray.size());

        for (int i = 0; i < expectedShips.size(); ++i)
        {
            JsonObject shipObj = shipsArray.getJsonObject(i);
            assertNotNull(shipObj);
            assertEquals(expectedShips.get(i).length(), shipObj.getInt(GameLogicJsonDefinitions.LENGTH_TAG));
            IShip.Orientation orientation = IShip.Orientation.valueOf(shipObj.getString(GameLogicJsonDefinitions.ORIENTATION_TAG));
            assertEquals(expectedShips.get(i).orientation(), orientation);

            JsonObject bowObj = shipObj.getJsonObject(GameLogicJsonDefinitions.BOW_TAG);
            assertNotNull(bowObj);
            int x = bowObj.getInt(GameLogicJsonDefinitions.X_TAG);
            int y = bowObj.getInt(GameLogicJsonDefinitions.Y_TAG);
            assertEquals(expectedShips.get(i).getBowCoordinates(), new Coordinate(x,y));
        }

        // Compare hits
        JsonArray hitsArray = areaObj.getJsonArray(GameLogicJsonDefinitions.HITS_TAG);
        assertNotNull(hitsArray);
        assertEquals(expectedArea.hitCount(), hitsArray.size());

        for (int i = 0; i < hitsArray.size(); ++i)
        {
            JsonObject hitObj = hitsArray.getJsonObject(i);
            assertNotNull(hitObj);
            int x = hitObj.getInt(GameLogicJsonDefinitions.X_TAG);
            int y = hitObj.getInt(GameLogicJsonDefinitions.Y_TAG);
            Coordinate hitLocation = new Coordinate(x,y);
            assertTrue(expectedArea.getSquare(hitLocation).isHit());
        }
    }

    @Test
    public void writeGameLogicWithoutHits()
    {
        IGameLogic logic = new GameLogicBuilder().createNewGame(12, 8, new int[]{2,3,5});
        String jsonStr = new GameLogicJsonWriter().writeJsonString(logic);
        assertNotNull(jsonStr);

        JsonReader reader = Json.createReader(new StringReader(jsonStr));
        JsonObject root = reader.readObject();

        JsonObject gameLogicObj = root.getJsonObject(GameLogicJsonDefinitions.GAME_LOGIC_TAG);
        assertNotNull(gameLogicObj);

        Player currentPlayer = Player.valueOf(gameLogicObj.getString(GameLogicJsonDefinitions.CURRENT_PLAYER_TAG));
        assertEquals(logic.getCurrentPlayer(), currentPlayer);

        JsonObject gameAreaObj1 = gameLogicObj.getJsonObject(GameLogicJsonDefinitions.PLAYER1_GAME_AREA_TAG);
        compareAreas(gameAreaObj1, logic.getGameArea(Player.PLAYER_1));
        JsonObject gameAreaObj2 = gameLogicObj.getJsonObject(GameLogicJsonDefinitions.PLAYER2_GAME_AREA_TAG);
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

        JsonReader reader = Json.createReader(new StringReader(jsonStr));
        JsonObject root = reader.readObject();

        JsonObject gameLogicObj = root.getJsonObject(GameLogicJsonDefinitions.GAME_LOGIC_TAG);
        assertNotNull(gameLogicObj);

        Player currentPlayer = Player.valueOf(gameLogicObj.getString(GameLogicJsonDefinitions.CURRENT_PLAYER_TAG));
        assertEquals(logic.getCurrentPlayer(), currentPlayer);

        JsonObject gameAreaObj1 = gameLogicObj.getJsonObject(GameLogicJsonDefinitions.PLAYER1_GAME_AREA_TAG);
        compareAreas(gameAreaObj1, logic.getGameArea(Player.PLAYER_1));
        JsonObject gameAreaObj2 = gameLogicObj.getJsonObject(GameLogicJsonDefinitions.PLAYER2_GAME_AREA_TAG);
        compareAreas(gameAreaObj2, logic.getGameArea(Player.PLAYER_2));
    }

}