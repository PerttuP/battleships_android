package org.hupisoft.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity
        extends AppCompatActivity
        implements NewGameConfirmDialog.INewGameConfirmListener {

    private IGameManager mManager = null;
    private IGameManager.GameType mNewGameRequest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mManager = (IGameManager)getApplicationContext();

        Button newVersusGameBtn = findViewById(R.id.newVersusGameBtn);
        newVersusGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewGameRequested(IGameManager.GameType.VersusGame);
            }
        });

        Button newSinglePlayerGameBtn = findViewById(R.id.newSoloGameBtn);
        newSinglePlayerGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewGameRequested(IGameManager.GameType.SinglePlayerGame);
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
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onNewGameConfirmed() {
        if (mNewGameRequest == IGameManager.GameType.VersusGame) {
            newVersusGame();
        }
        else if (mNewGameRequest == IGameManager.GameType.SinglePlayerGame) {
            newSinglePlayerGame();
        }
        mNewGameRequest = null;
    }

    @Override
    public void onNewGameCancelled() {
        System.out.println("New game cancelled.");
        mNewGameRequest = null;
    }

    private void onNewGameRequested(IGameManager.GameType gameType) {
        mNewGameRequest = gameType;
        if (mManager.currentGameType() != null) {
            // Player needs to confirm overwriting existing game.
            showConfirmNewGameDialog();
        }
        else {
            // No current game. Proceed.
            onNewGameConfirmed();
        }
    }

    private void newVersusGame() {
        System.out.println("New versus game.");
        mManager.newVersusGame();
        Intent intent = new Intent(getApplicationContext(), NextPlayerActivity.class);
        startActivity(intent);
    }

    private void newSinglePlayerGame() {
        System.out.println("New single player game.");
        Intent intent = new Intent(getApplicationContext(), NewSinglePlayerGameMenuActivity.class);
        startActivity(intent);
    }

    private void showConfirmNewGameDialog() {
        NewGameConfirmDialog dialog = new NewGameConfirmDialog();
        dialog.show(getSupportFragmentManager(), "NewGameConfirmDialog");
    }
}
