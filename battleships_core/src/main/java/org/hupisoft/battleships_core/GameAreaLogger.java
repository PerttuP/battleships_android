package org.hupisoft.battleships_core;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements IGameAreaLogger.
 */
class GameAreaLogger implements IGameAreaLogger {

    private List<Action> mActions;

    /**
     * Constructor.
     */
    GameAreaLogger() {
        mActions = new ArrayList<>();
    }

    @Override
    public int numberOfPerformedActions() {
        return mActions.size();
    }

    @Override
    public Action getLatestAction() {
        return getAction(numberOfPerformedActions()-1);
    }

    @Override
    public Action getAction(int numberOfPriorActions) {
        try {
            return mActions.get(numberOfPriorActions);
        }
        catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    @Override
    public void recordAction(Coordinate location, HitResult result) {
        mActions.add(new Action(location, result));
    }
}
