package org.hupisoft.battleships.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import org.hupisoft.battleships_core.ISquare;

public class GameAreaSquareView extends View {

    private ISquare mSquare;
    private Paint mBgPaint;
    private Paint mBorderPaint;
    private Paint mHitPaint;
    private boolean mShowHiddenShips;

    public GameAreaSquareView(Context context, ISquare square, boolean showHiddenShips) {
        super(context);
        init(square, showHiddenShips);
    }

    private void init(ISquare square, boolean showHiddenShips) {
        mSquare = square;
        mShowHiddenShips = showHiddenShips;

        mBgPaint = new Paint();
        mHitPaint = new Paint();
        mBorderPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(Color.BLUE);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawHitIndicator(canvas);
    }

    private void drawBackground(Canvas canvas) {
        if (mSquare.getShip() != null && mSquare.getShip().isDestroyed()) {
            mBgPaint.setColor(Color.BLACK);
        }
        else if (mSquare.getShip() != null && mShowHiddenShips) {
            mBgPaint.setColor(Color.GRAY);
        }
        else {
            mBgPaint.setColor(Color.BLUE);
        }
        drawBoundingRect(canvas, mBorderPaint, mBgPaint);
    }

    private void drawBoundingRect(Canvas canvas, Paint stroke, Paint fill) {
        float x1 = getTranslationX();
        float x2 = getTranslationX() + getWidth();
        float y1 = getTranslationY();
        float y2 = getTranslationY() + getHeight();

        canvas.drawRect(x1, y1, x2, y2, fill);
        canvas.drawRect(x1, y1, x2, y2, stroke);
    }

    private void drawHitIndicator(Canvas canvas) {
        if (mSquare.isHit()) {
            if (mSquare.getShip() != null){
                mHitPaint.setColor(Color.RED);
            }
            else {
                mHitPaint.setColor(Color.WHITE);
            }

            float x = getTranslationX() + getWidth()/2;
            float y = getTranslationX() + getHeight()/2;
            float r = getWidth()/2 - 3;
            canvas.drawCircle(x, y, r, mHitPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getMeasurement(widthMeasureSpec);
        int height = getMeasurement(heightMeasureSpec);
        int side = Math.min(width, height);
        setMeasuredDimension(side, side);
    }

    private int getMeasurement(int measureSpec) {
        int measured = 100;
        if (MeasureSpec.getMode(measureSpec) == MeasureSpec.AT_MOST) {
            measured = Math.min(measured, MeasureSpec.getSize(measureSpec));
        }
        else if (MeasureSpec.getMode(measureSpec) == MeasureSpec.EXACTLY) {
            measured = MeasureSpec.getSize(measureSpec);
        }
        return measured;
    }
}
