package org.hupisoft.battleships_ai;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the BattleShipsAIFactory class.
 */
public class BattleShipAIFactoryTest {

    @Test
    public void verifyAiIdentifiers()
    {
        assertEquals("RandomAI", BattleShipAIFactory.RANDOM_AI);
    }

    @Test
    public void factoryReturnsNullForUnknownId()
    {
        assertNull(new BattleShipAIFactory().createAI("unknownAI"));
    }

    @Test
    public void createUniqueRandomAITest()
    {
        BattleShipAIFactory factory = new BattleShipAIFactory();

        IBattleShipsAI ai1 = factory.createAI("RandomAI");
        assertNotNull(ai1);
        assertNotNull((RandomAI)ai1);

        IBattleShipsAI ai2 = factory.createAI(BattleShipAIFactory.RANDOM_AI);
        assertNotNull(ai2);
        assertNotNull((RandomAI)ai2);

        assertNotEquals(ai1, ai2);
    }
}