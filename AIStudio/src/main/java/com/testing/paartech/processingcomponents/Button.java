package com.testing.paartech.processingcomponents;

import processing.core.PApplet;

/**
 * Simple button with text.
 */
public class Button extends BaseDrawable {
    private int mBgColor;
    private int mStrokeColor;
    private Label mText;
    private IClickListener mListener = null;

    /**
     * Constructor.
     * @param w Button width.
     * @param h Button height.
     * @param text Button text.
     */
    public Button(int w, int h, String text)
    {
        setWidth(w);
        setHeight(h);
        mBgColor = 0xffffff00;
        mStrokeColor = 0;
        mText = new Label(text, width()-4, height()-4);
        mText.setBgColor(mBgColor);
        mText.setTextColor(mStrokeColor);
    }

    /**
     * Set button background color.
     * @param color Background color. See processing color-type.
     */
    public void setBgColor(int color) {
        mBgColor = color;
        mText.setBgColor(color);
    }

    /**
     * Set Button stroke color.
     * @param color Stroke color. See processing color-type.
     */
    public  void setStrokeColor(int color) {
        mStrokeColor = color;
        mText.setTextColor(color);
    }

    @Override
    public void draw(int x, int y, PApplet parent) {
        parent.stroke(mStrokeColor);
        parent.fill(mBgColor);
        parent.rect(x, y, width(), height());
        parent.rect(x+2, y+2, width()-4, height()-4);
        mText.draw(x+2, y+2, parent);
    }
}
