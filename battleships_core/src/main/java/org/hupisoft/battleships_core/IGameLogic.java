package org.hupisoft.battleships_core;

/**
 * Interface for game logic.
 * Game logic is responsible for keeping book on current player,
 * number of hits and declaring the winner.
 */
public interface IGameLogic {

    /**
     * Perform current player's action.
     * @param hitLocation Location to be hit.
     * @return Hit result.
     */
    public HitResult playerAction(Coordinate hitLocation);

    /**
     * Get player, whose turn it is.
     * @return Current player.
     */
    public Player getCurrentPlayer();

    /**
     * Get specified player's game area.
     * NOTE: this is intented for drawing / AI purposes. Perform hits using playerAction method.
     * @param player Specified player.
     * @return Player's game area.
     */
    public IGameArea getGameArea(Player player);

    /**
     * Get number of hits that the player has made.
     * @param player The player.
     * @return Number of hits the player has performed.
     */
    public int getNumberOfHits(Player player);

    /**
     * Get the winning player.
     * @return The winner. Null, if game is not finished.
     */
    public Player getWinner();

    /**
     * Check if game is over.
     * @return True, if game is over.
     */
    public boolean isGameOver();
}
