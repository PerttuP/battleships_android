package org.hupisoft.battleships_core;

import java.util.Random;

/**
 * Builder class for game logic.
 */
class GameLogicBuilder implements IGameLogicBuilder {

    private IGameAreaBuilder mAreaBuilder = null;

    /**
     * Create game logic builder with default game area builder.
     */
    GameLogicBuilder() {
        this(new GameAreaBuilder(new Random()));
    }

    /**
     * Create game logic builder with custom game area builder.
     * @param areaBuilder Custom game area builder.
     */
    GameLogicBuilder(IGameAreaBuilder areaBuilder) {
        mAreaBuilder = areaBuilder;
    }

    @Override
    public IGameLogic createNewGame(int areaWidth, int areaHeight, int[] shipLengths) {
        GameLogic.PlayerGameSetup setup1 = new GameLogic.PlayerGameSetup();
        setup1.numberOfHits = 0;
        setup1.area = mAreaBuilder.createInitialGameArea(areaWidth, areaHeight, shipLengths);

        GameLogic.PlayerGameSetup setup2 = new GameLogic.PlayerGameSetup();
        setup2.numberOfHits = 0;
        setup2.area = mAreaBuilder.createInitialGameArea(areaWidth, areaHeight, shipLengths);

        return new GameLogic(setup1, setup2, Player.PLAYER_1);
    }

    @Override
    public IGameAreaBuilder getAreaBuilder() {
        return mAreaBuilder;
    }
}
