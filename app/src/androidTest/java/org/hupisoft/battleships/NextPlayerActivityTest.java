package org.hupisoft.battleships;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
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

/**
 * Defines UI-tests for NextPlayerActivity.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NextPlayerActivityTest {

    @Rule
    public ActivityTestRule<MenuActivity> mMainActivityRule =
            new ActivityTestRule<>(MenuActivity.class);

    @Rule
    public ActivityTestRule<NextPlayerActivity> mActivityRule =
            new ActivityTestRule<>(NextPlayerActivity.class, true, false);

    @Rule
    public IntentsTestRule<NextPlayerActivity> intentsRule = new IntentsTestRule<>(NextPlayerActivity.class);

    private void launchActivity(Player playerInTurn) {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = new Intent(ctx, NextPlayerActivity.class);
        if (playerInTurn != null) {
            intent.putExtra(NextPlayerActivity.EXTRA_PLAYER_IN_TURN, playerInTurn);
        }
        mActivityRule.launchActivity(intent);
    }

    private void checkCommonElements() {
        onView(withId(R.id.nextPlayerBtn)).check(matches(withText(R.string.continueText)));
    }

    @Before
    public void setUp() {
        // Set current game to prevent launched BattleActivities from crashing.
        IGameManager manager = (IGameManager)mMainActivityRule.getActivity().getApplicationContext();
        manager.newVersusGame();
    }

    @Test
    public void showInstructionsForPlayer1() {
        launchActivity(Player.PLAYER_1);
        checkCommonElements();
        onView(withId(R.id.nextPlayerInfoTextView)).check(matches(withText(R.string.nextPlayer_instructionsPlayer1)));
    }

    @Test
    public void showInstructionsForPlayer2() {
        launchActivity(Player.PLAYER_2);
        checkCommonElements();
        onView(withId(R.id.nextPlayerInfoTextView)).check(matches(withText(R.string.nextPlayer_instructionsPlayer2)));
    }

    @Test
    public void showPlayer1InstructionsByDefault() {
        launchActivity(null);
        checkCommonElements();
        onView(withId(R.id.nextPlayerInfoTextView)).check(matches(withText(R.string.nextPlayer_instructionsPlayer1)));
    }

    @Test
    public void clickingContinueStartsBattleActivity() {
        launchActivity(Player.PLAYER_1);
        checkCommonElements();

        onView(withId(R.id.nextPlayerBtn)).perform(click());
        intended(hasComponent(BattleActivity.class.getName()));
    }
}
