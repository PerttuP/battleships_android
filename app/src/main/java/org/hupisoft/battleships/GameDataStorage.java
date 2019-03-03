package org.hupisoft.battleships;

import android.app.Application;
import android.content.Context;

import org.hupisoft.battleships_ai.BattleShipAIFactory;
import org.hupisoft.battleships_ai.IBattleShipsAI;
import org.hupisoft.battleships_core.GameLogicBuilderProvider;
import org.hupisoft.battleships_core.GameLogicJsonReader;
import org.hupisoft.battleships_core.GameLogicJsonWriter;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.IGameAreaBuilder;
import org.hupisoft.battleships_core.IGameAreaLogger;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * Implements IGameDataStorage.
 */
class GameDataStorage implements IGameDataStorage {

    static String GAME_LOGIC_FILE = "GameLogicSave";
    static String GAME_TYPE_FILE = "GameTypeSave";
    static String GAME_AI_FILE = "GameAiSave";

    @Override
    public boolean saveGameData(Context ctx, GameData data) {
        boolean success = true;

        try {
            GameLogicJsonWriter writer = new GameLogicJsonWriter();
            String logicStr = writer.writeJsonString(data.gameLogic);
            saveToFile(logicStr, ctx, GAME_LOGIC_FILE);

            saveToFile(data.gameType.toString(), ctx, GAME_TYPE_FILE);

            String aiType = data.gameAi == null ? "" : data.gameAi.id();
            saveToFile(aiType, ctx, GAME_AI_FILE);
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    private void saveToFile(String data, Context ctx, String fileName) throws Exception {
        FileOutputStream outStream = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
        outStream.write(data.getBytes());
        outStream.close();
    }

    @Override
    public GameData loadGameData(Context ctx) {
        GameData data = null;

        try {
            IGameLogic logic = readGameLogic(ctx);
            IGameManager.GameType type = readGameType(ctx);
            IBattleShipsAI ai = readAi(ctx);
            if (ai != null) {
                restoreAiState(ai, logic.getGameArea(Player.PLAYER_1));
            }
            data = new GameData(logic, type, ai);
        }
        catch (FileNotFoundException e) {
            // No saved game.
            data = new GameData();
        }
        catch (Exception e) {
            System.err.print(e.getStackTrace().toString());
        }

        return data;
    }

    private IGameLogic readGameLogic(Context ctx) throws Exception {
        String logicStr = readFromFile(ctx, GAME_LOGIC_FILE);
        GameLogicJsonReader reader = new GameLogicJsonReader();
        IGameLogic logic = reader.readJsonString(logicStr);

        if (logic == null) {
            throw new Exception("Could not read GameLogic JSON.");
        }
        return logic;
    }

    private IGameManager.GameType readGameType(Context ctx) throws Exception {
        String typeStr = readFromFile(ctx, GAME_TYPE_FILE);
        return IGameManager.GameType.valueOf(typeStr);
    }

    private IBattleShipsAI readAi(Context ctx) throws Exception {
        String aiType = readFromFile(ctx, GAME_AI_FILE);
        IBattleShipsAI ai = null;
        if (!aiType.isEmpty()) {
            BattleShipAIFactory factory = new BattleShipAIFactory();
            ai = factory.createAI(aiType);
            if (ai == null) {
                throw new Exception("Unknown AI type " + aiType);
            }
        }
        return ai;
    }

    private void restoreAiState(IBattleShipsAI ai, IGameArea area) {
        IGameAreaBuilder areaBuilder = new GameLogicBuilderProvider()
                .getBuilderInstance().getAreaBuilder();

        IGameArea initialArea = areaBuilder.createSnapshot(area, 0);
        ai.setTargetGameArea(initialArea.getRestrictedInstance());

        // Replay performed actions to AI.
        IGameAreaLogger logger = area.getLogger();
        for (int i = 0; i < logger.numberOfPerformedActions(); ++i) {
            // THIS DOES NOT WORK YET!
            // AI need specific methods to restore old state.
            IGameAreaLogger.Action act = logger.getAction(i);
            initialArea.hit(act.location());
            ai.confirmDecision(act.location(), act.result());
        }

        ai.setTargetGameArea(area.getRestrictedInstance());
    }

    private String readFromFile(Context ctx, String fileName) throws Exception {
        File inFile = new File(ctx.getFilesDir(), fileName);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(inFile));

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();

        return stringBuilder.toString();
    }
}
