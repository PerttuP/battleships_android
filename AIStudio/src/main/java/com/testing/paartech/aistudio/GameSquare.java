package com.testing.paartech.aistudio;

import com.testing.paartech.processingcomponents.BaseDrawable;

import org.hupisoft.battleships_ai.IHitProbabilityCalculator;
import org.hupisoft.battleships_core.IRestrictedGameArea;
import org.hupisoft.battleships_core.ISquare;

import processing.core.PApplet;

/**
 * Draws a single square on GameArea.
 */
class GameSquare extends BaseDrawable {
    private ISquare mSquare;
    private IHitProbabilityCalculator mCalculator;
    private IRestrictedGameArea mArea;
    private int mProbabilityFactor;

    /**
     * Constructor.
     * @param sideWidth Pixel length of the side of the square.
     */
    GameSquare(int sideWidth) {
        mSquare = null;
        mCalculator = null;
        mProbabilityFactor = 0;
        setWidth(sideWidth);
        setHeight(sideWidth);
    }

    /**
     * Set square model. This has to be called before draw.
     * @param sqr Square model.
     */
    void setSquare(ISquare sqr) {
        mSquare = sqr;
    }

    /**
     * Set hit probability calculator. This has to be called before draw.
     * @param calculator Hit probability calculator.
     * @param area Restricted game area instance.
     */
    void setCalculator(IHitProbabilityCalculator calculator, IRestrictedGameArea area) {
        mCalculator = calculator;
        mArea = area;
    }

    @Override
    public void draw(int x, int y, PApplet parent) {
        drawBackground(x, y, parent);
        drawHitIndicator(x, y, parent);
        drawProbabilityFactor(x, y, parent);
    }

    private int getBgColor(PApplet applet) {
        int color = applet.color(0, 0, 255);
        if (mSquare.getShip() != null) {
            if (mSquare.getShip().isDestroyed()) {
                color = applet.color(150,150,150);
            }
            else {
                color = applet.color(200,200,200);
            }
        }
        return color;
    }

    private int getHitIndicatorColor(PApplet applet) {
        int color = applet.color(255);
        if (mSquare.getShip() != null) {
            color = applet.color(155, 0, 0);
        }
        return color;
    }

    private void drawBackground(int x, int y, PApplet parent) {
        parent.stroke(0);
        parent.fill(getBgColor(parent));
        parent.rect(x, y, width(), height());
    }

    private void drawHitIndicator(int x, int y, PApplet parent)
    {
        if (mSquare.isHit()) {
            parent.fill(getHitIndicatorColor(parent));
            parent.ellipse(x + width()/2, y + height()/2, width()/2 - 4, height()/2 - 4);
        }
    }

    private void drawProbabilityFactor(int x, int y, PApplet parent) {
        // Update probability factor until square is hit.
        if (!mSquare.isHit()) {
            mProbabilityFactor = mCalculator.getProbabilityFactor(mArea, mSquare.getLocation());
        }

        parent.fill(255, 171, 15);
        parent.textSize(height()/2);
        parent.text(Integer.toString(mProbabilityFactor), x+3, y+3, width()-6, height()-6);
    }
}
