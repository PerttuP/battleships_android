package org.hupisoft.battleships_core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for GameAreaLogger.
 */
public class GameAreaLoggerTest {

    private void compareActions(IGameAreaLogger.Action expected, IGameAreaLogger.Action actual) {
        assertEquals(expected.location(), actual.location());
        assertEquals(expected.result(), actual.result());
    }

    @Test
    public void ActionTest() {
        Coordinate location = new Coordinate(3, 4);
        HitResult result = HitResult.SHIP_HIT;
        IGameAreaLogger.Action action = new IGameAreaLogger.Action(location, result);
        assertEquals(location, action.location());
        assertEquals(result, action.result());
    }

    @Test
    public void loggerIsEmptyAtCreation() {
        GameAreaLogger logger = new GameAreaLogger();
        assertEquals(0, logger.numberOfPerformedActions());
        assertNull(logger.getLatestAction());
        assertNull(logger.getAction(0));
    }

    @Test
    public void newActionsAreAddedToEnd() {
        GameAreaLogger logger = new GameAreaLogger();
        IGameAreaLogger.Action first = new IGameAreaLogger.Action(new Coordinate(1,2), HitResult.EMPTY);
        IGameAreaLogger.Action second = new IGameAreaLogger.Action(new Coordinate(2,3), HitResult.SHIP_HIT);
        IGameAreaLogger.Action third = new IGameAreaLogger.Action(new Coordinate(3,4), HitResult.SHIP_DESTROYED);
        IGameAreaLogger.Action fourth = new IGameAreaLogger.Action(new Coordinate(4,5), HitResult.ALREADY_HIT);
        IGameAreaLogger.Action fifth = new IGameAreaLogger.Action(new Coordinate(5,6), HitResult.VICTORY);
        IGameAreaLogger.Action sixth = new IGameAreaLogger.Action(new Coordinate(6,7), HitResult.GAME_HAS_ENDED);

        logger.recordAction(first.location(), first.result());
        compareActions(first, logger.getLatestAction());
        logger.recordAction(second.location(), second.result());
        compareActions(second, logger.getLatestAction());
        logger.recordAction(third.location(), third.result());
        compareActions(third, logger.getLatestAction());
        logger.recordAction(fourth.location(), fourth.result());
        compareActions(fourth, logger.getLatestAction());
        logger.recordAction(fifth.location(), fifth.result());
        compareActions(fifth, logger.getLatestAction());
        logger.recordAction(sixth.location(), sixth.result());
        compareActions(sixth, logger.getLatestAction());

        assertEquals(6, logger.numberOfPerformedActions());
        compareActions(first, logger.getAction(0));
        compareActions(second, logger.getAction(1));
        compareActions(third, logger.getAction(2));
        compareActions(fourth, logger.getAction(3));
        compareActions(fifth, logger.getAction(4));
        compareActions(sixth, logger.getAction(5));

        assertNull(logger.getAction(-1));
        assertNull(logger.getAction(6));
    }
}