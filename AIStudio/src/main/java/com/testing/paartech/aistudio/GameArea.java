package com.testing.paartech.aistudio;

import com.testing.paartech.processingcomponents.BaseDrawable;

import org.hupisoft.battleships_ai.IHitProbabilityCalculator;
import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.IGameArea;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

/**
 * Shows a single game area.
 */
class GameArea extends BaseDrawable {

    List<List<GameSquare>> mSquares;

    /**
     * Constructor.
     * @param area Game area model.
     * @param calculator Hit probability calculator.
     * @param squareWidth Single square width.
     */
    GameArea(IGameArea area, IHitProbabilityCalculator calculator, int squareWidth) {
        setWidth(area.width() * squareWidth);
        setHeight(area.height() * squareWidth);
        mSquares = createArea(area, calculator, squareWidth);
    }

    @Override
    public void draw(int x, int y, PApplet parent) {
        int w = mSquares.get(0).get(0).width();
        for (int col = 0; col < mSquares.size(); ++col) {
            for (int row = 0; row < mSquares.get(0).size(); ++row) {
                mSquares.get(col).get(row).draw(x + col*w, y + row*w, parent);
            }
        }
    }

    private List<List<GameSquare>> createArea(IGameArea area, IHitProbabilityCalculator calculator, int squareWidth) {
        List<List<GameSquare>> columns = new ArrayList<>();
        for (int x = 0; x < area.width(); ++x) {
            List<GameSquare> col = new ArrayList<>();
            for (int y = 0; y < area.height(); ++y) {
                GameSquare sqr = new GameSquare(squareWidth);
                sqr.setSquare(area.getSquare(new Coordinate(x,y)));
                sqr.setCalculator(calculator, area.getRestrictedInstance());
                col.add(sqr);
            }
            columns.add(col);
        }
        return columns;
    }
}
