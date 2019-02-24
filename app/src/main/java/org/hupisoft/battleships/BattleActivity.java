package org.hupisoft.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.arch.lifecycle.ViewModelProviders;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.hupisoft.battleships.views.GameAreaView;
import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.Player;

public class BattleActivity extends AppCompatActivity implements GameAreaView.IGameAreaClickListener {

    private BattleViewModel mModel;
    private IGameManager mGameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = ViewModelProviders.of(this).get(BattleViewModel.class);
        mGameManager = (IGameManager)getApplicationContext();
        setContentView(R.layout.activity_battle);
        initToggleButton();
        initEndTurnButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGameArea();
        setToggleButtonText();
        setStatusInformation();
        setEndTurnButtonEnabled(mModel.isHitPerformed());
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

    private void initEndTurnButton() {
        Button endTurnBtn = findViewById(R.id.endTurnButton);
        endTurnBtn.setEnabled(false);
        endTurnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTurn();
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
        if (mModel.isHitPerformed()) {
            showShips = !showShips;
        }
        GameAreaView.IGameAreaClickListener areaListener = showShips ? null : this;
        areaView.setArea(area, showShips, areaListener);
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

        // If hit is performed, invert selection
        if (mModel.isHitPerformed()) {
            if (displayedPlayer == Player.PLAYER_1) {
                displayedPlayer = Player.PLAYER_2;
            }
            else {
                displayedPlayer = Player.PLAYER_1;
            }
        }

        return displayedPlayer;
    }

    private void setStatusInformation() {
        setCurrentPlayerStatus();
        setNumberOfHitsStatus();
        setInstructionText();
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

    private void setInstructionText() {
        TextView instructions = findViewById(R.id.battleInstructionTextView);
        HitResult result = mModel.getHitResult();

        if (!mModel.isHitPerformed()) {
            instructions.setText(R.string.battleView_instruction_tap);
        }
        else if (result == HitResult.VICTORY ||result == HitResult.SHIP_DESTROYED) {
            instructions.setText(R.string.battleView_instruction_destroy);
        }
        else if (result == HitResult.EMPTY) {
            instructions.setText(R.string.battleView_instruction_miss);
        }
        else if (result == HitResult.SHIP_HIT) {
            instructions.setText(R.string.battleView_instruction_hit);
        }
    }

    private void setEndTurnButtonEnabled(boolean enabled) {
        Button endTurnBtn = findViewById(R.id.endTurnButton);
        endTurnBtn.setEnabled(enabled);
    }

    private void endTurn() {
        Intent nextPlayerIntent = new Intent(getApplicationContext(), NextPlayerActivity.class);
        startActivity(nextPlayerIntent);
    }

    @Override
    public void squareClicked(Coordinate location) {
        if (!mModel.isHitPerformed()) {
            HitResult result = mGameManager.currentGameLogic().playerAction(location);
            mModel.setHitResult(result);
            setEndTurnButtonEnabled(mModel.isHitPerformed());
            setGameArea();
            setStatusInformation();
        }
    }
}
