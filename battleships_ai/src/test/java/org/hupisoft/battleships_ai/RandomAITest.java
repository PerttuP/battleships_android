package org.hupisoft.battleships_ai;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.GameLogicBuilder;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.IRestrictedGameArea;
import org.hupisoft.battleships_core.Player;
import org.junit.Test;
import org.mockito.internal.verification.Times;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the RandomAI class.
 */
public class RandomAITest {

    @Test
    public void getId() {
        IBattleShipsAI ai = new BattleShipAIFactory().createAI(BattleShipAIFactory.RANDOM_AI);
        assertEquals(BattleShipAIFactory.RANDOM_AI, ai.id());
    }

    @Test
    public void AIAlwaysSelectsRandomFromNonHitLocations()
    {
        // Set up non hit locations.
        ArrayList<Coordinate> remaining = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            remaining.add(new Coordinate(i,i+1));
        }
        IRestrictedGameArea mockArea = mock(IRestrictedGameArea.class);
        Random mockRng = mock(Random.class);

        RandomAI ai = new RandomAI(mockRng);
        ai.setTargetGameArea(mockArea);
        // Emulate game
        for (int i=0; i<10; ++i) {
            when(mockArea.getNonHitLocations()).thenReturn(remaining);
            when(mockRng.nextInt(10-i)).thenReturn(9-i);

            Coordinate target = ai.makeDecision();
            verify(mockRng).nextInt(10-i);

            assertEquals(target, remaining.get(9-i));
            remaining.remove(9-i);
            ai.confirmDecision(target, HitResult.EMPTY);
        }

        verify(mockArea, times(10)).getNonHitLocations();
    }

    @Test
    public void aiReturnsNullIfNonHitLocationsAreNotAvailable()
    {
        ArrayList<Coordinate> empty = new ArrayList<>();
        IRestrictedGameArea mockArea = mock(IRestrictedGameArea.class);
        when(mockArea.getNonHitLocations()).thenReturn(empty);
        Random mockRng = mock(Random.class);

        RandomAI ai = new RandomAI(mockRng);
        ai.setTargetGameArea(mockArea);
        assertNull(ai.makeDecision());

        verify(mockArea).getNonHitLocations();
        verifyNoMoreInteractions(mockArea);
        verifyNoMoreInteractions(mockRng);
    }

    @Test
    public void AICanWinRealGame()
    {
        IGameLogic logic = new GameLogicBuilder().createNewGame(12,8, new int[]{2,3,5});
        RandomAI ai1 = new RandomAI(new Random());
        RandomAI ai2 = new RandomAI(new Random());
        ai1.setTargetGameArea(logic.getGameArea(Player.PLAYER_2).getRestrictedInstance());
        ai2.setTargetGameArea(logic.getGameArea(Player.PLAYER_1).getRestrictedInstance());

        HitResult hitResult = null;
        while (hitResult != HitResult.VICTORY) {
            RandomAI currentAI = null;
            if (logic.getCurrentPlayer() == Player.PLAYER_1) {
                currentAI = ai1;
            } else {
                currentAI = ai2;
            }

            Coordinate target = currentAI.makeDecision();
            assertNotNull(target);
            hitResult = logic.playerAction(target);
            assertNotEquals(HitResult.ALREADY_HIT, hitResult);
            assertNotEquals(HitResult.GAME_HAS_ENDED, hitResult);
            currentAI.confirmDecision(target, hitResult);
        }

        assertNotNull(logic.getWinner());
        String message = String.format("Random game ended! Winner: %s, number of hits: %d",
                logic.getWinner().toString(), logic.getNumberOfHits(logic.getWinner()));
        System.out.println(message);
    }
}