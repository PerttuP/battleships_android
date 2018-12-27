package com.testing.paartech.processingcomponents;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

/**
 * Drawable object composed of other drawable objects.
 */
public class CompositeDrawable extends BaseDrawable {

    private class SubView{
        int x;
        int y;
        IDrawable drawable;

        SubView(int x, int y, IDrawable drawable) {
            this.x = x;
            this.y = y;
            this.drawable = drawable;
        }
    }

    private int mBgColor;
    private List<SubView> mSubViews;

    /**
     * Constructor.
     * @param width Object width.
     * @param height Object height.
     * @param bgColor Object background color.
     */
    public CompositeDrawable(int width, int height, int bgColor) {
        setWidth(width);
        setHeight(height);
        mBgColor = bgColor;
        mSubViews = new ArrayList<>();
    }

    /**
     * Add drawable object to composite view.
     * @param drawable Drawable object to be added.
     * @param x Relative x-coordinate inside this view.
     * @param y Relative y-coordinate inside this view.
     */
    public void addDrawable(IDrawable drawable, int x, int y) {
        mSubViews.add(new SubView(x, y, drawable));
    }

    @Override
    public void draw(int x, int y, PApplet parent) {
        parent.fill(mBgColor);
        parent.rect(x, y, width(), height());

        for (SubView sv : mSubViews) {
            sv.drawable.draw(x+ sv.x, y + sv.y, parent);
        }
    }

    @Override
    public void clicked(int x, int y)
    {
        super.clicked(x,y);

        for (SubView sv : mSubViews) {
            if (x > sv.x && x < sv.x + sv.drawable.width()
                    && y > sv.y &&  y < sv.y + sv.drawable.height())
            {
                sv.drawable.clicked(x - sv.x, y - sv.y);
            }
        }
    }
}
