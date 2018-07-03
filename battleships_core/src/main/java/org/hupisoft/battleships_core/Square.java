package org.hupisoft.battleships_core;

import static org.mockito.Mockito.*;

class Square implements ISquare {

    private Coordinate mLocation = null;
    private IShip mShip = null;
    private boolean mIsHit = false;

    Square(Coordinate location) {
        mLocation = location;
    }

    @Override
    public boolean isHit() {
        return mIsHit;
    }

    @Override
    public HitResult hit() {
        HitResult result = HitResult.EMPTY;
        if (mIsHit) {
            result = HitResult.ALREADY_HIT;
        } else {
            mIsHit = true;
            if (mShip != null) {
                if (mShip.hit()) {
                    result = HitResult.DESTROYED_SHIP;
                } else {
                    result = HitResult.HIT_SHIP;
                }
            }
        }
        return result;
    }

    @Override
    public IShip getShip() {
        return mShip;
    }

    @Override
    public void setShip(IShip ship) {
        mShip = ship;
    }

    @Override
    public Coordinate getLocation() {
        return mLocation;
    }
}
