package org.hupisoft.battleships;

import org.hupisoft.battleships_ai.BattleShipAIFactory;
import org.hupisoft.battleships_ai.IBattleShipsAI;
import org.hupisoft.battleships_core.IGameLogic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Unit tests for GameManager
 */
public class GameManagerTest {

    private IGameDataStorage mStorage;

    @Before
    public void setUp() {
        mStorage = mock(IGameDataStorage.class);
        GameDataStorageProvider.setStorage(mStorage);
    }

    @After
    public void tearDown() {
        GameDataStorageProvider.reset();
    }

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

    @Test
    public void saveGameDoesNothingIfActiveGameDoesNotExist() {
        GameManager manager = new GameManager();
        assertTrue(manager.saveGame());
        verifyNoMoreInteractions(mStorage);
    }

    @Test
    public void saveVersusGameSuccessfully() {
        GameManager manager = new GameManager();
        when(mStorage.saveGameData(eq(manager), any(IGameDataStorage.GameData.class)))
                .thenReturn(true);

        manager.newVersusGame();
        assertTrue(manager.saveGame());

        ArgumentCaptor<IGameDataStorage.GameData> data = ArgumentCaptor.forClass(GameDataStorage.GameData.class);
        verify(mStorage).saveGameData(eq(manager), data.capture());
        assertEquals(manager.currentGameLogic(), data.getValue().gameLogic);
        assertEquals(manager.currentGameType(), data.getValue().gameType);
        assertNull(data.getValue().gameAi);
        verifyNoMoreInteractions(mStorage);
    }

    @Test
    public void saveVersusGameFails() {
        GameManager manager = new GameManager();
        when(mStorage.saveGameData(eq(manager), any(IGameDataStorage.GameData.class)))
                .thenReturn(false);

        manager.newVersusGame();
        assertFalse(manager.saveGame());

        ArgumentCaptor<IGameDataStorage.GameData> data = ArgumentCaptor.forClass(GameDataStorage.GameData.class);
        verify(mStorage).saveGameData(eq(manager), data.capture());
        assertEquals(manager.currentGameLogic(), data.getValue().gameLogic);
        assertEquals(manager.currentGameType(), data.getValue().gameType);
        assertNull(data.getValue().gameAi);
        verifyNoMoreInteractions(mStorage);
    }

    @Test
    public void saveSinglePlayerGameSuccessfully() {
        GameManager manager = new GameManager();
        when(mStorage.saveGameData(eq(manager), any(IGameDataStorage.GameData.class)))
                .thenReturn(true);

        manager.newSinglePlayerGame(BattleShipAIFactory.PROBABILITY_AI);
        assertTrue(manager.saveGame());

        ArgumentCaptor<IGameDataStorage.GameData> data = ArgumentCaptor.forClass(GameDataStorage.GameData.class);
        verify(mStorage).saveGameData(eq(manager), data.capture());
        assertEquals(manager.currentGameLogic(), data.getValue().gameLogic);
        assertEquals(manager.currentGameType(), data.getValue().gameType);
        assertEquals(manager.AIPlayer(), data.getValue().gameAi);
        verifyNoMoreInteractions(mStorage);
    }

    @Test
    public void saveSinglePlayerGameFails() {
        GameManager manager = new GameManager();
        when(mStorage.saveGameData(eq(manager), any(IGameDataStorage.GameData.class)))
                .thenReturn(false);

        manager.newSinglePlayerGame(BattleShipAIFactory.PROBABILITY_AI);
        assertFalse(manager.saveGame());

        ArgumentCaptor<IGameDataStorage.GameData> data = ArgumentCaptor.forClass(GameDataStorage.GameData.class);
        verify(mStorage).saveGameData(eq(manager), data.capture());
        assertEquals(manager.currentGameLogic(), data.getValue().gameLogic);
        assertEquals(manager.currentGameType(), data.getValue().gameType);
        assertEquals(manager.AIPlayer(), data.getValue().gameAi);
        verifyNoMoreInteractions(mStorage);
    }

    @Test
    public void loadGameSucceeds() {
        GameManager manager = new GameManager();
        IGameDataStorage.GameData data = new IGameDataStorage.GameData(
                mock(IGameLogic.class),
                IGameManager.GameType.SinglePlayerGame,
                mock(IBattleShipsAI.class)
        );
        when(mStorage.loadGameData(manager)).thenReturn(data);

        assertTrue(manager.loadGame());
        assertEquals(data.gameAi, manager.AIPlayer());
        assertEquals(data.gameType, manager.currentGameType());
        assertEquals(data.gameLogic, manager.currentGameLogic());

        verify(mStorage).loadGameData(manager);
        verifyNoMoreInteractions(mStorage);
    }

    @Test
    public void loadGameFails() {
        GameManager manager = new GameManager();
        when(mStorage.loadGameData(manager)).thenReturn(null);
        manager.newVersusGame();
        assertFalse(manager.loadGame());
        assertNull(manager.AIPlayer());
        assertNull(manager.currentGameType());
        assertNull(manager.currentGameLogic());

        verify(mStorage).loadGameData(manager);
        verifyNoMoreInteractions(mStorage);
    }
}