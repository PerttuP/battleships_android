package com.testing.paartech.aistudio;

import org.hupisoft.battleships_ai.IBattleShipsAI;
import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.Player;

/**
 * Handles interaction with battleships core game logic and AIs.
 */
class GameRunner {

    private IGameLogic mLogic;
    private IBattleShipsAI mAi1;
    private IBattleShipsAI mAi2;

    /**
     * Constructor.
     * @param logic Game logic.
     * @param ai1 Player 1 AI.
     * @param ai2 Player 2 AI.
     */
    GameRunner(IGameLogic logic, IBattleShipsAI ai1, IBattleShipsAI ai2) {
        mLogic = logic;
        mAi1 = ai1;
        mAi2 = ai2;
        mAi1.setTargetGameArea(logic.getGameArea(Player.PLAYER_2).getRestrictedInstance());
        mAi2.setTargetGameArea(logic.getGameArea(Player.PLAYER_1).getRestrictedInstance());
    }

    /**
     * Execute current player's action.
     */
    void progressOneStep() {
        IBattleShipsAI currentAI = mAi1;
        if (mLogic.getCurrentPlayer() == Player.PLAYER_2) {
            currentAI = mAi2;
        }

        Coordinate target = currentAI.makeDecision();
        HitResult result = mLogic.playerAction(target);
        currentAI.confirmDecision(target, result);
    }
}
