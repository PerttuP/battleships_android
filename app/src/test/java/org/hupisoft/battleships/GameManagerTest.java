package org.hupisoft.battleships;

import org.hupisoft.battleships_ai.BattleShipAIFactory;
import org.hupisoft.battleships_core.IGameLogic;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for GameManager
 */
public class GameManagerTest {

    @Test
    public void gameManagerHasNoCurrentGameByDefault() {
        GameManager manager = new GameManager();
        assertNull(manager.AIPlayer());
        assertNull(manager.currentGameLogic());
        assertNull(manager.currentGameType());
    }

    @Test
    public void newVersusGameCreatesVersusGame() {
        GameManager manager = new GameManager();
        manager.newVersusGame();
        assertNull(manager.AIPlayer());
        assertNotNull(manager.currentGameLogic());
        assertEquals(IGameManager.GameType.VersusGame, manager.currentGameType());
    }

    @Test
    public void newSinglePlayerGameCreatesSinglePlayerGame() {
        List<String> ais = new BattleShipAIFactory().supportedAiTypes();

        for (String ai : ais) {
            GameManager manager = new GameManager();
            assertTrue(manager.newSinglePlayerGame(ai));
            assertNotNull(manager.AIPlayer());
            assertEquals(ai, manager.AIPlayer().id());
            assertNotNull(manager.currentGameLogic());
            assertEquals(IGameManager.GameType.SinglePlayerGame, manager.currentGameType());
        }
    }

    @Test
    public void failedNewSinglePlayerGameResetsGame() {
        GameManager manager = new GameManager();
        manager.newVersusGame();
        assertNotNull(manager.currentGameLogic());
        assertNotNull(manager.currentGameType());

        manager.newSinglePlayerGame("UnknownAI");
        assertNull(manager.currentGameType());
        assertNull(manager.currentGameLogic());
        assertNull(manager.AIPlayer());

    }

    @Test
    public void newVersusGameOverridesPreviousGame() {
        GameManager manager = new GameManager();
        assertTrue(manager.newSinglePlayerGame(BattleShipAIFactory.RANDOM_AI));
        IGameLogic oldLogic = manager.currentGameLogic();
        assertNotNull(oldLogic);
        assertNotNull(manager.AIPlayer());
        assertEquals(IGameManager.GameType.SinglePlayerGame, manager.currentGameType());

        manager.newVersusGame();
        assertEquals(IGameManager.GameType.VersusGame, manager.currentGameType());
        assertNotNull(manager.currentGameLogic());
        assertNotEquals(oldLogic, manager.currentGameLogic());
        assertNull(manager.AIPlayer());
    }

    @Test
    public void newSinglePlayerGameOverridesPreviousGame() {
        GameManager manager = new GameManager();
        manager.newVersusGame();
        assertNotNull(manager.currentGameLogic());
        IGameLogic oldLogic = manager.currentGameLogic();
        assertEquals(IGameManager.GameType.VersusGame, manager.currentGameType());
        assertNull(manager.AIPlayer());

        assertTrue(manager.newSinglePlayerGame(BattleShipAIFactory.PROBABILITY_AI));
        assertNotNull(manager.AIPlayer());
        assertEquals(BattleShipAIFactory.PROBABILITY_AI, manager.AIPlayer().id());
        assertNotNull(manager.currentGameLogic());
        assertNotEquals(oldLogic, manager.currentGameLogic());
        assertEquals(IGameManager.GameType.SinglePlayerGame, manager.currentGameType());
    }

    @Test
    public void resetGameResetsGame() {
        GameManager manager = new GameManager();
        manager.newSinglePlayerGame(BattleShipAIFactory.RANDOM_AI);
        assertNotNull(manager.currentGameLogic());
        assertNotNull(manager.currentGameType());
        assertNotNull(manager.AIPlayer());

        manager.resetCurrentGame();
        assertNull(manager.AIPlayer());
        assertNull(manager.currentGameType());
        assertNull(manager.currentGameLogic());
    }


}