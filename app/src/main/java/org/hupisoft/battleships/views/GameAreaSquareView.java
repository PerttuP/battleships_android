package org.hupisoft.battleships.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.hupisoft.battleships_core.ISquare;

public class GameAreaSquareView extends View {

    private Rect mRect = null;
    private Paint mPaint = null;
    private ISquare mSquare = null;

    public GameAreaSquareView(Context context) {
        super(context);
        init(null);
    }

    public GameAreaSquareView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GameAreaSquareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public GameAreaSquareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void setSquare(ISquare square) {
        mSquare = square;
    }

    public ISquare getSquare() {
        return mSquare;
    }

    private void init(@Nullable AttributeSet attrs) {
        mRect = new Rect();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRect.left = this.getLeft();
        mRect.top = this.getTop();
        int width = Math.min(this.getWidth(), this.getHeight());
        mRect.right = mRect.bottom = width;

        drawBackground(canvas);
        drawContent(canvas);

        super.onDraw(canvas);
    }

    private void drawBackground(Canvas canvas) {
        // fill
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(mRect, mPaint);
        // border
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(mRect, mPaint);
    }

    private void drawContent(Canvas canvas) {
        if (mSquare != null && mSquare.isHit()) {
            // Draw hit indicator
            int color = hitIndicatorColor(mSquare);
            mPaint.setColor(color);
            int mid = mRect.width() / 2;
            canvas.drawCircle(mid, mid, mid - 2, mPaint);
        }
    }

    private int hitIndicatorColor(ISquare square) {
        int color = Color.WHITE;
        if (square.getShip() != null) {
            if (square.getShip().isDestroyed()) {
                color = Color.BLACK;
            } else {
                color = Color.RED;
            }
        }
        return color;
    }
}
