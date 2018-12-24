package org.hupisoft.battleships_ai;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IRestrictedGameArea;
import org.hupisoft.battleships_core.IShip;

import java.util.ArrayList;
import java.util.List;

/**
 * Default ship destroyer. Selects most probable candidate.
 */
class DefaultShipDestroyer implements IShipDestroyer {

    private IRestrictedGameArea mArea;
    private Coordinate mOrigin;
    private IHitProbabilityCalculator mCalculator;
    private List<Coordinate> mCandidates;

    /**
     * Constructor.
     */
    DefaultShipDestroyer() {
        mArea = null;
        mOrigin = null;
        mCalculator = null;
        mCandidates = null;
    }

    /**
     * Get list of current candidates.
     * @return List of current candidates.
     */
    List<Coordinate> getCandidates() {
        return mCandidates;
    }

    @Override
    public void initialize(IRestrictedGameArea area, Coordinate origin, IHitProbabilityCalculator calculator) {
        mArea = area;
        mOrigin = origin;
        mCalculator = calculator;
        mCandidates = getInitialCandidates(origin);
    }

    @Override
    public Coordinate getNextTarget() {
        // Return candidate with highest probability.
        Coordinate target = null;
        int topProbability = 0;
        for (Coordinate c : mCandidates) {
            int p = mCalculator.getProbabilityFactor(mArea, c);
            if (p > topProbability) {
                target = c;
                topProbability = p;
            }
        }
        return target;
    }

    @Override
    public void confirmAction(HitResult result, Coordinate location) {
        // Update candidates.
        IShip.Orientation orientation = null;
        if (result == HitResult.SHIP_HIT)
        {
            if (location.x() == mOrigin.x()) {
                orientation = IShip.Orientation.VERTICAL;
            }
            else {
                orientation = IShip.Orientation.HORIZONTAL;
            }
            updateCandidate(location);
        }
        else
        {
            mCandidates.remove(location);
        }

        removeCandidatesOfWrongOrientation(mOrigin, orientation, mCandidates);
    }

    private List<Coordinate> getInitialCandidates(Coordinate origin) {
        List<Coordinate> candidates = new ArrayList<>();
        List<Coordinate> aux = new ArrayList<>();
        aux.add(new Coordinate(origin.x()-1, origin.y()));
        aux.add(new Coordinate(origin.x()+1, origin.y()));
        aux.add(new Coordinate(origin.x(), origin.y()-1));
        aux.add(new Coordinate(origin.x(), origin.y()+1));

        for (Coordinate c : aux) {
            if (mCalculator.getProbabilityFactor(mArea, c) != 0) {
                candidates.add(c);
            }
        }
        return candidates;
    }

    private void removeCandidatesOfWrongOrientation(Coordinate origin,
                                                    IShip.Orientation orientation,
                                                    List<Coordinate> candidates) {
        if (orientation == IShip.Orientation.HORIZONTAL) {
            candidates.remove(new Coordinate(origin.x(), origin.y()-1));
            candidates.remove(new Coordinate(origin.x(), origin.y()+1));
        }
        else if (orientation == IShip.Orientation.VERTICAL) {
            candidates.remove(new Coordinate(origin.x()-1, origin.y()));
            candidates.remove(new Coordinate(origin.x()+1, origin.y()));
        }
    }

    private void updateCandidate(Coordinate oldCandidate) {
        Coordinate newCandidate = null;
        if (oldCandidate.x() < mOrigin.x()) {
            newCandidate = new Coordinate(oldCandidate.x()-1, oldCandidate.y());
        }
        else if (oldCandidate.x() > mOrigin.x()) {
            newCandidate = new Coordinate(oldCandidate.x()+1, oldCandidate.y());
        }
        else if (oldCandidate.y() < mOrigin.y()) {
            newCandidate = new Coordinate(oldCandidate.x(), oldCandidate.y()-1);
        }
        else if (oldCandidate.y() > mOrigin.y()) {
            newCandidate = new Coordinate(oldCandidate.x(), oldCandidate.y()+1);
        }
        mCandidates.remove(oldCandidate);
        if (mCalculator.getProbabilityFactor(mArea, newCandidate) != 0)
        mCandidates.add(newCandidate);
    }
}
