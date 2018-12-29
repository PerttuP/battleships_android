package org.hupisoft.battleships_core;

import java.io.StringWriter;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Creates JSON string representing current state of the game.
 * This can be used to save game instance.
 */
public class GameLogicJsonWriter {

    /**
     * Creates JSON string representing current state of the game.
     * @param logic Game logic instance.
     * @return JSON string representing logic's current state.
     */
    public String writeJsonString(IGameLogic logic)
    {
        JSONObject logicObj = new JSONObject();
        logicObj.put(GameLogicJsonDefinitions.CURRENT_PLAYER_TAG, logic.getCurrentPlayer().toString());
        logicObj.put(GameLogicJsonDefinitions.PLAYER1_GAME_AREA_TAG, createAreaObject(logic.getGameArea(Player.PLAYER_1)));
        logicObj.put(GameLogicJsonDefinitions.PLAYER2_GAME_AREA_TAG, createAreaObject(logic.getGameArea(Player.PLAYER_2)));

        JSONObject root = new JSONObject();
        root.put(GameLogicJsonDefinitions.GAME_LOGIC_TAG, logicObj);
        return root.toString();
    }

    private JSONObject createAreaObject(IGameArea area)
    {
        JSONObject obj = new JSONObject();
        obj.put(GameLogicJsonDefinitions.WIDTH_TAG, area.width());
        obj.put(GameLogicJsonDefinitions.HEIGHT_TAG, area.height());
        obj.put(GameLogicJsonDefinitions.SHIPS_TAG, createShipsArray(area.getShips()));
        obj.put(GameLogicJsonDefinitions.HITS_TAG, createHitsArray(area));
        return obj;
    }

    private JSONArray createShipsArray(List<IShip> ships)
    {
        JSONArray array = new JSONArray();
        for (IShip ship : ships) {
            array.put(createShipJson(ship));
        }
        return array;
    }

    private JSONObject createShipJson(IShip ship)
    {
        JSONObject obj = new JSONObject();
        obj.put(GameLogicJsonDefinitions.LENGTH_TAG, ship.length());
        obj.put(GameLogicJsonDefinitions.ORIENTATION_TAG, ship.orientation().toString());
        obj.put(GameLogicJsonDefinitions.BOW_TAG, createCoordinateJson(ship.getBowCoordinates()));
        return obj;
    }

    private JSONArray createHitsArray(IGameArea area)
    {
        JSONArray array = new JSONArray();
        IGameAreaLogger logger = area.getLogger();
        for (int i = 0; i < logger.numberOfPerformedActions(); ++i) {
            Coordinate c = logger.getAction(i).location();
            array.put(createCoordinateJson(c));
        }
        return array;
    }

    private JSONObject createCoordinateJson(Coordinate coordinate)
    {
        JSONObject obj = new JSONObject();
        obj.put(GameLogicJsonDefinitions.X_TAG, coordinate.x());
        obj.put(GameLogicJsonDefinitions.Y_TAG, coordinate.y());
        return obj;
    }
}
