package com.testing.paartech.aistudio;

import com.testing.paartech.processingcomponents.IClickListener;

import org.hupisoft.battleships_ai.BattleShipAIFactory;
import org.hupisoft.battleships_ai.HitProbabilityCalculator;
import org.hupisoft.battleships_ai.IBattleShipsAI;
import org.hupisoft.battleships_core.GameLogicBuilder;
import org.hupisoft.battleships_core.IGameLogic;

import processing.core.PApplet;

/**
 * AI Studio is a simple development tool to test and visualize battleships game AI.
 * Tool is made with Processing 3.4.
 */
public class MainClass extends PApplet {

    private BattleView mMainView;
    private GameRunner mRunner;

    public static void main(String[] args)
    {
        PApplet.main("com.testing.paartech.aistudio.MainClass");
    }

    @Override
    public void settings(){
        size(600,900);
    }

    @Override
    public void setup(){
        GameLogicBuilder builder = new GameLogicBuilder();
        BattleShipAIFactory aiFactory = new BattleShipAIFactory();
        IGameLogic logic = builder.createNewGame(12, 8, new int[]{2,3,5});

        // Hard-coded AI-selection. Edit here if need to change AI.
        IBattleShipsAI ai1 = aiFactory.createAI(BattleShipAIFactory.PROBABILITY_AI);
        IBattleShipsAI ai2 = aiFactory.createAI(BattleShipAIFactory.PROBABILITY_AI);

        mRunner = new GameRunner(logic, ai1, ai2);
        mMainView = new BattleView(logic, new HitProbabilityCalculator(), 50);
        mMainView.setNextMoveCallback(new IClickListener() {
            @Override
            public void onClick() {
                mRunner.progressOneStep();
            }
        });
    }

    @Override
    public void draw(){
        mMainView.draw(0,0, this);
    }

    @Override
    public void mousePressed() {
        mMainView.clicked(mouseX, mouseY);
    }
}
