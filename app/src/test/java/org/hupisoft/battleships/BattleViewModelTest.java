package org.hupisoft.battleships;

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
}