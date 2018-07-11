package org.hupisoft.battleships_core;

/**
 * Interface for single square in game area.
 */
public interface ISquare {

    /**
     * Check if square has been hit.
     * @return True if square has been hit.
     */
    public boolean isHit();

    /**
     * Hit the square. Will also hit possible ship occupying the square.
     * @return True, if square was occupied by a ship.
     */
    public HitResult hit();

    /**
     * Get ship occupying the square.
     * @return Ship occupying the square or null, if no ship occupy the square.
     */
    public IShip getShip();

    /**
     * Set a ship to occupy this square.
     */
    public void setShip(IShip ship);

    /**
     * Get square coordinates on the game area.
     * @return Coordinate describing the location.
     */
    public Coordinate getLocation();
}
