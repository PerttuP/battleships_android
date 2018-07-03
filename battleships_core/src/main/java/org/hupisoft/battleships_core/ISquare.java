package org.hupisoft.battleships_core;

public interface ISquare {

    /**
     * Possible results for hitting a square.
     */
    public enum HitResult {
        EMPTY,
        HIT_SHIP,
        DESTROYED_SHIP
    }

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
