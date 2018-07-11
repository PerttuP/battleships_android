package org.hupisoft.battleships_core;

/**
 * Possible outcomes for hitting the game area.
 */
public enum HitResult {

    /**
     * Game has already ended. Hit had no effect.
     */
    GAME_HAS_ENDED,

    /**
     * Square in the hit location is already hit. Hit had no effect.
     */
    ALREADY_HIT,

    /**
     * Shuare in the location had no ship.
     */
    EMPTY,

    /**
     * Ship was hit, but not destroyed.
     */
    SHIP_HIT,

    /**
     * Ship was hit and destroyed.
     */
    SHIP_DESTROYED,

    /**
     * Last remaining ship was hot and destroyed.
     */
    VICTORY
}
