package org.hupisoft.battleships_ai;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    BattleShipAIFactoryTest.class,
    RandomAITest.class,
    HitProbabilityCalculatorTest.class,
    DefaultShipDestroyerTest.class,
    ProbabilityAITest.class
})

/**
 * Runs all unit tests.
 */
public class AITestSuite {
}