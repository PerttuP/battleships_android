package org.hupisoft.battleships_core.gamelogicwriter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CoordinateWriterTest.class,
        ShipWriterTest.class,
        GameAreaWriterTest.class,
        GameLogicWriterTest.class
})

/**
 * Run all writer unit tests.
 */
public class WriterSuite {
}
