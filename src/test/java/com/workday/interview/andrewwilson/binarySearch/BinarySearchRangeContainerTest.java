package com.workday.interview.andrewwilson.binarySearch;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.scanning.ScanningRangeContainerTest;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Call all the standard tests for Binary Search Range Container.
 */
public class BinarySearchRangeContainerTest extends ScanningRangeContainerTest {

    @Override
    protected RangeContainer getRangeContainer(long[] data) {
        return new BinarySearchRangeContainer(data, true, false, false);
    }

    @Test
    public void testSingleValueShouldNotCopy() {
        assertFalse(((BinarySearchIds)rc.findIdsInRange(16,16,true,true)).isCopy());
    }

    @Test(expected = IllegalThreadStateException.class)
    public void testFullyDrained() {
        BinarySearchRangeContainer container = new BinarySearchRangeContainer(SAMPLE_DATA, false, true, false);
        container.findIdsInRange(2,3,true,true);
        // This should fail because we didn't fully drain.
        container.findIdsInRange(2,3,true,true);
    }

    @Test(expected = IllegalThreadStateException.class)
    public void testFullyDrainedOnlyOnce() {
        BinarySearchRangeContainer container = new BinarySearchRangeContainer(SAMPLE_DATA, false, true, false);
        Ids ids = container.findIdsInRange(2, 3, true, true);
        // This should fail because we drained more than once.
        for(int i=0;i<10;i++) {
            ids.nextId();
        }
    }

}
