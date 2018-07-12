package org.hupisoft.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button newVersusGameBtn = findViewById(R.id.newVersusGameBtn);
        newVersusGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toBattleViewIntent = new Intent(getApplicationContext(), BattleActivity.class);
                startActivity(toBattleViewIntent);
            }
        });
    }
}
