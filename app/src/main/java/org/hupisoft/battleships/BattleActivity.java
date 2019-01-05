package org.hupisoft.battleships;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import org.hupisoft.battleships.views.GameAreaView;

public class BattleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        IGameManager manager = (IGameManager)getApplicationContext();
        GameAreaView areaView = new GameAreaView(getApplicationContext());
        areaView.setArea(manager.currentGameLogic().getGameArea(manager.currentGameLogic().getCurrentPlayer()));
        ViewGroup grp = findViewById(R.id.testLayout);
        grp.addView(areaView);
        //layout.addView();
    }
}
