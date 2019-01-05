package org.hupisoft.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.hupisoft.battleships_core.Player;

public class MenuActivity extends AppCompatActivity {

    private GameManager mManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        System.out.println("Create menu");

        mManager = (GameManager)getApplicationContext();

        Button newVersusGameBtn = findViewById(R.id.newVersusGameBtn);
        newVersusGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mManager.newVersusGame();
                Intent intent = new Intent(getApplicationContext(), NextPlayerActivity.class);
                intent.putExtra(NextPlayerActivity.EXTRA_PLAYER_IN_TURN, Player.PLAYER_1);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Button continueBtn = findViewById(R.id.continueGameBtn);
        continueBtn.setEnabled(mManager.currentGameLogic() != null);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mManager.currentGameType() == GameManager.GameType.VersusGame) {
                    Intent intent = new Intent(getApplicationContext(), NextPlayerActivity.class);
                    intent.putExtra(NextPlayerActivity.EXTRA_PLAYER_IN_TURN, mManager.currentGameLogic().getCurrentPlayer());
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
