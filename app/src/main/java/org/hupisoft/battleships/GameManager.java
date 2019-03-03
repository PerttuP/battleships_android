package org.hupisoft.battleships;

import android.app.Application;

import org.hupisoft.battleships_ai.BattleShipAIFactory;
import org.hupisoft.battleships_ai.IBattleShipsAI;
import org.hupisoft.battleships_core.GameLogicBuilderProvider;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.IGameLogicBuilder;

public class GameManager extends Application implements IGameManager {

    private IGameLogic mCurrentGame = null;
    private GameType mGameType = null;
    private IBattleShipsAI mAIPlayer = null;

    @Override
    public IGameManager.GameType currentGameType() {
        return mGameType;
    }

    @Override
    public IGameLogic currentGameLogic() {
        return mCurrentGame;
    }

    @Override
    public void newVersusGame() {
        resetCurrentGame();
        mCurrentGame = createGameLogic();
        mGameType = GameType.VersusGame;
        mAIPlayer = null;
    }

    @Override
    public boolean newSinglePlayerGame(String aiName) {
        resetCurrentGame();
        mAIPlayer = new BattleShipAIFactory().createAI(aiName);
        if (mAIPlayer != null) {
            mCurrentGame = createGameLogic();
            mGameType = GameType.SinglePlayerGame;
        }
        return mAIPlayer != null;
    }

    @Override
    public IBattleShipsAI AIPlayer() {
        return mAIPlayer;
    }

    @Override
    public void resetCurrentGame() {
        mAIPlayer = null;
        mGameType = null;
        mCurrentGame = null;
    }

    @Override
    public boolean saveGame() {
        boolean success = true;
        if (mCurrentGame != null){
            IGameDataStorage storage = GameDataStorageProvider.getStorage();
            IGameDataStorage.GameData data =
                    new IGameDataStorage.GameData(mCurrentGame, mGameType, mAIPlayer);
            success = storage.saveGameData(this, data);
        }
        return success;
    }

    @Override
    public boolean loadGame() {
        boolean success = false;
        resetCurrentGame();
        IGameDataStorage storage = GameDataStorageProvider.getStorage();
        IGameDataStorage.GameData data = storage.loadGameData(this);
        if (data != null) {
            mCurrentGame = data.gameLogic;
            mGameType = data.gameType;
            mAIPlayer = data.gameAi;
            success = true;
        }
        return success;
    }

    private IGameLogic createGameLogic() {
        IGameLogicBuilder builder = new GameLogicBuilderProvider().getBuilderInstance();
        return builder.createNewGame(12, 8, new int[]{2,3,5});
    }

}
