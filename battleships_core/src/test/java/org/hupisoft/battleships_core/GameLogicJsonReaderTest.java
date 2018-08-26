package org.hupisoft.battleships_core;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameLogicJsonReaderTest {

    @Test
    public void parsingInvalidJsonReturnsNull()
    {
        String jsonStr = "{";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfGameLogicFieldIsMissing()
    {
        String jsonStr = "{}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfPlayer1AreaIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}" +
                "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfPlayer2AreaIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfCurrentPlayerIsInvalid()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"NOT_A_PLAYER\", " +
                        "\"player1Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}], " +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}], " +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfCurrentPlayerFiledIsNotAString()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": 1, " +
                        "\"player1Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}], " +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}], " +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfCurrentPlayerIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {" +
                        "\"player1Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaWidthIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaHeightIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaWidthIsNotANumber()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":\"\", \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaHeightIsNotANumber()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":\"8\", " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipsIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipsIsNotArray()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\": {}, " +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipLengthIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipLengthIsNotANumber()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":\"3\", \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipOrientationIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipOrientationIsInvalid()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"ILLEGAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipOrientationIsNotAString()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": 0}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipBowIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipBowIsNotAnObject()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":[], \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipBowXIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"y\":0}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipBowYIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":0}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipBowXIsNotANumber()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":\"0\", \"y\":3}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfAreaShipBowYIsNotANumber()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":0, \"y\":\"3\"}, \"orientation\": \"VERTICAL\"}]," +
                        "\"hits\": []}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfHitsAreMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}]" +
                        "}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfHitsIsNotArray()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}], " +
                        "\"hits\":{}}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfHitsCoordinateXIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}], " +
                        "\"hits\":[{\"y\":4}]}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfHitsCoordinateYIsMissing()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}], " +
                        "\"hits\":[{\"x\":4}]}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfHitsCoordinateXIsNotANumber()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}], " +
                        "\"hits\":[{\"x\":\"1\", \"y\":4}]}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonFailsIfHitsCoordinateYIsNotANumber()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_1\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": []}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}], " +
                        "\"hits\":[{\"x\":1, \"y\":\"4\"}]}" +
                        "}}";
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNull(logic);
    }

    @Test
    public void parsingJsonSucceeds()
    {
        String jsonStr =
                "{\"gameLogic\": {\"currentPlayer\": \"PLAYER_2\", " +
                        "\"player1Area\": {\"width\":12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":1, \"y\":2}, \"orientation\": \"HORIZONTAL\"}]," +
                        "\"hits\": [{\"x\":4, \"y\":5}, {\"x\":5, \"y\":6}]}, " +
                        "\"player2Area\": {\"width\": 12, \"height\":8, " +
                        "\"ships\":[{\"length\":3, \"bowCoordinates\":{\"x\":3, \"y\":4}, \"orientation\": \"VERTICAL\"}], " +
                        "\"hits\":[{\"x\":0, \"y\":1}, {\"x\":1, \"y\":2}, {\"x\":3, \"y\":4}]}" +
                        "}}";

        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(jsonStr);
        assertNotNull(logic);

        assertEquals(Player.PLAYER_2, logic.getCurrentPlayer());
        assertEquals(3, logic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(2, logic.getNumberOfHits(Player.PLAYER_2));

        IGameArea area1 = logic.getGameArea(Player.PLAYER_1);
        assertEquals(12, area1.width());
        assertEquals(8, area1.height());
        assertEquals(2, area1.hitCount());
        assertTrue(area1.getSquare(new Coordinate(4,5)).isHit());
        assertTrue(area1.getSquare(new Coordinate(5,6)).isHit());
        ArrayList<IShip> ships1 = area1.getShips();
        assertEquals(1, ships1.size());
        assertEquals(3, ships1.get(0).length());
        assertEquals(new Coordinate(1,2), ships1.get(0).getBowCoordinates());
        assertEquals(IShip.Orientation.HORIZONTAL, ships1.get(0).orientation());

        IGameArea area2 = logic.getGameArea(Player.PLAYER_2);
        assertEquals(12, area2.width());
        assertEquals(8, area2.height());
        assertEquals(3, area2.hitCount());
        assertTrue(area2.getSquare(new Coordinate(0,1)).isHit());
        assertTrue(area2.getSquare(new Coordinate(1,2)).isHit());
        assertTrue(area2.getSquare(new Coordinate(3,4)).isHit());
        ArrayList<IShip> ships2 = area2.getShips();
        assertEquals(1, ships2.size());
        assertEquals(3, ships2.get(0).length());
        assertEquals(new Coordinate(3,4), ships2.get(0).getBowCoordinates());
        assertEquals(IShip.Orientation.VERTICAL, ships2.get(0).orientation());
    }
}