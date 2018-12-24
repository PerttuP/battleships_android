package org.hupisoft.battleships_ai;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IRestrictedGameArea;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultShipDestroyerTest {

    private DefaultShipDestroyer mDestroyer = null;
    private IRestrictedGameArea mArea = null;
    private IHitProbabilityCalculator mCalculator = null;
    private Coordinate mOrigin = null;

    @Before
    public void setUp() {
        mDestroyer = new DefaultShipDestroyer();
        mArea = mock(IRestrictedGameArea.class);
        mCalculator = mock(IHitProbabilityCalculator.class);
        mOrigin = new Coordinate(3,4);
    }

    private void whenInitialized(int pNorth, int pSouth, int pWest, int pEast) {
        when(mCalculator.getProbabilityFactor(mArea, new Coordinate(mOrigin.x(),mOrigin.y()-1))).thenReturn(pNorth);
        when(mCalculator.getProbabilityFactor(mArea, new Coordinate(mOrigin.x(),mOrigin.y()+1))).thenReturn(pSouth);
        when(mCalculator.getProbabilityFactor(mArea, new Coordinate(mOrigin.x()-1,mOrigin.y()))).thenReturn(pWest);
        when(mCalculator.getProbabilityFactor(mArea, new Coordinate(mOrigin.x()+1,mOrigin.y()))).thenReturn(pEast);
    }

    private void verifyInitialized()
    {
        verify(mCalculator).getProbabilityFactor(mArea, new Coordinate(mOrigin.x(),mOrigin.y()-1));
        verify(mCalculator).getProbabilityFactor(mArea, new Coordinate(mOrigin.x(),mOrigin.y()+1));
        verify(mCalculator).getProbabilityFactor(mArea, new Coordinate(mOrigin.x()-1, mOrigin.y()));
        verify(mCalculator).getProbabilityFactor(mArea, new Coordinate(mOrigin.x()+1 ,mOrigin.y()));
    }

    @Test
    public void testConstructor() {
        DefaultShipDestroyer destroyer = new DefaultShipDestroyer();
        assertNull(destroyer.getCandidates());
    }

    @Test
    public void destroyerHas4CandidatesAfterInitIfNoRestrictions() {
        whenInitialized(1,2,3,4);

        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(4, candidates.size());
        assertTrue(candidates.contains(new Coordinate(3,3)));
        assertTrue(candidates.contains(new Coordinate(3,5)));
        assertTrue(candidates.contains(new Coordinate(2,4)));
        assertTrue(candidates.contains(new Coordinate(4,4)));

        verifyInitialized();
    }

    @Test
    public void candidateIsExcludedIfProbabilityIs0() {
        whenInitialized(0,2,3,0);

        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(3,5)));
        assertTrue(candidates.contains(new Coordinate(2,4)));

        verifyInitialized();
    }

    @Test
    public void candidateWightHighestProbabilityIsSelected() {
        whenInitialized(0,2,3,0);

        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        assertEquals(new Coordinate(2,4), mDestroyer.getNextTarget());
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(3,5)));
        assertTrue(candidates.contains(new Coordinate(2,4)));

        verify(mCalculator, times(2)).getProbabilityFactor(mArea, new Coordinate(mOrigin.x(),mOrigin.y()+1));
        verify(mCalculator, times(2)).getProbabilityFactor(mArea, new Coordinate(mOrigin.x()-1, mOrigin.y()));
    }

    @Test
    public void candidateIsRemovedIfMissed() {
        whenInitialized(0,2,3,0);
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.EMPTY, new Coordinate(2,4));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(1, candidates.size());
        assertTrue(candidates.contains(new Coordinate(3,5)));
    }

    @Test
    public void candidatesAreUpdatedIfHit1() {
        whenInitialized(1,2,3,4);
        when(mCalculator.getProbabilityFactor(mArea, new Coordinate(5,4))).thenReturn(3);
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.SHIP_HIT, new Coordinate(4,4));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(2,4)));
        assertTrue(candidates.contains(new Coordinate(5,4)));
    }

    @Test
    public void candidatesAreUpdatedIfHit2() {
        whenInitialized(2,1,4,3);
        when(mCalculator.getProbabilityFactor(mArea, new Coordinate(1,4))).thenReturn(3);
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.SHIP_HIT, new Coordinate(2,4));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(1,4)));
        assertTrue(candidates.contains(new Coordinate(4,4)));
    }

    @Test
    public void candidatesAreUpdatedIfHit3() {
        whenInitialized(1,4,3,2);
        when(mCalculator.getProbabilityFactor(mArea, new Coordinate(3,6))).thenReturn(3);
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.SHIP_HIT, new Coordinate(3,5));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(3,3)));
        assertTrue(candidates.contains(new Coordinate(3,6)));
    }

    @Test
    public void candidatesAreUpdatedIfHit4() {
        whenInitialized(4,3,2,1);
        when(mCalculator.getProbabilityFactor(mArea, new Coordinate(3,2))).thenReturn(3);
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.SHIP_HIT, new Coordinate(3,3));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(3,2)));
        assertTrue(candidates.contains(new Coordinate(3,5)));
    }

    @Test
    public void candidateIsOnlyRemovedWhenNewCandidateHasProbability0() {
        whenInitialized(0,2,3,3);
        when(mCalculator.getProbabilityFactor(mArea, new Coordinate(1,4))).thenReturn(0);
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.SHIP_HIT, new Coordinate(2,4));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(1, candidates.size());
        assertTrue(candidates.contains(new Coordinate(4,4)));
    }
}