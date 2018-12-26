package org.hupisoft.battleships_ai;

import org.hupisoft.battleships_core.Coordinate;
import org.hupisoft.battleships_core.HitResult;
import org.hupisoft.battleships_core.IRestrictedGameArea;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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
        List<Integer> shipLengths = new ArrayList<>();
        shipLengths.add(2);
        shipLengths.add(3);
        mDestroyer = new DefaultShipDestroyer();
        mArea = mock(IRestrictedGameArea.class);
        when(mArea.remainingShipLengths()).thenReturn(shipLengths);
        mCalculator = mock(IHitProbabilityCalculator.class);
        mOrigin = new Coordinate(3,4);
    }

    private void whenInitialized(int[] pNorth, int[] pSouth, int[] pWest, int[] pEast) {
        for (int i = 1; i < 3; ++i) {
            when(mCalculator.getProbabilityFactor(mArea, new Coordinate(mOrigin.x(), mOrigin.y() - i))).thenReturn(pNorth[i-1]);
            when(mCalculator.getProbabilityFactor(mArea, new Coordinate(mOrigin.x(), mOrigin.y() + i))).thenReturn(pSouth[i-1]);
            when(mCalculator.getProbabilityFactor(mArea, new Coordinate(mOrigin.x() - i, mOrigin.y()))).thenReturn(pWest[i-1]);
            when(mCalculator.getProbabilityFactor(mArea, new Coordinate(mOrigin.x() + i, mOrigin.y()))).thenReturn(pEast[i-1]);
        }
    }

    private void verifyInitialized()
    {
        for (int i = 1; i < 3; ++i) {
            verify(mCalculator).getProbabilityFactor(mArea, new Coordinate(mOrigin.x(), mOrigin.y() - i));
            verify(mCalculator).getProbabilityFactor(mArea, new Coordinate(mOrigin.x(), mOrigin.y() + i));
            verify(mCalculator).getProbabilityFactor(mArea, new Coordinate(mOrigin.x() - i, mOrigin.y()));
            verify(mCalculator).getProbabilityFactor(mArea, new Coordinate(mOrigin.x() + i, mOrigin.y()));
        }
        verifyNoMoreInteractions(mCalculator);
    }

    @Test
    public void testConstructor() {
        DefaultShipDestroyer destroyer = new DefaultShipDestroyer();
        assertNull(destroyer.getCandidates());
    }

    @Test
    public void destroyerHas4CandidatesAfterInitIfNoRestrictions() {
        whenInitialized(new int[]{1,1,1,}, new int[]{2,2,2}, new int[]{3,3,3}, new int[]{4,4,4});

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
        whenInitialized(new int[]{0,1,1,}, new int[]{2,2,2}, new int[]{3,3,3}, new int[]{0,4,4});

        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(3,5)));
        assertTrue(candidates.contains(new Coordinate(2,4)));

        verifyInitialized();
    }

    @Test
    public void candidateWightHighestProbabilityIsSelected() {
        whenInitialized(new int[]{0,1,1,}, new int[]{2,2,2}, new int[]{3,3,3}, new int[]{0,4,4});

        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        assertEquals(new Coordinate(2,4), mDestroyer.getNextTarget());
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(3,5)));
        assertTrue(candidates.contains(new Coordinate(2,4)));

        verifyInitialized();
    }

    @Test
    public void candidateIsRemovedIfMissed() {
        whenInitialized(new int[]{0,1,1,}, new int[]{2,2,2}, new int[]{3,3,3}, new int[]{0,4,4});
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.EMPTY, new Coordinate(2,4));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(1, candidates.size());
        assertTrue(candidates.contains(new Coordinate(3,5)));
        verifyInitialized();
    }

    @Test
    public void candidatesAreUpdatedIfHit1() {
        whenInitialized(new int[]{1,1,1,}, new int[]{2,2,2}, new int[]{3,3,3}, new int[]{4,4,4});
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.SHIP_HIT, new Coordinate(4,4));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(2,4)));
        assertTrue(candidates.contains(new Coordinate(5,4)));
        verifyInitialized();
    }

    @Test
    public void candidatesAreUpdatedIfHit2() {
        whenInitialized(new int[]{2,1,1,}, new int[]{1,2,2}, new int[]{4,3,3}, new int[]{3,4,4});
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.SHIP_HIT, new Coordinate(2,4));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(1,4)));
        assertTrue(candidates.contains(new Coordinate(4,4)));
        verifyInitialized();
    }

    @Test
    public void candidatesAreUpdatedIfHit3() {
        whenInitialized(new int[]{1,1,1,}, new int[]{4,2,2}, new int[]{3,3,3}, new int[]{2,4,4});
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.SHIP_HIT, new Coordinate(3,5));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(3,3)));
        assertTrue(candidates.contains(new Coordinate(3,6)));
        verifyInitialized();
    }

    @Test
    public void candidatesAreUpdatedIfHit4() {
        whenInitialized(new int[]{4,1,1,}, new int[]{3,2,2}, new int[]{2,3,3}, new int[]{1,4,4});
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.SHIP_HIT, new Coordinate(3,3));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new Coordinate(3,2)));
        assertTrue(candidates.contains(new Coordinate(3,5)));
        verifyInitialized();
    }

    @Test
    public void candidateIsOnlyRemovedWhenNewCandidateHasProbability0() {
        whenInitialized(new int[]{0,1,1,}, new int[]{2,2,2}, new int[]{3,0,3}, new int[]{2,4,4});
        mDestroyer.initialize(mArea, mOrigin, mCalculator);
        mDestroyer.confirmAction(HitResult.SHIP_HIT, new Coordinate(2,4));
        List<Coordinate> candidates = mDestroyer.getCandidates();
        assertEquals(1, candidates.size());
        assertTrue(candidates.contains(new Coordinate(4,4)));
        verifyInitialized();
    }
}