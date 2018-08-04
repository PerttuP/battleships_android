package org.hupisoft.battleships_core.gamelogicwriter;

import org.hupisoft.battleships_core.IGameLogic;

/**
 * Interface for writing IGameLogic instances to string.
 * Can be used to save game state.
 */
public interface IGameLogicWriter {

    /**
     * Get game logic state as a string.
     * @param gameLogic Game logic instance to be written.
     * @return String representation of game logic state.
     */
    public String logicToString(IGameLogic gameLogic);
}
