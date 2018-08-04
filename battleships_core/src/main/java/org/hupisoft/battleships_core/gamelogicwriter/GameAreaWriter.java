package org.hupisoft.battleships_core.gamelogicwriter;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.IShip;
import org.hupisoft.battleships_core.ISquare;

import java.util.ArrayList;

/**
 * Creates string representation of game area state.
 */
class GameAreaWriter {

    private static final String TEMPLATE = "GameArea{width:%d, height:%d, ships:[%s], hits:[%s]}";

    /**
     * Creates string representation of game area state.
     * @param area Game area.
     * @return String representation.
     */
    public String gameAreaToString(IGameArea area){
       int widht = area.width();
       int height = area.height();
       ArrayList<IShip> ships = area.getShips();

       ArrayList<Coordinate> hitLocations = new ArrayList<>();
       for (int x = 0; x < widht; ++x) {
           for (int y = 0; y < height; ++y) {
               Coordinate c = new Coordinate(x,y);
               ISquare sqr = area.getSquare(c);
               if (sqr.isHit()) {
                   hitLocations.add(c);
               }
           }
       }

       return String.format(TEMPLATE, widht, height, getShipStrings(ships), getHitString(hitLocations));
    }

    private String getShipStrings(ArrayList<IShip> ships) {
        StringBuilder str = new StringBuilder();
        ShipWriter writer = new ShipWriter();
        for (int i = 0; i < ships.size(); ++i) {
            if (i > 0) {
                str.append(", ");
            }
            str.append(writer.shipToString(ships.get(i)));
        }
        return str.toString();
    }

    private String getHitString(ArrayList<Coordinate> hits) {
        StringBuilder str = new StringBuilder();
        CoordinateWriter writer = new CoordinateWriter();
        for (int i = 0; i < hits.size(); ++i) {
            if (i > 0) {
                str.append(", ");
            }
            str.append(writer.coordinateToString(hits.get(i)));
        }
        return str.toString();
    }
}
