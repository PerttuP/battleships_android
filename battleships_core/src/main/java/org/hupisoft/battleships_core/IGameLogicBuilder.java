package org.hupisoft.battleships_core;

/**
 * Interface for battleships game logic builder.
 */
public interface IGameLogicBuilder {

    /**
     * Create new game.
     * @param areaWidth Game area width.
     * @param areaHeight Game area height.
     * @param shipLengths List of ship lengths.
     * @return New IGameLogic instance.
     */
    IGameLogic createNewGame(int areaWidth, int areaHeight, int[] shipLengths);

    /**
     * Get used IGameAreaBuilder instance.
     * @return Used game area instance.
     */
    IGameAreaBuilder getAreaBuilder();
}
