package org.hupisoft.battleships_core;

import java.util.List;

/**
 * Interface for one player's own game area.
 */
public interface IGameArea {

    /**
     * Get area width.
     * @return Width in squares.
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
    public List<IShip> getShips();

    /**
     *
     * @param location Location to be hit.
     * @return Hit result. Null if there is no square at the location.
     */
    HitResult hit(Coordinate location);

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
    List<Coordinate> getNonHitLocations();

    /**
     * Get restricted area.
     * @return Restricted game area instance matching this area.
     */
    IRestrictedGameArea getRestrictedInstance();

    /**
     * Get area logger.
     * @return Area logger. Return null if logger is not set.
     */
    IGameAreaLogger getLogger();
}
