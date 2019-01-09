package org.hupisoft.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.hupisoft.battleships_core.Player;

/**
 * Activity shown between player turns. Prevents players from seeing other's unconcealed game area.
 */
public class NextPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_player);

        Button nextPlayerBtn = findViewById(R.id.nextPlayerBtn);
        nextPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to battle view
                Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setInstructionText();
    }

    private void setInstructionText() {
        IGameManager manager = (IGameManager)getApplicationContext();
        Player playerInTurn = manager.currentGameLogic().getCurrentPlayer();

        TextView text = findViewById(R.id.nextPlayerInfoTextView);
        if (playerInTurn == Player.PLAYER_2) {
            text.setText(R.string.nextPlayer_instructionsPlayer2);
        } else {
            text.setText(R.string.nextPlayer_instructionsPlayer1);
        }
    }
}
