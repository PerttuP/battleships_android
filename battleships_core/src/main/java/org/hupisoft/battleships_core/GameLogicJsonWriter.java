package org.hupisoft.battleships_core;

import java.io.StringWriter;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;

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
        JsonObject logicObj = Json.createObjectBuilder()
                .add(GameLogicJsonDefinitions.CURRENT_PLAYER_TAG, logic.getCurrentPlayer().toString())
                .add(GameLogicJsonDefinitions.PLAYER1_GAME_AREA_TAG, createAreaObject(logic.getGameArea(Player.PLAYER_1)))
                .add(GameLogicJsonDefinitions.PLAYER2_GAME_AREA_TAG, createAreaObject(logic.getGameArea(Player.PLAYER_2)))
                .build();

        JsonObject root = Json.createObjectBuilder()
                .add(GameLogicJsonDefinitions.GAME_LOGIC_TAG, logicObj)
                .build();

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stringWriter);
        jsonWriter.writeObject(root);
        jsonWriter.close();
        return stringWriter.getBuffer().toString();
    }

    private JsonObject createAreaObject(IGameArea area)
    {
        return Json.createObjectBuilder()
                .add(GameLogicJsonDefinitions.WIDTH_TAG, area.width())
                .add(GameLogicJsonDefinitions.HEIGHT_TAG, area.height())
                .add(GameLogicJsonDefinitions.SHIPS_TAG, createShipsArray(area.getShips()))
                .add(GameLogicJsonDefinitions.HITS_TAG, createHitsArray(area))
                .build();
    }

    private JsonArray createShipsArray(List<IShip> ships)
    {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (IShip ship : ships) {
            builder.add(createShipJson(ship));
        }
        return builder.build();
    }

    private JsonObject createShipJson(IShip ship)
    {
        return Json.createObjectBuilder()
                .add(GameLogicJsonDefinitions.LENGTH_TAG, ship.length())
                .add(GameLogicJsonDefinitions.ORIENTATION_TAG, ship.orientation().toString())
                .add(GameLogicJsonDefinitions.BOW_TAG, createCoordinateJson(ship.getBowCoordinates()))
                .build();
    }

    private JsonArray createHitsArray(IGameArea area)
    {
        JsonArrayBuilder builder = Json.createArrayBuilder();

        for (int x = 0; x < area.width(); ++x) {
            for (int y = 0; y < area.height(); ++y) {
                Coordinate c = new Coordinate(x,y);
                if (area.getSquare(c).isHit()) {
                    builder.add(createCoordinateJson(c));
                }
            }
        }

        return builder.build();
    }

    private JsonObject createCoordinateJson(Coordinate coordinate)
    {
        return Json.createObjectBuilder()
                .add(GameLogicJsonDefinitions.X_TAG, coordinate.x())
                .add(GameLogicJsonDefinitions.Y_TAG, coordinate.y())
                .build();
    }
}
