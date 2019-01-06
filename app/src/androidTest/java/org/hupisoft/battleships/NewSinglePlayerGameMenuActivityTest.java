package org.hupisoft.battleships;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hupisoft.battleships_ai.BattleShipAIFactory;
import org.hupisoft.battleships_core.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static org.junit.Assert.*;

/**
 * Automated UI tests for NewSinglePlayerGameMenuActivity.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewSinglePlayerGameMenuActivityTest {

    @Rule
    public ActivityTestRule<NewSinglePlayerGameMenuActivity> mActivityRule =
            new ActivityTestRule<>(NewSinglePlayerGameMenuActivity.class, true, false);

    @Rule
    public IntentsTestRule<NewSinglePlayerGameMenuActivity> intentsRule =
            new IntentsTestRule<>(NewSinglePlayerGameMenuActivity.class);


    @Before
    public void launchActivity() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = new Intent(ctx, NewSinglePlayerGameMenuActivity.class);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void menuHasCorrectTitle() {
        onView(withId(R.id.newSinglePlayerMenuTitle)).check(matches(withText(R.string.newSinglePlayerGameMenu_title)));
    }

    @Test
    public void menuHasButtonForEachDifficulty() {
        onView(withId(R.id.easyAiBtn)).check(matches(withText(R.string.newSinglePlayerGameMenu_easy)));
        onView(withId(R.id.difficultAiBtn)).check(matches(withText(R.string.newSinglePlayerGameMenu_difficult)));
    }

    @Test
    public void easyButtonCreatesRandomAiAndOpensBattleView() {
        onView(withId(R.id.easyAiBtn)).perform(click());
        intended(hasComponent(BattleActivity.class.getName()));

        IGameManager manager = (IGameManager)mActivityRule.getActivity().getApplicationContext();
        assertNotNull(manager.AIPlayer());
        assertEquals(BattleShipAIFactory.RANDOM_AI, manager.AIPlayer().id());
        assertNotNull(manager.currentGameLogic());
        assertEquals(IGameManager.GameType.SinglePlayerGame, manager.currentGameType());
    }

    @Test
    public void difficultButtonCreatesProbabilityAiAndOpensBattleView() {
        onView(withId(R.id.difficultAiBtn)).perform(click());
        intended(hasComponent(BattleActivity.class.getName()));

        IGameManager manager = (IGameManager)mActivityRule.getActivity().getApplicationContext();
        assertNotNull(manager.AIPlayer());
        assertEquals(BattleShipAIFactory.PROBABILITY_AI, manager.AIPlayer().id());
        assertNotNull(manager.currentGameLogic());
        assertEquals(IGameManager.GameType.SinglePlayerGame, manager.currentGameType());
    }
}