package org.hupisoft.battleships_core;


import java.util.ArrayList;

/**
 * Interface for ships.
 */
public interface IShip {

    /**
     * Ship orientation enumeration.
     */
    public enum Orientation
    {
        VERTICAL,
        HORISONTAL
    }

    /**
     * Check if ship has been destroyed.
     * @return True, if ship has been destroyed.
     */
    public boolean isDestroyed();

    /**
     * Get ship orientation.
     * @return Ship orientation.
     */
    public Orientation orientation();

    /**
     * Get ship length.
     * @return Ship length in squares.
     */
    public int length();

    /**
     * Get coordinates of the bow of the ship.
     * @return Coordinate pair for the head of the ship.
     */
    public Coordinate getBowCoordinates();

    /**
     * Get coordinates of the rear of the ship.
     * @return Coordinate pair for the rear of the ship.
     */
    public Coordinate getRearCoordinates();

    /**
     * Get list of coordinates occupied by the ship in order from bow to rear.
     * @return List of coordinates.
     */
    public ArrayList<Coordinate> getOccupiedCoordinates();

    /**
     * Get list of coordinates that are forbidden from other ships.
     * List contains all occupied coordinates and their neighbours.
     * Coordinates are not in any specific order.
     * @return List of restricted coordinates.
     */
    public ArrayList<Coordinate> getRestrictedCoordinates();

    /**
     * Get ship hit count.
     * @return Number of hits that ship has received.
     */
    public int hitCount();

    /**
     * Increase ship's hit counter.
     */
    public boolean hit();
}