package org.hupisoft.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.hupisoft.battleships_ai.BattleShipAIFactory;

/**
 * Menu for selecting new single player difficulty level and creating the game.
 */
public class NewSinglePlayerGameMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsingleplayergamemenu);
        setButtonListener(R.id.easyAiBtn, BattleShipAIFactory.RANDOM_AI);
        setButtonListener(R.id.difficultAiBtn, BattleShipAIFactory.PROBABILITY_AI);
    }

    private void setButtonListener(int buttonId, final String aiId) {
        Button btn = findViewById(buttonId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSinglePlayerGame(aiId);
            }
        });

    }

    private void createSinglePlayerGame(String aiId) {
        IGameManager manager = (IGameManager)getApplicationContext();
        manager.newSinglePlayerGame(aiId);
        Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
        startActivity(intent);
    }
}
