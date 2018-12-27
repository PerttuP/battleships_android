package com.testing.paartech.processingcomponents;

import processing.core.PApplet;

/**
 * Simple text label.
 */
public class Label extends BaseDrawable {

    private String mText;
    private int mTextColor;
    private int mBgColor;

    /**
     * Constructor.
     * @param text Label text.
     * @param width Label width in pixels.
     * @param height Label height in pixels.
     */
    public Label(String text, int width, int height) {
        mText = text;
        setWidth(width);
        setHeight(height);
        mTextColor = 0;
        mBgColor = 0xffffffff;
    }

    /**
     * Set label background color.
     * @param color New background color. See processing color-type.
     */
    public void setBgColor(int color) {
        mBgColor = color;
    }

    /**
     * Set text color.
     * @param color New text color. See processing color-type.
     */
    public void setTextColor(int color) {
        mTextColor = color;
    }

    /**
     * Set label text.
     * @param text New label text.
     */
    public void setText(String text) {
        mText = text;
    }

    @Override
    public void draw(int x, int y, PApplet parent) {
        parent.stroke(mTextColor);
        parent.fill(mBgColor);
        parent.rect(x,y, width(), height());
        parent.textSize(height());
        parent.fill(mTextColor);
        parent.text(mText, x, y, x+width(), y+height());
    }
}
