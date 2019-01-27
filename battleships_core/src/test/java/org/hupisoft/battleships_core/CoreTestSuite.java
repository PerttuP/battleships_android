package org.hupisoft.battleships_core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CoordinateTest.class,
        ShipTest.class,
        SquareTest.class,
        RestrictedGameAreaWrapperTest.class,
        GameAreaLoggerTest.class,
        GameAreaTest.class,
        GameAreaBuilderTest.class,
        GameLogicTest.class,
        GameLogicBuilderTest.class,
        GameLogicBuilderProviderTest.class,
        GameLogicJsonReaderTest.class,
        GameLogicJsonWriterTest.class,
        GameLogicJsonIntegrationTest.class
})

/**
 * Runs all unit tests.
 */
public class CoreTestSuite {
}
