package org.hupisoft.battleships_core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CoordinateTest.class,
        ShipTest.class,
        SquareTest.class,
        GameAreaTest.class,
        GameAreaBuilderTest.class,
        GameLogicTest.class,
        GameLogicBuilderTest.class
})

public class CoreTestSuite {
}
