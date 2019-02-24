package org.hupisoft.battleships;

import android.arch.lifecycle.ViewModel;

import org.hupisoft.battleships_core.HitResult;

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
    private HitResult mHitResult = null;

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
     * Return true, if player in turn has performed his action.
     * @return True, if player in turn has already performed his action.
     */
    Boolean isHitPerformed() {
        return mHitResult != null &&
                mHitResult != HitResult.ALREADY_HIT &&
                mHitResult != HitResult.GAME_HAS_ENDED;
    }

    /**
     * Set hit result.
     */
    void setHitResult(HitResult result) {
        mHitResult = result;
    }

    /**
     * Get hit result.
     */
    HitResult getHitResult() {
        return mHitResult;
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
