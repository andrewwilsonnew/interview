package com.workday.interview.andrewwilson.binarySearch;

import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.AbstractRangeContainerTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Call all the standard tests for Binary Search Range Container.
 */
public class BinarySearchRangeContainerTest extends AbstractRangeContainerTest {

    @Override
    protected RangeContainer getRangeContainer(long[] data) {
        return new BinarySearchRangeContainer(data);
    }

    @Test
    public void testSingleValueShouldNotCopy() {
        assertFalse(((BinarySearchIds)rc.findIdsInRange(16,16,true,true)).isCopy());
    }

    @Test public void testBelowRange() {
        assertEquals("Below range should return Empty Range", EmptyRange.class, rc.findIdsInRange(0,1,true,true).getClass() );
    }

    @Test public void testAboveRange() {
        assertEquals("Above range should return Empty Range", EmptyRange.class, rc.findIdsInRange(23,24,true,true).getClass() );
    }

    @Test public void testWrongWayRound() {
        assertEquals("Wrong way round should return Empty Range", EmptyRange.class, rc.findIdsInRange(16,15,true,true).getClass());
    }
}
