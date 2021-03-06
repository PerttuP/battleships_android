package org.hupisoft.battleships;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.Player;
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
    public IntentsTestRule<NextPlayerActivity> mActivityRule =
            new IntentsTestRule<>(NextPlayerActivity.class, true, false);

    private void startActivity(Player playerInTurn) {
        IGameManager manager = (IGameManager)InstrumentationRegistry.getTargetContext().getApplicationContext();
        manager.newVersusGame();
        if (playerInTurn != Player.PLAYER_1) {
            manager.currentGameLogic().playerAction(new Coordinate(1,2));
        }

        mActivityRule.launchActivity(new Intent(InstrumentationRegistry.getTargetContext().getApplicationContext(), NextPlayerActivity.class));
    }

    private void checkCommonElements() {
        onView(withId(R.id.nextPlayerBtn)).check(matches(withText(R.string.continueText)));
    }

    @Test
    public void showInstructionsForPlayer1() {
        startActivity(Player.PLAYER_1);
        checkCommonElements();
        onView(withId(R.id.nextPlayerInfoTextView)).check(matches(withText(R.string.nextPlayer_instructionsPlayer1)));
    }

    @Test
    public void showInstructionsForPlayer2() {
        startActivity(Player.PLAYER_2);
        checkCommonElements();
        onView(withId(R.id.nextPlayerInfoTextView)).check(matches(withText(R.string.nextPlayer_instructionsPlayer2)));
    }

    @Test
    public void clickingContinueStartsBattleActivity() {
        startActivity(Player.PLAYER_1);
        checkCommonElements();
        onView(withId(R.id.nextPlayerBtn)).perform(click());
        intended(hasComponent(BattleActivity.class.getName()));
    }
}
