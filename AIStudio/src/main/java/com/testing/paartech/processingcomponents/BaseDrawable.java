package com.testing.paartech.processingcomponents;

/**
 * Base implementation for all IDrawable implementations.
 */
public abstract class BaseDrawable implements IDrawable {

    private IClickListener mListener = null;
    private int mWidth = 0;
    private int mHeight = 0;

    /**
     * Get currently set click listener.
     * @return Currently set click listener.
     */
    protected IClickListener getClickListener() {
        return  mListener;
    }

    /**
     * Set object width.
     * @param width Width in pixels.
     */
    protected final void setWidth(int width) {
        mWidth = width;
    }

    /**
     * Set object height.
     * @param height Height in pixels.
     */
    protected final void setHeight(int height) {
        mHeight = height;
    }

    @Override
    public void setClickListener(IClickListener listener) {
        mListener = listener;
    }

    @Override
    public void clicked(int x, int y) {
        if (mListener != null) {
            mListener.onClick();
        }
    }

    @Override
    public final int width() {
        return mWidth;
    }

    @Override
    public final int height() {
        return mHeight;
    }
}
