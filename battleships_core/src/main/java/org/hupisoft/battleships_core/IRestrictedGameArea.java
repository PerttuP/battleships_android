package org.hupisoft.battleships_core;

import java.util.List;

/**
 * Provides restricted access to game area.
 * This interface provides information for AI players without possibility to modify area or
 * peek undiscovered ship locations.
 */
public interface IRestrictedGameArea {

    /**
     * @return Area width.
     */
    int width();

    /**
     * @return Area height.
     */
    int height();

    /**
     * Get ships that have been totally destroyed.
     * @return List of completely destroyed ships.
     */
    List<IShip> destroyedShips();

    /**
     * Get number of ships that have not been destroyed.
     * @return Number of remaining ships.
     */
    int remainingShipCount();

    /**
     * Get lengths of remaining ships.
     * @return Lengths in list.
     */
    List<Integer> remainingShipLengths();

    /**
     * @return Number of hits on game area.
     */
    int hitCount();

    /**
     * Get locations that have not been hit.
     * @return List of coordinates of non-hit locations.
     */
    List<Coordinate> getNonHitLocations();

    /**
     * Check if given location has already been hit.
     * @param location Target location.
     * @return True, if target square has been hit.
     */
    boolean isHit(Coordinate location);
}
