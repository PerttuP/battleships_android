package com.testing.paartech.aistudio;

import com.testing.paartech.processingcomponents.Button;
import com.testing.paartech.processingcomponents.CompositeDrawable;
import com.testing.paartech.processingcomponents.IClickListener;
import com.testing.paartech.processingcomponents.Label;

import org.hupisoft.battleships_ai.IHitProbabilityCalculator;
import org.hupisoft.battleships_core.IGameLogic;
import org.hupisoft.battleships_core.Player;

import processing.core.PApplet;

/**
 * Shows both game areas and executed actions.
 */
class BattleView extends CompositeDrawable {

    private IGameLogic mLogic;
    private Button mNextBtn;
    private Label mText;

    /**
     * Constructor.
     * @param logic Game logic.
     * @param calculator Hit probability calculator.
     * @param squareWidth Single square width.
     */
    BattleView(IGameLogic logic,
                      IHitProbabilityCalculator calculator,
                      int squareWidth) {
        super(logic.getGameArea(Player.PLAYER_1).width() * squareWidth,
                logic.getGameArea(Player.PLAYER_1).height() * squareWidth * 2 + 100, 0xffffff00);

        mLogic = logic;
        GameArea area1 = new GameArea(logic.getGameArea(Player.PLAYER_1), calculator, squareWidth);
        GameArea area2 = new GameArea(logic.getGameArea(Player.PLAYER_2), calculator, squareWidth);
        mNextBtn = new Button(area1.width(), 50, "Next");
        mNextBtn.setClickListener(new IClickListener() {
            @Override
            public void onClick() {
                System.out.println("Hello World!");
            }
        });

        mText = new Label("", area1.width(), 50);
        addDrawable(area2, 0, 0);
        addDrawable(mText, 0, area2.height());
        addDrawable(area1, 0, area2.height() + 50);
        addDrawable(mNextBtn, 0, area1.height() + area2.height() + 50);
    }

    /**
     * Set reaction for 'Next' button click.
     * @param listener Click listener to be called on 'Next' button click.
     */
    void setNextMoveCallback(IClickListener listener) {
        mNextBtn.setClickListener(listener);
    }

    @Override
    public void draw(int x, int y, PApplet parent) {
        setStatusText();
        super.draw(x, y, parent);
    }

    private void setStatusText() {
        if (mLogic.isGameOver()) {
            if (mLogic.getWinner() == Player.PLAYER_1) {
                mText.setText("Player 1 Won!");
            }
            else {
                mText.setText("Player 2 Won!");
            }
        }
        else {
            if (mLogic.getCurrentPlayer() == Player.PLAYER_1) {
                mText.setText("Player 1 Attacks");
            }
            else {
                mText.setText("Player 2 Attacks");
            }
        }
    }
}
