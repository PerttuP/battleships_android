package org.hupisoft.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Main menu activity. Launcher activity for the application.
 */
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
        setNewGameButtonListener(R.id.newVersusGameBtn, IGameManager.GameType.VersusGame);
        setNewGameButtonListener(R.id.newSoloGameBtn, IGameManager.GameType.SinglePlayerGame);
    }

    private void setNewGameButtonListener(int buttonId, final IGameManager.GameType gameType) {
        Button btn = findViewById(buttonId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewGameRequested(gameType);
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
        mManager.newVersusGame();
        Intent intent = new Intent(getApplicationContext(), NextPlayerActivity.class);
        startActivity(intent);
    }

    private void newSinglePlayerGame() {
        Intent intent = new Intent(getApplicationContext(), NewSinglePlayerGameMenuActivity.class);
        startActivity(intent);
    }

    private void showConfirmNewGameDialog() {
        NewGameConfirmDialog dialog = new NewGameConfirmDialog();
        dialog.show(getSupportFragmentManager(), "NewGameConfirmDialog");
    }
}
