package org.hupisoft.battleships_ai;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IRestrictedGameArea;

/**
 * Interface for finding most likely hidden ship locations.
 */
interface IHitProbabilityCalculator {

    /**
     * Get hidden ship probability factor for an individual location.
     * @param area Target area.
     * @param location Target location.
     * @return Probability factor. That means number of area configuration where there might be
     *  a hidden ship in the location. This number is relative to probability for location having
     *  a hidden ship.
     */
    int getProbabilityFactor(IRestrictedGameArea area, Coordinate location);
}
