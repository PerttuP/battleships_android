package org.hupisoft.battleships_core;

/**
 * Interface for creating game area for each player.
 */
public interface IGameAreaBuilder {

    /**
     * Generates random game area. None of the squares are hit and all ships are intact.
     * NOTE: Function may get stuck if area is not big enough for relialibly placing ships at random.
     *
     * @param width Area width
     * @param height Area height
     * @param shipLengths Ship lengths. Array size indicates number of ships and elements the
     *                    length for each ship.
     * @return Randomly generated game area without any pre-existing hits.
     */
    public IGameArea createInitialGameArea(int width, int height, int[] shipLengths);
}
