package org.hupisoft.battleships_core;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the GameLogicBuilderProvider class.
 */
public class GameLogicBuilderProviderTest {

    @Test
    public void providerReturnsRealBuilderByDefault() {
        GameLogicBuilderProvider provider = new GameLogicBuilderProvider();
        IGameLogicBuilder builder1 = provider.getBuilderInstance();
        IGameLogicBuilder builder2 = provider.getBuilderInstance();
        assertSame(builder1, builder2);
        assertNotNull((GameLogicBuilder)builder1);
    }

    @Test
    public void setBuilderChangesBuilder() {
        GameLogicBuilderProvider provider = new GameLogicBuilderProvider();
        IGameLogicBuilder mockBuilder = mock(IGameLogicBuilder.class);
        provider.setBuilder(mockBuilder);
        assertSame(mockBuilder, provider.getBuilderInstance());
        provider.reset();
        IGameLogicBuilder builder = provider.getBuilderInstance();
        assertNotSame(mockBuilder, provider.getBuilderInstance());
        assertNotNull((GameLogicBuilder)builder);
    }

}