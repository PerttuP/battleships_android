package org.hupisoft.battleships;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import static android.support.test.espresso.Espresso.*;

import org.hupisoft.battleships_ai.BattleShipAIFactory;
import org.hupisoft.battleships_ai.IBattleShipsAI;
import org.hupisoft.battleships_core.IGameLogic;
import org.junit.Rule;
import org.junit.Test;


import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * Automated tests for MenuActivity.
 */
public class MenuActivityTest {

    @Rule
    public IntentsTestRule<MenuActivity> mActivityRule =
            new IntentsTestRule<>(MenuActivity.class, true, false);

    private void launchMenuActivity() {
        mActivityRule.launchActivity(new Intent());
    }

    private void createVersusGame() {
        IGameManager m = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        m.newVersusGame();
    }

    private void createSinglePlayerGame() {
        IGameManager m = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        assertTrue(m.newSinglePlayerGame(BattleShipAIFactory.PROBABILITY_AI));
    }

    private void resetGame() {
        IGameManager m = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        m.resetCurrentGame();
    }

    @Test
    public void continueButtonIsDisabledIfThereIsNoActiveGameInstance() {
        resetGame();
        launchMenuActivity();

        onView(withId(R.id.continueGameBtn)).check(matches(withText(R.string.continueText)));
        onView(withId(R.id.continueGameBtn)).check(matches(not(isEnabled())));
        onView(withId(R.id.newVersusGameBtn)).check(matches(withText(R.string.menu_newVersusGame)));
        onView(withId(R.id.newSoloGameBtn)).check(matches(withText(R.string.menu_newSoloGame)));
    }

    @Test
    public void continueButtonIsEnabledIfThereIsAnActiveGameInstance() {
        createVersusGame();
        launchMenuActivity();

        onView(withId(R.id.continueGameBtn)).check(matches(withText(R.string.continueText)));
        onView(withId(R.id.continueGameBtn)).check(matches(isEnabled()));
        onView(withId(R.id.newVersusGameBtn)).check(matches(withText(R.string.menu_newVersusGame)));
        onView(withId(R.id.newSoloGameBtn)).check(matches(withText(R.string.menu_newSoloGame)));
    }

    @Test
    public void createVersusGameCreatesGameAndOpensNextPlayerActivity() {
        resetGame();
        launchMenuActivity();

        onView(withId(R.id.newVersusGameBtn)).perform(click());
        intended(hasComponent(NextPlayerActivity.class.getName()));

        IGameManager manager = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        assertNotNull(manager.currentGameLogic());
        assertEquals(IGameManager.GameType.VersusGame, manager.currentGameType());
        assertNull(manager.AIPlayer());
    }

    @Test
    public void createSinglePlayerOpensNewSinglePlayerMenu() {
        resetGame();
        launchMenuActivity();

        onView(withId(R.id.newSoloGameBtn)).perform(click());
        intended(hasComponent(NewSinglePlayerGameMenuActivity.class.getName()));

        IGameManager manager = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        assertNull(manager.currentGameLogic());
        assertNull(manager.currentGameType());
        assertNull(manager.AIPlayer());
    }

    @Test
    public void continueButtonOpensNextPlayerActivityInVersusGame() {
        createVersusGame();
        launchMenuActivity();

        IGameManager manager = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        IGameLogic oldLogic = manager.currentGameLogic();
        IGameManager.GameType oldType = manager.currentGameType();
        IBattleShipsAI oldAI = manager.AIPlayer();

        onView(withId(R.id.continueGameBtn)).perform(click());
        intended(hasComponent(NextPlayerActivity.class.getName()));

        // No changes to the game.
        assertEquals(oldLogic, manager.currentGameLogic());
        assertEquals(oldType, manager.currentGameType());
        assertEquals(oldAI, manager.AIPlayer());
    }

    @Test
    public void continueButtonOpensBattleActivityInSinglePlayer() {
        createSinglePlayerGame();
        launchMenuActivity();

        IGameManager manager = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        IGameLogic oldLogic = manager.currentGameLogic();
        IGameManager.GameType oldType = manager.currentGameType();
        IBattleShipsAI oldAI = manager.AIPlayer();

        onView(withId(R.id.continueGameBtn)).perform(click());
        intended(hasComponent(BattleActivity.class.getName()));

        // No changes to the game.
        assertEquals(oldLogic, manager.currentGameLogic());
        assertEquals(oldType, manager.currentGameType());
        assertEquals(oldAI, manager.AIPlayer());
    }

    @Test
    public void createVersusGameWhileGameExistsShowsAlertDialog() {
        createVersusGame();
        launchMenuActivity();

        IGameManager manager = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        IGameLogic oldLogic = manager.currentGameLogic();
        IGameManager.GameType oldType = manager.currentGameType();
        IBattleShipsAI oldAI = manager.AIPlayer();

        onView(withId(R.id.newVersusGameBtn)).perform(click());
        // Dialog is displayed
        onView(withText(R.string.confirmNewGame_message)).check(matches(isDisplayed()));
        // Dialog negative button can be clicked.
        onView(withText(R.string.confirmNewGame_cancel)).perform(click());

        // Negative response has not effect
        assertEquals(oldLogic, manager.currentGameLogic());
        assertEquals(oldType, manager.currentGameType());
        assertEquals(oldAI, manager.AIPlayer());
    }

    @Test
    public void createSinglePlayerGameWhileGameExistsShowsAlertDialog() {
        createVersusGame();
        launchMenuActivity();

        IGameManager manager = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        IGameLogic oldLogic = manager.currentGameLogic();
        IGameManager.GameType oldType = manager.currentGameType();
        IBattleShipsAI oldAI = manager.AIPlayer();

        onView(withId(R.id.newSoloGameBtn)).perform(click());
        // Dialog is displayed
        onView(withText(R.string.confirmNewGame_message)).check(matches(isDisplayed()));
        // Dialog negative button can be clicked.
        onView(withText(R.string.confirmNewGame_cancel)).perform(click());

        // Negative response has not effect
        assertEquals(oldLogic, manager.currentGameLogic());
        assertEquals(oldType, manager.currentGameType());
        assertEquals(oldAI, manager.AIPlayer());
    }

    @Test
    public void acceptingNewVersusGameCreatesNewGameAndOpensNextPlayerActivity() {
        createSinglePlayerGame();
        launchMenuActivity();

        IGameManager manager = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        IGameLogic oldLogic = manager.currentGameLogic();

        onView(withId(R.id.newVersusGameBtn)).perform(click());
        onView(withText(R.string.confirmNewGame_confirm)).perform(click());

        intended(hasComponent(NextPlayerActivity.class.getName()));

        assertNotEquals(oldLogic, manager.currentGameLogic());
        assertEquals(IGameManager.GameType.VersusGame, manager.currentGameType());
        assertNull(manager.AIPlayer());
    }

    @Test
    public void acceptingNewSinglePlayerCreatesNewGameAndOpensNewSinglePlayerMenu() {
        createSinglePlayerGame();
        launchMenuActivity();

        IGameManager manager = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        IGameLogic oldLogic = manager.currentGameLogic();
        IGameManager.GameType oldType = manager.currentGameType();
        IBattleShipsAI oldAi = manager.AIPlayer();

        onView(withId(R.id.newSoloGameBtn)).perform(click());
        onView(withText(R.string.confirmNewGame_confirm)).perform(click());

        intended(hasComponent(NewSinglePlayerGameMenuActivity.class.getName()));

        // New game is not created yet.
        assertEquals(oldLogic, manager.currentGameLogic());
        assertEquals(oldType, manager.currentGameType());
        assertEquals(oldAi, manager.AIPlayer());
    }
}