package org.hupisoft.battleships_core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Tests the GameLogicBuilder class.
 * @see GameLogicBuilder
 */
public class GameLogicBuilderTest {

    private GameLogicBuilder mBuilder = null;
    @Mock
    private IGameAreaBuilder mockAreaBuilder;
    @Mock
    private IGameArea mockArea1;
    @Mock
    private IGameArea mockArea2;

    @Before
    public void setUp() {
        mockArea1 = mock(IGameArea.class);
        mockArea2 = mock(IGameArea.class);
        mockAreaBuilder = mock(IGameAreaBuilder.class);
    }

    @Test
    public void newGameCreatesGameAreaForBothPlayers() {
        mBuilder = new GameLogicBuilder(mockAreaBuilder);
        when(mockAreaBuilder.createInitialGameArea(10, 8, new int[]{2,3,5})).thenAnswer(new Answer<IGameArea>(){
            int i = 0;
            @Override
            public IGameArea answer(InvocationOnMock invocation) throws Throwable {
                IGameArea area = i%2 == 0 ? mockArea1 : mockArea2;
                ++i;
                return area;
            }
        });

        IGameLogic logic = mBuilder.createNewGame(10, 8, new int[]{2,3,5});
        assertNotNull(logic);
        assertEquals(mockArea1, logic.getGameArea(Player.PLAYER_1));
        assertEquals(mockArea2, logic.getGameArea(Player.PLAYER_2));
        assertEquals(0, logic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, logic.getNumberOfHits(Player.PLAYER_2));

        verify(mockAreaBuilder, times(2)).createInitialGameArea(10, 8, new int[]{2,3,5});
    }

    @Test
    public void createNewGameWithDefaultAreaBuilder() {
        mBuilder = new GameLogicBuilder();
        int areaWidth = 12;
        int areaHeight = 10;
        int[] shipLengths = {2,3,5};
        IGameLogic logic = mBuilder.createNewGame(areaWidth, areaHeight, shipLengths);

        assertNotNull(logic);
        assertNotEquals(logic.getGameArea(Player.PLAYER_1), logic.getGameArea(Player.PLAYER_2));
        assertEquals(areaWidth, logic.getGameArea(Player.PLAYER_1).width());
        assertEquals(areaHeight, logic.getGameArea(Player.PLAYER_1).height());
        assertEquals(areaWidth, logic.getGameArea(Player.PLAYER_2).width());
        assertEquals(areaHeight, logic.getGameArea(Player.PLAYER_2).height());
        assertEquals(0, logic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, logic.getNumberOfHits(Player.PLAYER_2));
        assertEquals(Player.PLAYER_1, logic.getCurrentPlayer());
        assertFalse(logic.isGameOver());
        assertNull(logic.getWinner());

        assertEquals(shipLengths.length, logic.getGameArea(Player.PLAYER_1).getShips().size());
        assertEquals(shipLengths.length, logic.getGameArea(Player.PLAYER_2).getShips().size());

        for (int i = 0; i < shipLengths.length; ++i) {
            IShip ship1 = logic.getGameArea(Player.PLAYER_1).getShips().get(i);
            IShip ship2 = logic.getGameArea(Player.PLAYER_2).getShips().get(i);
            assertNotEquals(ship1, ship2);
            assertEquals(shipLengths[i], ship1.length());
            assertEquals(0, ship1.hitCount());
            assertEquals(shipLengths[i], ship2.length());
            assertEquals(0, ship2.hitCount());
        }
    }
}