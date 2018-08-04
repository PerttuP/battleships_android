package org.hupisoft.battleships_core.gamelogicwriter;

import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.Player;

/**
 * Implements the IGameLogicWriter interface.
 * @see IGameLogicWriter
 */
public class GameLogicWriter implements IGameLogicWriter {

    private static final String TEMPLATE = "GameLogic{player:%s, area1:%s, area2:%s}";

    @Override
    public String logicToString(IGameLogic gameLogic) {
        Player currentPlayer = gameLogic.getCurrentPlayer();
        IGameArea area1 = gameLogic.getGameArea(Player.PLAYER_1);
        IGameArea area2 = gameLogic.getGameArea(Player.PLAYER_2);
        GameAreaWriter areaWriter = new GameAreaWriter();

        return String.format(TEMPLATE,
                currentPlayer, areaWriter.gameAreaToString(area1), areaWriter.gameAreaToString(area2));
    }
}
