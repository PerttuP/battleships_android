package org.hupisoft.battleships_core;

import java.util.ArrayList;

public interface IGameArea {

    /**
     * Get area widrh.
     * @return Widrh in squares.
     */
    int width();

    /**
     * Get are height.
     * @return Height in squares.
     */
    int height();

    /**
     * Get square at the location.
     * @param location Location of the square.
     * @return Square in the location or null, if location has no square.
     */
    public ISquare getSquare(Coordinate location);

    /**
     * Get all ships on game area.
     * @return List of all ships. Includes destroyed ships.
     */
    public ArrayList<IShip> getShips();

    /**
     *
     * @param location Location to be hit.
     * @return Hit result. Null if there is no square at the location.
     */
    ISquare.HitResult hit(Coordinate location);

    /**
     * Get number of remaining ships.
     * @return Number of ships that have not been destroyed.
     */
    int remainingShipCount();

    /**
     * Number of times area has been hit.
     * @return Number of hits.
     */
    int hitCount();

    /**
     * Get locations that have not been hit yet.
     * @return List of coordinates for squares that have not been hit.
     */
    ArrayList<Coordinate> getUnHitLocations();
}
