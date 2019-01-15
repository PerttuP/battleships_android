package org.hupisoft.battleships;

import android.content.Context;

import org.hupisoft.battleships_ai.IBattleShipsAI;
import org.hupisoft.battleships_core.IGameLogic;

/**
 * Interface for storing game data to persistent memory.
 */
interface IGameDataStorage {

    /**
     * Game data struct.
     */
    class GameData {
        /**
         * Game logic instance.
         */
        final IGameLogic gameLogic;

        /**
         * Game type.
         */
        final IGameManager.GameType gameType;

        /**
         * Game AI. Can be null, if game type is versus game.
         */
        final IBattleShipsAI gameAi;

        /**
         * Constructor.
         * @param logic Game logic.
         * @param type Game type.
         * @param ai Game AI.
         */
        GameData(IGameLogic logic, IGameManager.GameType type, IBattleShipsAI ai) {
            gameLogic = logic;
            gameType = type;
            gameAi = ai;
        }

        /**
         * Create empty game data.
         */
        GameData() {
            this(null, null, null);
        }
    }

    /**
     * Save game data to file.
     * @param ctx Application instance.
     * @param data Game data.
     * @return True on success.
     */
    boolean saveGameData(Context ctx, GameData data);

    /**
     * Load game data to file. Restores game logic and AI completely.
     * @param ctx Application instance.
     * @return True on success.
     */
    GameData loadGameData(Context ctx);
}
