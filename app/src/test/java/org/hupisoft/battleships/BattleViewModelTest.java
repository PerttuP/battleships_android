package org.hupisoft.battleships;

import org.hupisoft.battleships_core.HitResult;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for BattleViewModel class.
 */
public class BattleViewModelTest {

    @Test
    public void initialStateIsShowFriendlyArea() {
        BattleViewModel model = new BattleViewModel();
        assertEquals(BattleViewModel.ViewState.ShowFriendlyArea, model.getState());
    }

    @Test
    public void toggleStateChangesViewState() {
        BattleViewModel model = new BattleViewModel();
        model.toggleState();
        assertEquals(BattleViewModel.ViewState.ShowEnemyArea, model.getState());
        model.toggleState();
        assertEquals(BattleViewModel.ViewState.ShowFriendlyArea, model.getState());
    }

    @Test
    public void hitIsNotPerformedInitially() {
        BattleViewModel model = new BattleViewModel();
        assertFalse(model.isHitPerformed());
        assertNull(model.getHitResult());
    }

    @Test
    public void hitIsNotPerformedIfHittingAlreadyHitSquare() {
        BattleViewModel model = new BattleViewModel();
        model.setHitResult(HitResult.ALREADY_HIT);
        assertFalse(model.isHitPerformed());
        assertEquals(HitResult.ALREADY_HIT, model.getHitResult());
    }

    @Test
    public void hitIsNotPerformedIfGameHasAlreadyEnded() {
        BattleViewModel model = new BattleViewModel();
        model.setHitResult(HitResult.GAME_HAS_ENDED);
        assertFalse(model.isHitPerformed());
        assertEquals(HitResult.GAME_HAS_ENDED, model.getHitResult());
    }

    @Test
    public void hitIsPerformedIfSquareIsEmpty() {
        BattleViewModel model = new BattleViewModel();
        model.setHitResult(HitResult.EMPTY);
        assertTrue(model.isHitPerformed());
        assertEquals(HitResult.EMPTY, model.getHitResult());
    }

    @Test
    public void hitIsPerformedIfShipIsHit() {
        BattleViewModel model = new BattleViewModel();
        model.setHitResult(HitResult.SHIP_HIT);
        assertTrue(model.isHitPerformed());
        assertEquals(HitResult.SHIP_HIT, model.getHitResult());
    }

    @Test
    public void hitIsPerformedIfShipIsDestroyed() {
        BattleViewModel model = new BattleViewModel();
        model.setHitResult(HitResult.SHIP_DESTROYED);
        assertTrue(model.isHitPerformed());
        assertEquals(HitResult.SHIP_DESTROYED, model.getHitResult());
    }

    @Test
    public void hitIsPerformedIfVictory() {
        BattleViewModel model = new BattleViewModel();
        model.setHitResult(HitResult.VICTORY);
        assertTrue(model.isHitPerformed());
        assertEquals(HitResult.VICTORY, model.getHitResult());
    }
}