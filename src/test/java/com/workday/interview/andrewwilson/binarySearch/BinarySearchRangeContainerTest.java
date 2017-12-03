package com.workday.interview.andrewwilson.binarySearch;

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
        return new BinarySearchRangeContainer(data);
    }

    @Test
    public void testSingleValueShouldNotCopy() {
        assertFalse(((BinarySearchIds)rc.findIdsInRange(16,16,true,true)).isCopy());
    }
}
