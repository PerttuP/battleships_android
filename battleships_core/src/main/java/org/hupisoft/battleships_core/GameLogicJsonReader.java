package org.hupisoft.battleships_core;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class GameLogicJsonReader {

    public IGameLogic readJsonString(String jsonString)
    {
        GameLogic logic = null;

        try {
            JsonReader reader = Json.createReader(new StringReader(jsonString));
            JsonObject jsonObject = reader.readObject();

            JsonObject gameLogicObj = jsonObject.getJsonObject(GameLogicJsonDefinitions.GAME_LOGIC_TAG);
            if (gameLogicObj == null) {
                throw new JsonException("Missing " + GameLogicJsonDefinitions.GAME_LOGIC_TAG + " field.");
            }

            JsonObject gameArea1Obj = gameLogicObj.getJsonObject(GameLogicJsonDefinitions.PLAYER1_GAME_AREA_TAG);
            if (gameArea1Obj == null) {
                throw new JsonException("Missing " + GameLogicJsonDefinitions.PLAYER1_GAME_AREA_TAG + " field.");
            }

            JsonObject gameArea2Obj = gameLogicObj.getJsonObject(GameLogicJsonDefinitions.PLAYER2_GAME_AREA_TAG);
            if (gameArea2Obj == null) {
                throw new JsonException("Missing " + GameLogicJsonDefinitions.PLAYER2_GAME_AREA_TAG + " field.");
            }

            GameLogic.PlayerGameSetup setup1 = createPlayerSetupFromJson(gameArea1Obj);
            GameLogic.PlayerGameSetup setup2 = createPlayerSetupFromJson(gameArea2Obj);
            setup1.numberOfHits = setup2.area.hitCount();
            setup2.numberOfHits = setup1.area.hitCount();

            Player currentPlayer = null;
            try {
                String playerStr = gameLogicObj.getString(GameLogicJsonDefinitions.CURRENT_PLAYER_TAG);
                currentPlayer = Player.valueOf(playerStr);
            }
            catch (NullPointerException|ClassCastException|IllegalArgumentException e) {
                JsonException jsonException = new JsonException("Invalid " +
                                                                GameLogicJsonDefinitions.CURRENT_PLAYER_TAG +
                                                                " field: " + e.getMessage());
                throw jsonException;
            }

            logic = new GameLogic(setup1, setup2, currentPlayer);
        }
        catch (JsonException e)
        {
            printError(e.getMessage());
        }

        return logic;
    }



    private GameLogic.PlayerGameSetup createPlayerSetupFromJson(JsonObject jsonObject)
    {
        try {
            Integer areaWidth = jsonObject.getInt(GameLogicJsonDefinitions.WIDTH_TAG);
            Integer areaHeight = jsonObject.getInt(GameLogicJsonDefinitions.HEIGHT_TAG);
            List<List<ISquare>> squares = createSquares(areaWidth, areaHeight);

            JsonArray shipsObj = jsonObject.getJsonArray(GameLogicJsonDefinitions.SHIPS_TAG);
            ArrayList<IShip> ships = createShipsFromJson(shipsObj);
            GameAreaBuilder.setShipOccupations(ships, squares);

            GameArea area = new GameArea(squares, ships);

            JsonArray hitsObj = jsonObject.getJsonArray(GameLogicJsonDefinitions.HITS_TAG);
            ArrayList<Coordinate> hits = createHitCoordinatesFromJson(hitsObj);
            for (Coordinate c : hits) {
                area.hit(c);
            }

            GameLogic.PlayerGameSetup setup = new GameLogic.PlayerGameSetup();
            setup.area = area;
            setup.numberOfHits = 0; // Is assigned based on opponents game area hits.
            return setup;
        }
        catch (NullPointerException e) {
            throw new JsonException("Invalid area dimension: " + e.getMessage());
        }
        catch (ClassCastException e) {
            throw new JsonException("Invalid cast: " + e.getMessage());
        }
    }

    private ArrayList<IShip> createShipsFromJson(JsonArray shipsObj)
    {
        if (shipsObj == null) {
            throw new JsonException("Missing " + GameLogicJsonDefinitions.SHIPS_TAG + " field.");
        }

        ArrayList<IShip> ships = new ArrayList<>();
        for (int i=0; i < shipsObj.size(); ++i) {
            JsonObject obj = shipsObj.getJsonObject(i);
            ships.add(createShipFromJson(obj));
        }
        return ships;
    }

    private IShip createShipFromJson(JsonObject obj)
    {
        try {
            Integer length = obj.getInt(GameLogicJsonDefinitions.LENGTH_TAG);
            String orientationStr = obj.getString(GameLogicJsonDefinitions.ORIENTATION_TAG);
            IShip.Orientation orientation = IShip.Orientation.valueOf(orientationStr);
            JsonObject bowObj = obj.getJsonObject(GameLogicJsonDefinitions.BOW_TAG);
            Coordinate bowCoordinate = createCoordinateFromJson(bowObj);
            return new Ship(length, bowCoordinate, orientation);
        }
        catch (NullPointerException | ClassCastException e) {
            throw new JsonException("Invalid ship field: " + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            throw new JsonException("Invalid ship orientation: " + e.getMessage());
        }
    }

    private ArrayList<Coordinate> createHitCoordinatesFromJson(JsonArray hitsObj)
    {
        if (hitsObj == null) {
            throw new JsonException("Missing " + GameLogicJsonDefinitions.HITS_TAG + " field.");
        }

        ArrayList<Coordinate> hits = new ArrayList<>();
        for (int i = 0; i < hitsObj.size(); ++i) {
            hits.add(createCoordinateFromJson(hitsObj.getJsonObject(i)));
        }
        return hits;
    }

    private Coordinate createCoordinateFromJson(JsonObject obj)
    {
        try {
            Integer x = obj.getInt(GameLogicJsonDefinitions.X_TAG);
            Integer y = obj.getInt(GameLogicJsonDefinitions.Y_TAG);
            return new Coordinate(x, y);
        }
        catch (NullPointerException | ClassCastException e) {
            throw new JsonException("Invalid coordinate field: " + e.getMessage());
        }
    }

    private List<List<ISquare>> createSquares(int width, int height)
    {
        List<List<ISquare>> squares = new ArrayList<>();
        for (int x = 0; x < width; ++x) {
            List<ISquare> column = new ArrayList<>();
            for (int y = 0; y < height; ++y) {
                column.add(new Square(new Coordinate(x,y)));
            }
            squares.add(column);
        }
        return squares;
    }

    private void printError(String extraInfo)
    {
        System.err.println("Failed to read game logic from JSON: " + extraInfo);
    }
}
