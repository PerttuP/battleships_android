package org.hupisoft.battleships;

import android.arch.lifecycle.ViewModel;

/**
 * View model for BattleActivity.
 * Persists UI state over configuration changes.
 */
class BattleViewModel extends ViewModel {

    /**
     * Battle view state.
     * Battle view is either showing friendly or enemy game area.
     */
    enum ViewState {
        /**
         * Currently showing friendly game area.
         */
        ShowFriendlyArea,

        /**
         * Currently showing enemy area.
         */
        ShowEnemyArea
    }

    private ViewState mState;

    /**
     * Get battle view state.
     * @return Battle view state.
     */
    ViewState getState() {
        if (mState == null) {
            mState = ViewState.ShowFriendlyArea;
        }
        return mState;
    }

    /**
     * Toggle view state to the other.
     */
    void toggleState() {
        if (getState() == ViewState.ShowFriendlyArea) {
            mState = ViewState.ShowEnemyArea;
        }
        else {
            mState = ViewState.ShowFriendlyArea;
        }
    }
}
