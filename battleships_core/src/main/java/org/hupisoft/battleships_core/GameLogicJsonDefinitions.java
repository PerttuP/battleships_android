package org.hupisoft.battleships_core;

/**
 * Definitions for JSON field identifiers.
 * Example JSON:
 * {
 *     gameLogic:{
 *          currentPlayer: PLAYER_1,
 *          player1Area: {
 *              width: 12,
 *              height: 8,
 *              ships: [
 *                  {length: 3, bowCoordinates: {x:3, y:4}, orientation: HORIZONTAL},
 *                  {length: 2, bowCoordinates: {x:5, y:6}, orientation: VERTICAL}
 *              ],
 *              hits: [{x:7, y:3}, {x:9, y:7}]
 *          },
 *          player2Area: {
 *              width: 12,
 *              height: 8,
 *              ships: [
 *                  {length: 3, bowCoordinates: {x:5, y:6}, orientation: HORIZONTAL},
 *                  {length: 2, bowCoordinates: {x:1, y:1}, orientation: VERTICAL}
 *              ],
 *              hits: [{x:1, y:2}, {x:6, y:5}]
 *          }
 *     }
 * }
 */
class GameLogicJsonDefinitions {

    public static final String GAME_LOGIC_TAG = "gameLogic";
        public static final String CURRENT_PLAYER_TAG = "currentPlayer";
        public static final String PLAYER1_GAME_AREA_TAG = "player1Area";
        public static final String PLAYER2_GAME_AREA_TAG = "player2Area";
            public static final String WIDTH_TAG = "width";
            public static final String HEIGHT_TAG = "height";
            public static final String HITS_TAG = "hits";
                public static final String X_TAG = "x";
                public static final String Y_TAG = "y";
            public static final String SHIPS_TAG = "ships";
                public static final String LENGTH_TAG = "length";
                public static final String BOW_TAG = "bowCoordinates";
                public static final String ORIENTATION_TAG = "orientation";
}
