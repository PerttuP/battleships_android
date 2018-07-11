package org.hupisoft.battleships_core;

import java.util.Random;

public class GameLogicBuilder {

    private IGameAreaBuilder mAreaBuilder = null;

    /**
     * Create gane logic builder with default game area builder.
     */
    public GameLogicBuilder() {
        this(new GameAreaBuilder(new Random()));
    }

    /**
     * Create gane logic builder with custom game area builder.
     * @param areaBuilder Custom game area builder.
     */
    public GameLogicBuilder(IGameAreaBuilder areaBuilder) {
        mAreaBuilder = areaBuilder;
    }

    /**
     * Create new game.
     * @param areaWidth Game area width.
     * @param areaHeight Game area height.
     * @param shipLengths Number of ships and their lengths.
     * @return New untouched game.
     */
    public IGameLogic createNewGame(int areaWidth, int areaHeight, int[] shipLengths) {
        GameLogic.PlayerGameSetup setup1 = new GameLogic.PlayerGameSetup();
        setup1.numberOfHits = 0;
        setup1.area = mAreaBuilder.createInitialGameArea(areaWidth, areaHeight, shipLengths);

        GameLogic.PlayerGameSetup setup2 = new GameLogic.PlayerGameSetup();
        setup2.numberOfHits = 0;
        setup2.area = mAreaBuilder.createInitialGameArea(areaWidth, areaHeight, shipLengths);

        return new GameLogic(setup1, setup2, Player.PLAYER_1);
    }
}
