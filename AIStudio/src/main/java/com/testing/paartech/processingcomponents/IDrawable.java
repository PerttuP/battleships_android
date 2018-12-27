package com.testing.paartech.processingcomponents;

import processing.core.PApplet;

/**
 * Common interface for drawable objects.
 */
public interface IDrawable {

    /**
     *
     * @param x X-coordinate for top-left corner.
     * @param y Y-coordinate for top-left corner.
     */
    void draw(int x, int y, PApplet parent);

    /**
     * IDrawable object's width.
     * @return Width in px.
     */
    int width();

    /**
     * IDrawable object's height.
     * @return Height in px.
     */
    int height();

    /**
     * Called when drawable has been clicked.
     * @param x Mouse x-coordinate relative to drawable top-left corner.
     * @param y Mouse y-coordinate relative to drawable top-left corner.
     */
    void clicked(int x, int y);

    /**
     * Set click listener. Listener will be called when drawable is clicked. Set null to remove
     * listener.
     * @param listener click listener.
     */
    void setClickListener(IClickListener listener);
}
