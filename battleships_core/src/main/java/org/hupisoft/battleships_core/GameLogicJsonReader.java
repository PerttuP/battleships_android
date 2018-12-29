package org.hupisoft.battleships_core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class GameLogicJsonReader {

    public IGameLogic readJsonString(String jsonString)
    {
        GameLogic logic = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            JSONObject gameLogicObj = jsonObject.getJSONObject(GameLogicJsonDefinitions.GAME_LOGIC_TAG);
            JSONObject gameArea1Obj = gameLogicObj.getJSONObject(GameLogicJsonDefinitions.PLAYER1_GAME_AREA_TAG);
            JSONObject gameArea2Obj = gameLogicObj.getJSONObject(GameLogicJsonDefinitions.PLAYER2_GAME_AREA_TAG);

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
                JSONException jsonException = new JSONException("Invalid " +
                                                                GameLogicJsonDefinitions.CURRENT_PLAYER_TAG +
                                                                " field: " + e.getMessage());
                throw jsonException;
            }

            logic = new GameLogic(setup1, setup2, currentPlayer);
        }
        catch (JSONException e)
        {
            printError(e.getMessage());
        }

        return logic;
    }



    private GameLogic.PlayerGameSetup createPlayerSetupFromJson(JSONObject jsonObject)
    {
        Integer areaWidth = jsonObject.getInt(GameLogicJsonDefinitions.WIDTH_TAG);
        Integer areaHeight = jsonObject.getInt(GameLogicJsonDefinitions.HEIGHT_TAG);
        List<List<ISquare>> squares = createSquares(areaWidth, areaHeight);

        JSONArray shipsObj = jsonObject.getJSONArray(GameLogicJsonDefinitions.SHIPS_TAG);
        ArrayList<IShip> ships = createShipsFromJson(shipsObj);
        GameAreaBuilder.setShipOccupations(ships, squares);

        GameArea area = new GameArea(squares, ships, new GameAreaLogger());

        JSONArray hitsObj = jsonObject.getJSONArray(GameLogicJsonDefinitions.HITS_TAG);
        ArrayList<Coordinate> hits = createHitCoordinatesFromJson(hitsObj);
        for (Coordinate c : hits) {
            area.hit(c);
        }

        GameLogic.PlayerGameSetup setup = new GameLogic.PlayerGameSetup();
        setup.area = area;
        setup.numberOfHits = 0; // Is assigned based on opponents game area hits.
        return setup;
    }

    private ArrayList<IShip> createShipsFromJson(JSONArray shipsObj)
    {
        ArrayList<IShip> ships = new ArrayList<>();
        for (int i=0; i < shipsObj.length(); ++i) {
            JSONObject obj = shipsObj.getJSONObject(i);
            ships.add(createShipFromJson(obj));
        }
        return ships;
    }

    private IShip createShipFromJson(JSONObject obj)
    {
        try {
            Integer length = obj.getInt(GameLogicJsonDefinitions.LENGTH_TAG);
            String orientationStr = obj.getString(GameLogicJsonDefinitions.ORIENTATION_TAG);
            IShip.Orientation orientation = IShip.Orientation.valueOf(orientationStr);
            JSONObject bowObj = obj.getJSONObject(GameLogicJsonDefinitions.BOW_TAG);
            Coordinate bowCoordinate = createCoordinateFromJson(bowObj);
            return new Ship(length, bowCoordinate, orientation);
        }
        catch (IllegalArgumentException e) {
            throw new JSONException("Invalid ship orientation: " + e.getMessage());
        }
    }

    private ArrayList<Coordinate> createHitCoordinatesFromJson(JSONArray hitsObj)
    {
        ArrayList<Coordinate> hits = new ArrayList<>();
        for (int i = 0; i < hitsObj.length(); ++i) {
            hits.add(createCoordinateFromJson(hitsObj.getJSONObject(i)));
        }
        return hits;
    }

    private Coordinate createCoordinateFromJson(JSONObject obj)
    {
        Integer x = obj.getInt(GameLogicJsonDefinitions.X_TAG);
        Integer y = obj.getInt(GameLogicJsonDefinitions.Y_TAG);
        return new Coordinate(x, y);
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
