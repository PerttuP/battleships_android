package org.hupisoft.battleships;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import org.hupisoft.battleships.views.GameAreaView;
import org.hupisoft.battleships_core.GameLogicBuilder;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.Player;

public class BattleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        GameLogicBuilder builder = new GameLogicBuilder();
        IGameLogic logic = builder.createNewGame(12,8, new int[]{2,3,5});

        GameAreaView areaView = new GameAreaView(getApplicationContext());
        areaView.setArea(logic.getGameArea(Player.PLAYER_1));
        ViewGroup grp = findViewById(R.id.testLayout);
        grp.addView(areaView);
        //layout.addView();
    }
}
