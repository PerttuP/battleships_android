package org.hupisoft.battleships_ai;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IRestrictedGameArea;

/**
 * Game AI implementation that selects target square based on
 * probability of ship locations.
 */
public class ProbabilityAI implements IBattleShipsAI {

    /**
     * AI operating mode.
     */
    enum Mode {
        /**
         * AI has not found a new ship to be destroyed. Finding a new ship.
         */
        SeekShips,

        /**
         * AI has found a ship location. Destroying found ship.
         */
        DestroyShip
    }

    private IRestrictedGameArea mArea;
    private IHitProbabilityCalculator mCalculator;
    private IShipDestroyer mDestroyer;
    private Mode mMode;

    /**
     * Constructor.
     * @param calculator Probability calculator.
     */
    public ProbabilityAI(IHitProbabilityCalculator calculator, IShipDestroyer destroyer) {
        mArea = null;
        mCalculator = calculator;
        mDestroyer = destroyer;
        mMode = Mode.SeekShips;
    }

    @Override
    public void setTargetGameArea(IRestrictedGameArea targetArea) {
        mArea = targetArea;
    }

    @Override
    public Coordinate makeDecision() {
        Coordinate target = null;

        if (mMode == Mode.SeekShips) {
            target = findNewShips();
            mDestroyer.initialize(mArea, target, mCalculator);
        }
        else if (mMode == Mode.DestroyShip) {
            target = destroyShip();
        }

        return target;
    }

    @Override
    public void confirmDecision(Coordinate target, HitResult result) {
        if (result == HitResult.SHIP_DESTROYED || result == HitResult.GAME_HAS_ENDED) {
            // Ship destroyed.
            mMode = Mode.SeekShips;
        }
        else if (mMode == Mode.SeekShips && result == HitResult.SHIP_HIT) {
            // New ship found
            mMode = Mode.DestroyShip;
        }
        else if (mMode == Mode.DestroyShip) {
            mDestroyer.confirmAction(result, target);
        }
    }

    private Coordinate findNewShips() {
        // Iterate through all squares and select highest probability.
        // TODO: optimize: Save calculations and update only changed squares.
        Coordinate target = null;
        int probability = 0;

        for (int x = 0; x < mArea.width(); ++x) {
            for (int y = 0; y < mArea.height(); ++y) {
                Coordinate c = new Coordinate(x,y);
                int p = mCalculator.getProbabilityFactor(mArea, c);
                if (p > probability) {
                    // New highest probability.
                    probability = p;
                    target = c;
                }
            }
        }

        return target;
    }

    private Coordinate destroyShip() {
        Coordinate target = mDestroyer.getNextTarget();
        return target;
    }
}
