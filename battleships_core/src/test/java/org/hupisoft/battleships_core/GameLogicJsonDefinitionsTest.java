package org.hupisoft.battleships_core;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameLogicJsonDefinitionsTest {
    @Test
    public void gameLogicJsonDefinitionsTagsTest()
    {
        assertEquals("gameLogic", GameLogicJsonDefinitions.GAME_LOGIC_TAG);
        assertEquals("player1Area", GameLogicJsonDefinitions.PLAYER1_GAME_AREA_TAG);
        assertEquals("player2Area", GameLogicJsonDefinitions.PLAYER2_GAME_AREA_TAG);
        assertEquals("currentPlayer", GameLogicJsonDefinitions.CURRENT_PLAYER_TAG);
        assertEquals("width", GameLogicJsonDefinitions.WIDTH_TAG);
        assertEquals("height", GameLogicJsonDefinitions.HEIGHT_TAG);
        assertEquals("ships", GameLogicJsonDefinitions.SHIPS_TAG);
        assertEquals("length", GameLogicJsonDefinitions.LENGTH_TAG);
        assertEquals("bowCoordinates", GameLogicJsonDefinitions.BOW_TAG);
        assertEquals("orientation", GameLogicJsonDefinitions.ORIENTATION_TAG);
        assertEquals("hits", GameLogicJsonDefinitions.HITS_TAG);
        assertEquals("x", GameLogicJsonDefinitions.X_TAG);
        assertEquals("y", GameLogicJsonDefinitions.Y_TAG);
    }
}