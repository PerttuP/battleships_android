package org.hupisoft.battleships_ai;
import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IRestrictedGameArea;

/**
 * Interface for logic to destroy found ship.
 */
public interface IShipDestroyer {

    /**
     * Initialize destroyer. Call this before executing every action that may reveal a new ship.
     * @param area Game area.
     * @param origin First found ship-occupied location.
     * @param calculator Probability calculator.
     */
    void initialize(IRestrictedGameArea area, Coordinate origin, IHitProbabilityCalculator calculator);

    /**
     * Get next target coordinate.
     * @return Next best try to destroy the ship. Return null, if cannot decide next target (error).
     */
    Coordinate getNextTarget();

    /**
     * Inform destroyer on hit result. Call this after decision made by destroyer has been executed.
     * @param result Hit result.
     * @param location Hit location.
     */
    void confirmAction(HitResult result, Coordinate location);
}
