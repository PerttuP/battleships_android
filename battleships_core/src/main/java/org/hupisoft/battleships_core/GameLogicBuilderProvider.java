package org.hupisoft.battleships_core;

/**
 * Provides access to IGameLogicBuilder instance.
 */
public class GameLogicBuilderProvider {

    private IGameLogicBuilder mBuilder = new GameLogicBuilder();

    /**
     * Get IGameLogicBuilder instance.
     * @return Game logic builder.
     */
    public IGameLogicBuilder getBuilderInstance() {
        return mBuilder;
    }

    /**
     * Set builder instance. This method is for testing purposes only.
     * @param builder Builder instance to be returned by getBuilderInstance.
     */
    public void setBuilder(IGameLogicBuilder builder) {
        mBuilder = builder;
    }

    /**
     * Undo effects of setBuilder. This method is for testing purposes only.
     */
    public void reset() {
        mBuilder = new GameLogicBuilder();
    }
}
