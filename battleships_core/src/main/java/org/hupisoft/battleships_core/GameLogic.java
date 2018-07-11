package org.hupisoft.battleships_core;

/**
 * Implements the IGameLogic interface.
 * @see IGameLogic
 */
class GameLogic implements IGameLogic {

    /**
     * Helper class for initializing game logic. Contains setup for one player.
     */
    static class PlayerGameSetup {
        /**
         * Player's own game area.
         */
        public IGameArea area;
        /**
         * Number of hits the player has performed.
         */
        public Integer numberOfHits;
    }

    private PlayerGameSetup mSetup1 = null;
    private PlayerGameSetup mSetup2 = null;
    private Player mcCurrentPlayer = null;

    /**
     * Constructor.
     * @param player1Setup Setup for player 1.
     * @param player2Setup Setup for player 2.
     * @param currentPlayer Player making next action.
     */
    GameLogic(PlayerGameSetup player1Setup, PlayerGameSetup player2Setup, Player currentPlayer) {
        mSetup1 = player1Setup;
        mSetup2 = player2Setup;
        mcCurrentPlayer = currentPlayer;
    }

    @Override
    public HitResult playerAction(Coordinate hitLocation) {
        HitResult result = HitResult.GAME_HAS_ENDED;
        if (!isGameOver()) {
            IGameArea area = getCurrentPlayer() == Player.PLAYER_1 ? mSetup2.area : mSetup1.area;
            result = area.hit(hitLocation);
            if (result == HitResult.SHIP_DESTROYED) {
                if (isGameOver()) {
                    result = HitResult.VICTORY;
                }
            }
        }

        if (result != null && result != HitResult.ALREADY_HIT && result != HitResult.GAME_HAS_ENDED) {
            mSetup1.numberOfHits = getCurrentPlayer() == Player.PLAYER_1 ? mSetup1.numberOfHits + 1 : mSetup1.numberOfHits;
            mSetup2.numberOfHits = getCurrentPlayer() == Player.PLAYER_2 ? mSetup2.numberOfHits + 1 : mSetup2.numberOfHits;
            mcCurrentPlayer = getCurrentPlayer() == Player.PLAYER_1 ? Player.PLAYER_2 : Player.PLAYER_1;
        }

        return result;
    }

    @Override
    public Player getCurrentPlayer() {
        return mcCurrentPlayer;
    }

    @Override
    public IGameArea getGameArea(Player player) {
        IGameArea area = null;
        if (player == Player.PLAYER_1) {
            area = mSetup1.area;
        } else if (player == Player.PLAYER_2) {
            area = mSetup2.area;
        }
        return area;
    }

    @Override
    public int getNumberOfHits(Player player) {
        int numOfHits = 0;
        if (player == Player.PLAYER_1) {
            numOfHits = mSetup1.numberOfHits;
        } else if (player == Player.PLAYER_2) {
            numOfHits = mSetup2.numberOfHits;
        }
        return numOfHits;
    }

    @Override
    public Player getWinner() {
        Player winner = null;
        if (mSetup1.area.remainingShipCount() == 0) {
            winner = Player.PLAYER_2;
        } else if (mSetup2.area.remainingShipCount() == 0) {
            winner = Player.PLAYER_1;
        }
        return winner;
    }

    @Override
    public boolean isGameOver() {
        return getWinner() != null;
    }
}
