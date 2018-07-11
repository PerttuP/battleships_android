package org.hupisoft.battleships_core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameLogicTest {

    private GameLogic.PlayerGameSetup mSetup1;
    private GameLogic.PlayerGameSetup mSetup2;
    private GameLogic mLogic;

    private void defaultSetUp() {
        mSetup1 = new GameLogic.PlayerGameSetup();
        mSetup1.area = mock(IGameArea.class);
        mSetup1.numberOfHits = 0;
        mSetup2 = new GameLogic.PlayerGameSetup();
        mSetup2.area = mock(IGameArea.class);
        mSetup2.numberOfHits = 0;
        mLogic = new GameLogic(mSetup1, mSetup2, Player.PLAYER_1);
    }

    private void whenGameNotOver() {
        when(mSetup1.area.remainingShipCount()).thenReturn(3);
        when(mSetup2.area.remainingShipCount()).thenReturn(3);
    }

    private void whenPlayerOneLost() {
        when(mSetup1.area.remainingShipCount()).thenReturn(0);
        when(mSetup2.area.remainingShipCount()).thenReturn(3);
    }

    private void whenPlayerTwoLost() {
        when(mSetup1.area.remainingShipCount()).thenReturn(3);
        when(mSetup2.area.remainingShipCount()).thenReturn(0);
    }

    private void verifyGameOverCheck(Player winner) {
        if (winner == Player.PLAYER_2) {
            verify(mSetup1.area).remainingShipCount();
        } else {
            verify(mSetup1.area).remainingShipCount();
            verify(mSetup2.area).remainingShipCount();
        }

    }

    @Test
    public void playersHaveCorrectGameAreas() {
        defaultSetUp();
        assertEquals(mSetup1.area, mLogic.getGameArea(Player.PLAYER_1));
        assertEquals(mSetup2.area, mLogic.getGameArea(Player.PLAYER_2));
    }

    @Test
    public void correctPlayerIsInitiallyInTurn() {
        defaultSetUp();
        assertEquals(Player.PLAYER_1, mLogic.getCurrentPlayer());

        mLogic = new GameLogic(mSetup1, mSetup2, Player.PLAYER_2);
        assertEquals(Player.PLAYER_2, mLogic.getCurrentPlayer());
    }

    @Test
    public void gameAreaForNullPlayerIsNull() {
        defaultSetUp();
        assertNull(mLogic.getGameArea(null));
    }

    @Test
    public void playersHaveCorrectInitialPerformedHits() {
        defaultSetUp();
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_2));

        mSetup1.numberOfHits = 2;
        mSetup2.numberOfHits = 1;
        mLogic = new GameLogic(mSetup1, mSetup2, Player.PLAYER_2);
        assertEquals(2, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(1, mLogic.getNumberOfHits(Player.PLAYER_2));
    }

    @Test
    public void nullPlayerHas0Hits() {
        defaultSetUp();
        assertEquals(0, mLogic.getNumberOfHits(null));
    }

    @Test
    public void gameIsNotOverWhenBothPlayersHaveShipsRemaining() {
        defaultSetUp();
        whenGameNotOver();
        assertFalse(mLogic.isGameOver());
        verifyGameOverCheck(null);
        verifyNoMoreInteractions(mSetup1.area);
        verifyNoMoreInteractions(mSetup2.area);
    }

    @Test
    public void gameIsOverWhenPlayerOneHasNoLongerShips() {
        defaultSetUp();
        whenPlayerOneLost();
        assertTrue(mLogic.isGameOver());
        verifyGameOverCheck(Player.PLAYER_2);
        verifyNoMoreInteractions(mSetup1.area);
        verifyNoMoreInteractions(mSetup2.area);
    }

    @Test
    public void gameIsOverWhenPlayerTwoHasNoLongerShips() {
        defaultSetUp();
        whenPlayerTwoLost();
        assertTrue(mLogic.isGameOver());
        verifyGameOverCheck(Player.PLAYER_1);
        verifyNoMoreInteractions(mSetup1.area);
        verifyNoMoreInteractions(mSetup2.area);
    }

    @Test
    public void noWinnerIfGameIsNotOver() {
        defaultSetUp();
        whenGameNotOver();
        assertNull(mLogic.getWinner());
        verifyGameOverCheck(null);
        verifyNoMoreInteractions(mSetup1.area);
        verifyNoMoreInteractions(mSetup2.area);
    }

    @Test
    public void playerOneWinsIfPlayerTwoHasNoShipsLeft() {
        defaultSetUp();
        whenPlayerTwoLost();
        assertEquals(Player.PLAYER_1, mLogic.getWinner());
        verifyGameOverCheck(Player.PLAYER_1);
        verifyNoMoreInteractions(mSetup1.area);
        verifyNoMoreInteractions(mSetup2.area);
    }

    @Test
    public void playerTwoWinsIfPlayerOneHasNoShipsLeft() {
        defaultSetUp();
        whenPlayerOneLost();
        assertEquals(Player.PLAYER_2, mLogic.getWinner());
        verifyGameOverCheck(Player.PLAYER_2);
        verifyNoMoreInteractions(mSetup1.area);
        verifyNoMoreInteractions(mSetup2.area);
    }

    @Test
    public void performingHitOnEmptyLocationIncreasesHitCountAndChangesPlayer() {
        defaultSetUp();
        Coordinate c1 = new Coordinate(1,2);
        Coordinate c2 = new Coordinate(3,4);
        when(mSetup2.area.hit(c1)).thenReturn(HitResult.EMPTY);
        when(mSetup1.area.hit(c2)).thenReturn(HitResult.SHIP_HIT);
        whenGameNotOver();

        // Initial
        assertEquals(Player.PLAYER_1, mLogic.getCurrentPlayer());
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_2));

        // Player1 hits
        assertEquals(HitResult.EMPTY, mLogic.playerAction(c1));
        assertEquals(Player.PLAYER_2, mLogic.getCurrentPlayer());
        assertEquals(1, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_2));

        // Player2 hits
        assertEquals(HitResult.SHIP_HIT, mLogic.playerAction(c2));
        assertEquals(Player.PLAYER_1, mLogic.getCurrentPlayer());
        assertEquals(1, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(1, mLogic.getNumberOfHits(Player.PLAYER_2));

        verify(mSetup2.area).hit(c1);
        verify(mSetup1.area).hit(c2);
        verify(mSetup1.area, times(2)).remainingShipCount();
        verify(mSetup2.area, times(2)).remainingShipCount();
        verifyNoMoreInteractions(mSetup1.area);
        verifyNoMoreInteractions(mSetup2.area);
    }

    @Test
    public void hittingAlreadyHitLocationHasNoEffect() {
        defaultSetUp();
        Coordinate c1 = new Coordinate(1,2);
        when(mSetup2.area.hit(c1)).thenReturn(HitResult.ALREADY_HIT);
        whenGameNotOver();

        // Initial
        assertEquals(Player.PLAYER_1, mLogic.getCurrentPlayer());
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_2));

        assertEquals(HitResult.ALREADY_HIT, mLogic.playerAction(c1));
        assertEquals(Player.PLAYER_1, mLogic.getCurrentPlayer());
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_2));

        verifyGameOverCheck(null);
        verify(mSetup2.area).hit(c1);
        verifyNoMoreInteractions(mSetup1.area);
        verifyNoMoreInteractions(mSetup2.area);
    }

    @Test
    public void hittingAfterGameHasEndedHasNoEffect() {
        defaultSetUp();
        Coordinate c1 = new Coordinate(1,2);
        whenPlayerTwoLost();

        assertEquals(HitResult.GAME_HAS_ENDED, mLogic.playerAction(c1));
        assertEquals(Player.PLAYER_1, mLogic.getCurrentPlayer());
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_2));

        verifyGameOverCheck(Player.PLAYER_1);
        verifyNoMoreInteractions(mSetup1.area);
        verifyNoMoreInteractions(mSetup2.area);
    }

    @Test
    public void sinkingShipChecksIfGameIsOver() {
        defaultSetUp();
        Coordinate c1 = new Coordinate(1,2);
        Coordinate c2 = new Coordinate(3,4);

        Answer<Integer> player1Ships = new Answer<Integer>() {
            int[] remaining = {1, 1, 1, 0};
            int i = 0;
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return remaining[i++];
            }
        };
        Answer<Integer> player2Ships = new Answer<Integer>() {
            int[] remaining = {2, 1, 1};
            int i = 0;
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return remaining[i++];
            }
        };
        when(mSetup1.area.remainingShipCount()).thenAnswer(player1Ships);
        when(mSetup2.area.remainingShipCount()).thenAnswer(player2Ships);
        when(mSetup2.area.hit(c1)).thenReturn(HitResult.SHIP_DESTROYED);
        when(mSetup1.area.hit(c2)).thenReturn(HitResult.SHIP_DESTROYED);

        // Initial
        assertEquals(Player.PLAYER_1, mLogic.getCurrentPlayer());
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_2));

        // Player1 hits
        assertEquals(HitResult.SHIP_DESTROYED, mLogic.playerAction(c1));
        assertEquals(Player.PLAYER_2, mLogic.getCurrentPlayer());
        assertEquals(1, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_2));

        // Player2 hits
        assertEquals(HitResult.VICTORY, mLogic.playerAction(c2));
        assertEquals(Player.PLAYER_1, mLogic.getCurrentPlayer());
        assertEquals(1, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(1, mLogic.getNumberOfHits(Player.PLAYER_2));

        verify(mSetup2.area).hit(c1);
        verify(mSetup1.area).hit(c2);
        verify(mSetup1.area, times(4)).remainingShipCount();
        verify(mSetup2.area, times(3)).remainingShipCount();
        verifyNoMoreInteractions(mSetup1.area);
        verifyNoMoreInteractions(mSetup2.area);
    }

    @Test
    public void hittingOutOfBoundsHasNoEffect() {
        defaultSetUp();
        Coordinate c1 = new Coordinate(20, 25);
        when(mSetup2.area.hit(c1)).thenReturn(null);
        whenGameNotOver();

        // Initial
        assertEquals(Player.PLAYER_1, mLogic.getCurrentPlayer());
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_2));

        assertNull(mLogic.playerAction(c1));
        assertEquals(Player.PLAYER_1, mLogic.getCurrentPlayer());
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_1));
        assertEquals(0, mLogic.getNumberOfHits(Player.PLAYER_2));

        verifyGameOverCheck(null);
        verify(mSetup2.area).hit(c1);
        verifyNoMoreInteractions(mSetup1.area);
        verifyNoMoreInteractions(mSetup2.area);
    }
}