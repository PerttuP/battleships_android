package org.hupisoft.battleships;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.arch.lifecycle.ViewModelProviders;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.hupisoft.battleships.views.GameAreaView;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.Player;

public class BattleActivity extends AppCompatActivity {

    private BattleViewModel mModel;
    private IGameManager mGameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = ViewModelProviders.of(this).get(BattleViewModel.class);
        mGameManager = (IGameManager)getApplicationContext();
        setContentView(R.layout.activity_battle);
        initToggleButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGameArea();
        setToggleButtonText();
        setStatusInformation();
    }

    private void initToggleButton() {
        Button button = findViewById(R.id.toggleAreaButton);
        setToggleButtonText();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleViewState();
            }
        });
    }

    private void toggleViewState() {
        mModel.toggleState();
        setToggleButtonText();
        setGameArea();
    }

    private void setToggleButtonText() {
        Button button = findViewById(R.id.toggleAreaButton);

        if (mModel.getState() == BattleViewModel.ViewState.ShowFriendlyArea) {
            button.setText(R.string.battleView_showEnemyArea);
        }
        else {
            button.setText(R.string.battleView_showFriendlyArea);
        }
    }

    private void setGameArea() {
        GameAreaView areaView = findViewById(R.id.gameAreaView);
        Player displayedPlayer = getPlayerToBeShown();
        IGameArea area = mGameManager.currentGameLogic().getGameArea(displayedPlayer);
        boolean showShips = mGameManager.currentGameLogic().getCurrentPlayer() == displayedPlayer;
        areaView.setArea(area, showShips);
    }

    private Player getPlayerToBeShown() {
        Player currentPlayer = mGameManager.currentGameLogic().getCurrentPlayer();
        Player displayedPlayer = Player.PLAYER_1;

        if (currentPlayer == Player.PLAYER_1) {
            if (mModel.getState() == BattleViewModel.ViewState.ShowEnemyArea) {
                displayedPlayer = Player.PLAYER_2;
            }
        }
        else if (mModel.getState() == BattleViewModel.ViewState.ShowFriendlyArea) {
            displayedPlayer = Player.PLAYER_2;
        }

        return displayedPlayer;
    }

    private void setStatusInformation() {
        setCurrentPlayerStatus();
        setNumberOfHitsStatus();
    }

    private void setCurrentPlayerStatus() {
        TextView currentPlayerText = findViewById(R.id.playerInTurnTextView);
        if (mGameManager.currentGameLogic().getCurrentPlayer() == Player.PLAYER_1) {
            currentPlayerText.setText(R.string.battle_player1);
        }
        else {
            currentPlayerText.setText(R.string.battle_player2);
        }
    }

    private void setNumberOfHitsStatus() {
        TextView numberOfHitsText = findViewById(R.id.numberOfHitsValueTextView);
        IGameLogic logic = mGameManager.currentGameLogic();
        numberOfHitsText.setText(Integer.toString(logic.getNumberOfHits(logic.getCurrentPlayer())));
    }
}
