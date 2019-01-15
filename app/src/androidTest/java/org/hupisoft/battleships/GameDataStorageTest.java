package org.hupisoft.battleships;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.hupisoft.battleships_ai.BattleShipAIFactory;
import org.hupisoft.battleships_ai.IBattleShipsAI;
import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.GameLogicBuilder;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.IGameAreaLogger;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.IShip;
import org.hupisoft.battleships_core.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class GameDataStorageTest {

    private GameDataStorage mStorage;
    private Context mContext;

    private void playSinglePlayerMoves(IGameLogic logic,
                                       IBattleShipsAI ai,
                                       Coordinate[] humanMoves) {
        for (Coordinate humanMove : humanMoves) {
            logic.playerAction(humanMove);
            Coordinate aiMove = ai.makeDecision();
            HitResult r = logic.playerAction(aiMove);
            ai.confirmDecision(aiMove, r);
        }
    }

    private void compareData(IGameDataStorage.GameData originalData, IGameDataStorage.GameData newData) {
        compareLogic(originalData.gameLogic, newData.gameLogic);
        assertEquals(originalData.gameType, newData.gameType);
        compareAIs(originalData.gameAi, newData.gameAi);
    }

    private void compareLogic(IGameLogic originalLogic, IGameLogic newLogic) {
        assertNotNull(newLogic);
        assertEquals(originalLogic.getCurrentPlayer(), newLogic.getCurrentPlayer());
        assertEquals(originalLogic.getNumberOfHits(Player.PLAYER_1), newLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(originalLogic.getNumberOfHits(Player.PLAYER_2), newLogic.getNumberOfHits(Player.PLAYER_2));
        assertEquals(originalLogic.getWinner(), newLogic.getWinner());
        assertEquals(originalLogic.isGameOver(), newLogic.isGameOver());
        compareAreas(originalLogic.getGameArea(Player.PLAYER_1), newLogic.getGameArea(Player.PLAYER_1));
        compareAreas(originalLogic.getGameArea(Player.PLAYER_2), newLogic.getGameArea(Player.PLAYER_2));
    }

    private void compareAreas(IGameArea originalArea, IGameArea newArea) {
        assertEquals(originalArea.width(), newArea.width());
        assertEquals(originalArea.height(), newArea.height());
        assertEquals(originalArea.hitCount(), newArea.hitCount());
        assertEquals(originalArea.remainingShipCount(), newArea.remainingShipCount());
        assertEquals(originalArea.getNonHitLocations().size(), newArea.getNonHitLocations().size());

        // Compare ships.
        List<IShip> originalShips = originalArea.getShips();
        List<IShip> newShips = newArea.getShips();
        assertEquals(originalShips.size(), newShips.size());
        for (int i = 0; i < originalShips.size(); ++i) {
            IShip expectedShip = originalShips.get(i);
            IShip actualShip = newShips.get(i);
            assertEquals(expectedShip.getBowCoordinates(), actualShip.getBowCoordinates());
            assertEquals(expectedShip.length(), actualShip.length());
            assertEquals(expectedShip.orientation(), actualShip.orientation());
            assertEquals(expectedShip.hitCount(), actualShip.hitCount());
        }

        // Compare logged actions.
        IGameAreaLogger originalLogger = originalArea.getLogger();
        IGameAreaLogger newLogger = newArea.getLogger();
        assertEquals(originalLogger.numberOfPerformedActions(), newLogger.numberOfPerformedActions());
        for (int i = 0; i < originalLogger.numberOfPerformedActions(); ++i) {
            IGameAreaLogger.Action act1 = originalLogger.getAction(i);
            IGameAreaLogger.Action act2 = newLogger.getAction(i);
            assertEquals(act1.location(), act2.location());
            assertEquals(act1.result(), act2.result());
        }
    }

    private void compareAIs(IBattleShipsAI originalAI, IBattleShipsAI newAI) {
        if (originalAI != null) {
            assertNotNull(newAI);
            assertEquals(originalAI.id(), newAI.id());
            // Restored AI should make same decision as original.
            assertEquals(originalAI.makeDecision(), newAI.makeDecision());
        }
        else {
            assertNull(newAI);
        }
    }

    @Before
    public void setUp() {
        mStorage = new GameDataStorage();
        mContext = InstrumentationRegistry.getTargetContext().getApplicationContext();
        tearDown();
    }

    @After
    public void tearDown() {
        deleteLocalFile(GameDataStorage.GAME_LOGIC_FILE);
        deleteLocalFile(GameDataStorage.GAME_TYPE_FILE);
        deleteLocalFile(GameDataStorage.GAME_AI_FILE);
    }

    private void deleteLocalFile(String fileName) {
        File f = new File(mContext.getFilesDir(), fileName);
        f.delete();
    }

    @Test
    public void constructorWithNoParametersCreatesEmptyGameData() {
        IGameDataStorage.GameData data = new GameDataStorage.GameData();
        assertNull(data.gameAi);
        assertNull(data.gameLogic);
        assertNull(data.gameType);
    }

    @Test
    public void constructorWithParametersCreatesPopulatedGameData() {
        IGameLogic logic = new GameLogicBuilder().createNewGame(12, 8, new int[]{2,3,5});
        IBattleShipsAI ai = new BattleShipAIFactory().createAI(BattleShipAIFactory.RANDOM_AI);
        IGameManager.GameType type = IGameManager.GameType.SinglePlayerGame;
        IGameDataStorage.GameData data = new IGameDataStorage.GameData(logic, type, ai);
        assertEquals(logic, data.gameLogic);
        assertEquals(type, data.gameType);
        assertEquals(ai, data.gameAi);
    }

    @Test
    public void loadGameReturnsEmptyDataIfNoSavedGame() {
        GameDataStorage.GameData data = mStorage.loadGameData(mContext);
        assertNotNull(data);
        assertNull(data.gameAi);
        assertNull(data.gameLogic);
        assertNull(data.gameType);
    }

    // TODO: THIS TEST DOES NOT PASS YET.
    @Test
    public void saveAndLoadSinglePlayerGame() {
        // Create original data.
        IGameLogic originalLogic = new GameLogicBuilder().createNewGame(12, 8, new int[]{2,3,5});
        IBattleShipsAI originalAi = new BattleShipAIFactory().createAI(BattleShipAIFactory.PROBABILITY_AI);
        originalAi.setTargetGameArea(originalLogic.getGameArea(Player.PLAYER_1).getRestrictedInstance());
        IGameManager.GameType originalType = IGameManager.GameType.SinglePlayerGame;

        // Play few moves.
        Coordinate[] moves = new Coordinate[] {
                 new Coordinate(1,2), new Coordinate(3,4), new Coordinate(5,6)
        };
        playSinglePlayerMoves(originalLogic, originalAi, moves);

        // Save data.
        IGameDataStorage.GameData originalData = new IGameDataStorage.GameData(originalLogic, originalType, originalAi);
        assertTrue(mStorage.saveGameData(mContext, originalData));

        // Load data
        IGameDataStorage.GameData newData = mStorage.loadGameData(mContext);
        compareData(originalData, newData);
    }

    @Test
    public void saveAndLoadVersusGame() {
        // Create original data.
        IGameLogic originalLogic = new GameLogicBuilder().createNewGame(12, 8, new int[]{2,3,5});
        IGameManager.GameType type = GameManager.GameType.VersusGame;
        IGameDataStorage.GameData originalData = new IGameDataStorage.GameData(originalLogic, type, null);

        // Play few moves
        originalLogic.playerAction(new Coordinate(1,2));
        originalLogic.playerAction(new Coordinate(5,6));
        originalLogic.playerAction(new Coordinate(3,4));
        originalLogic.playerAction(new Coordinate(6,7));
        originalLogic.playerAction(new Coordinate(2,3));
        originalLogic.playerAction(new Coordinate(11,7));
        originalLogic.playerAction(new Coordinate(4,5));

        assertTrue(mStorage.saveGameData(mContext, originalData));
        IGameDataStorage.GameData newData = mStorage.loadGameData(mContext);
        compareData(originalData, newData);

    }

}