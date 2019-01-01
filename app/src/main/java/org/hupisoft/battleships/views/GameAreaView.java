package org.hupisoft.battleships.views;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.ISquare;

public class GameAreaView extends GridLayout {

    private class SquareClickListener implements OnClickListener {
        private final Coordinate mLocation;

        public SquareClickListener(Coordinate c) {
            mLocation = c;
        }

        @Override
        public void onClick(View view) {
            System.out.println("Clicked square " + mLocation.toString());
        }
    }

    public GameAreaView(Context context) {
        super(context);
        setClickable(false);
    }

    public void setArea(IGameArea area) {
        removeAllViews();
        setColumnCount(area.width());
        setRowCount(area.height());

        for (int y = 0; y < area.height(); ++y) {
            for (int x = 0; x < area.width(); ++x) {
                Coordinate c = new Coordinate(x,y);
                ISquare sqr = area.getSquare(c);
                GameAreaSquareView square = new GameAreaSquareView(getContext(), sqr, true);
                SquareClickListener sqrListener = new SquareClickListener(c);
                square.setClickable(true);
                square.setOnClickListener(sqrListener);
                GridLayout.Spec rowSpec = spec(y, 1);
                GridLayout.Spec colSpec = spec(x, 1);
                addView(square, new GridLayout.LayoutParams(rowSpec, colSpec));
            }
        }
    }
}
