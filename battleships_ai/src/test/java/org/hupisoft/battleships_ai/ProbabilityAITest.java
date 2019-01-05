package org.hupisoft.battleships_ai;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.GameLogicBuilder;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.Player;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProbabilityAITest {

    @Test
    public void verifyAiId() {
        BattleShipAIFactory factory = new BattleShipAIFactory();
        IBattleShipsAI ai = factory.createAI(BattleShipAIFactory.PROBABILITY_AI);
        ProbabilityAI pai = (ProbabilityAI)ai;
        assertNotNull(pai);
        assertEquals(BattleShipAIFactory.PROBABILITY_AI, ai.id());
    }

    @Test
    public void ProbabilityAICanWin() {
        IGameLogic logic = new GameLogicBuilder().createNewGame(12,8, new int[]{2,3,5});
        BattleShipAIFactory factory = new BattleShipAIFactory();
        IBattleShipsAI ai1 = factory.createAI(BattleShipAIFactory.PROBABILITY_AI);
        IBattleShipsAI ai2 = factory.createAI(BattleShipAIFactory.PROBABILITY_AI);

        ai1.setTargetGameArea(logic.getGameArea(Player.PLAYER_2).getRestrictedInstance());
        ai2.setTargetGameArea(logic.getGameArea(Player.PLAYER_1).getRestrictedInstance());

        while (!logic.isGameOver())
        {
            IBattleShipsAI currentAI = null;
            if (logic.getCurrentPlayer() == Player.PLAYER_1) {
                currentAI = ai1;
            } else {
                currentAI = ai2;
            }

            Coordinate target = currentAI.makeDecision();
            assertNotNull(target);
            HitResult hitResult = logic.playerAction(target);
            assertNotEquals(HitResult.ALREADY_HIT, hitResult);
            assertNotEquals(HitResult.GAME_HAS_ENDED, hitResult);
            currentAI.confirmDecision(target, hitResult);
        }

        assertNotNull(logic.getWinner());
        String message = String.format("ProbabilityAI game ended! Winner: %s, number of hits: %d",
                logic.getWinner().toString(), logic.getNumberOfHits(logic.getWinner()));
        System.out.println(message);
    }
}