package org.hupisoft.battleships;

import org.hupisoft.battleships_ai.IBattleShipsAI;
import org.hupisoft.battleships_core.IGameLogic;

import java.io.File;

/**
 * Interface for accessing game data across whole application.
 */
public interface IGameManager {

    /**
     * Current game type.
     */
    enum GameType {
        /**
         * 2 human players.
         */
        VersusGame,

        /**
         * Human player vs AI.
         */
        SinglePlayerGame
    }

    /**
     * Get current game type.
     * @return Current game type or null, if there is no current game.
     */
    GameType currentGameType();

    /**
     * Get access to current game logic.
     * @return Current game logic, or null, if there is not current game.
     */
    IGameLogic currentGameLogic();

    /**
     * Create new versus game.
     */
    void newVersusGame();

    /**
     *
     * @param aiName Name of AI to be used.
     * @return True, if named AI is supported.
     */
    boolean newSinglePlayerGame(String aiName);

    /**
     * Get current game AI.
     * @return Current AI or null, if there is no current game or game type is VersusGame.
     */
    IBattleShipsAI AIPlayer();

    /**
     * Reset current game. There will no longer be an active game.
     */
    void resetCurrentGame();

    /**
     * Save game to a file in app's internal storage.
     * @return True on success.
     */
    boolean saveGame();

    /**
     * Load game from a file in app's internal storage.
     * @return True on success.
     */
    boolean loadGame();
}
