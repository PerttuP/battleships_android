package org.hupisoft.battleships_core;

/**
 * Interface for logging actions performed on a single game area.
 */
public interface IGameAreaLogger {

    /**
     * Action (hit) performed on game area.
     */
    class Action {

        private final Coordinate mLocation;
        private final HitResult mResult;

        /**
         * Constructor.
         * @param location Hit location.
         * @param result Hit result.
         */
        Action(Coordinate location, HitResult result) {
            this.mLocation = location;
            this.mResult = result;
        }

        /**
         * @return Hit location.
         */
        Coordinate location() {
            return mLocation;
        }

        /**
         * @return Hit result.
         */
        HitResult result() {
            return mResult;
        }
    }

    /**
     * Number of actions (hits) that have been performed on game area.
     * @return
     */
    int numberOfPerformedActions();

    /**
     * Get latest performed action.
     * @return Latest performed action.
     */
    Action getLatestAction();

    /**
     * Get an action at specific time in history.
     * @param numberOfPriorActions Number of actions performed before fetched action (0 = get first).
     * @return Action in specified step or null, if no such action exists.
     */
    Action getAction(int numberOfPriorActions);

    /**
     * Add new action.
     * @param location Hit location.
     * @param result Hit result.
     */
    void recordAction(Coordinate location, HitResult result);
}
