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
    IGameArea createInitialGameArea(int width, int height, int[] shipLengths);

    /**
     * Create deep copy of the original game area.
     * @param original Original game area.
     * @return Deep copy of the original area.
     */
    IGameArea createCopy(IGameArea original);

    /**
     * Create snapshot from original game area.
     * @param original Original game area.
     * @param numberOfHits Number of hits that have been performed at the time of snapshot.
     * @return Snapshot from original at point of specified hit count. Null if hit count is out of range.
     */
    IGameArea createSnapshot(IGameArea original, int numberOfHits);
}
