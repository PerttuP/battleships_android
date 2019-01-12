package org.hupisoft.battleships.views;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IGameArea;
import org.hupisoft.battleships_core.ISquare;

public class GameAreaView extends GridLayout {

    private static final String[] ALPHABETS = {
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"
    };

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
        init();
    }

    public GameAreaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setClickable(false);
    }

    public void setArea(IGameArea area) {
        removeAllViews();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setColumnCount(area.height() + 1);
            setRowCount(area.width() + 1);
        }
        else {
            setColumnCount(area.width() + 1);
            setRowCount(area.height() + 1);
        }

        setCoordinates(area.width(), area.height());
        setSquares(area);
    }

    private void setCoordinates(int width, int height) {
        setHorizontalCoordinates(width);
        setVerticalCoordinates(height);
    }

    private void setHorizontalCoordinates(int width) {
        for (int x = 0; x < width; ++x) {
            View view = createCoordinateView(ALPHABETS[x]);

            GridLayout.Spec rowSpec = spec(0, 1);
            GridLayout.Spec colSpec = spec(x+1, 1);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                rowSpec = spec(x+1, 1);
                colSpec = spec(0, 1);
            }

            addView(view, new GridLayout.LayoutParams(rowSpec, colSpec));
        }
    }

    private void setVerticalCoordinates(int height) {
        for (int y = 0; y < height; ++y) {
            View view = createCoordinateView(Integer.toString(y+1));
            GridLayout.Spec rowSpec = spec(y+1, 1);
            GridLayout.Spec colSpec = spec(0, 1);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                rowSpec = spec(0, 1);
                colSpec = spec(y+1, 1);
            }
            addView(view, new GridLayout.LayoutParams(rowSpec, colSpec));
        }
    }

    private View createCoordinateView(String text) {
        TextView view = new TextView(getContext());
        view.setText(text);
        return view;
    }

    private void setSquares(IGameArea area) {
        for (int y = 0; y < area.height(); ++y) {
            for (int x = 0; x < area.width(); ++x) {
                Coordinate c = new Coordinate(x,y);
                ISquare sqr = area.getSquare(c);
                GameAreaSquareView square = new GameAreaSquareView(getContext(), sqr, true);
                SquareClickListener sqrListener = new SquareClickListener(c);
                square.setClickable(true);
                square.setOnClickListener(sqrListener);
                GridLayout.Spec rowSpec = spec(y+1, 1);
                GridLayout.Spec colSpec = spec(x+1, 1);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    rowSpec = spec(x+1, 1);
                    colSpec = spec(y+1, 1);
                }
                addView(square, new GridLayout.LayoutParams(rowSpec, colSpec));
            }
        }
    }
}
