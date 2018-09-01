package org.hupisoft.battleships_ai;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IGameArea;

/**
 * Interface for all different battleships AI implementations.
 */
public interface IBattleShipsAI {

    /**
     * Set target game area. This should be the AI's opponents game area.
     * Calling the method will reset AI's possible caches, forgetting the previous game.
     * TODO: AI should not be allowed to modify area directly.
     * TODO: Separate IGameArea to read-only and modifiable interface.
     * @param targetArea AI's opponent's game area.
     */
    void setTargetGameArea(IGameArea targetArea);

    /**
     * Decide the next hit location.
     * @return Location on target area to be hit.
     */
    Coordinate makeDecision();

    /**
     * Game engine will call this method when AI's decision is performed.
     * AI may now update it's internal state and trust the hit has been made on target area.
     * @param target AI's latest decision that has been performed.
     * @param result Result for performed hit.
     */
    void confirmDecision(Coordinate target, HitResult result);
}
