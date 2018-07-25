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

    public static final String EXTRA_PLAYER_IN_TURN = "org.hupisoft.battleships.playerInTurn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_player);
        setInstructionText();

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

    private void setInstructionText() {
        Player playerInTurn = (Player)getIntent().getSerializableExtra(NextPlayerActivity.EXTRA_PLAYER_IN_TURN);

        TextView text = findViewById(R.id.nextPlayerInfoTextView);
        if (playerInTurn == Player.PLAYER_2) {
            text.setText(R.string.nextPlayer_instructionsPlayer2);
        } else {
            text.setText(R.string.nextPlayer_instructionsPlayer1);
        }
    }
}
