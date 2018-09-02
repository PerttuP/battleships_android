package org.hupisoft.battleships_ai;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IRestrictedGameArea;

import java.util.List;
import java.util.Random;

/**
 * AI implementation, that only makes random guesses.
 */
class RandomAI implements IBattleShipsAI {

    private Random mRng;
    private IRestrictedGameArea mArea = null;

    public RandomAI(Random rng) {
        mRng = rng;
    }

    @Override
    public void setTargetGameArea(IRestrictedGameArea targetArea) {
        mArea = targetArea;
    }

    @Override
    public Coordinate makeDecision() {
        List<Coordinate> availableCoordinates = mArea.getNonHitLocations();
        Coordinate target = null;
        if (availableCoordinates.size() != 0) {
            int index = mRng.nextInt(availableCoordinates.size());
            target = availableCoordinates.get(index);
        }
        return target;
    }

    @Override
    public void confirmDecision(Coordinate target, HitResult result) {
        // Nothing to do here
    }
}
