package org.hupisoft.battleships_ai;

import java.util.Random;

/**
 * Factory class for battleships AI.
 */
public class BattleShipAIFactory {

    // Provided AI implementations:
    public static final String RANDOM_AI = "RandomAI";
    public static final String PROBABILITY_AI = "ProbabilityAI";

    /**
     * Create new AI instance.
     * @param id AI identifier.
     * @return New AI instance or null, if identifier does not match any available AI.
     */
    public IBattleShipsAI createAI(String id) {
        IBattleShipsAI ai = null;

        if (id.equals(RANDOM_AI)) {
            ai = new RandomAI(new Random());
        }
        else if (id.equals(PROBABILITY_AI)) {
            IHitProbabilityCalculator calculator = new HitProbabilityCalculator();
            IShipDestroyer destroyer = new DefaultShipDestroyer();
            ai = new ProbabilityAI(calculator, destroyer);
        }

        return ai;
    }
}
