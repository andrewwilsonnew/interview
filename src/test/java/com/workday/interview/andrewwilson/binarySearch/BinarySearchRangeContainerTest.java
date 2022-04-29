package com.workday.interview.andrewwilson.binarySearch;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.scanning.ScanningRangeContainerTest;

/**
 * Call all the standard tests for Binary Search Range Container.
 */
public class BinarySearchRangeContainerTest extends ScanningRangeContainerTest {

    @Override
    public RangeContainer getRangeContainer(long[] data) {
        return new BinarySearchRangeContainer(data, true, false, false);
    }

    @Test
    void testSingleValueShouldNotCopy() {
        assertFalse(((BinarySearchIds)getRangeContainer(SAMPLE_DATA).findIdsInRange(16,16,true,true)).isCopy());
    }

    @Test
    void testFullyDrained() {
        Assertions.assertThrows(IllegalThreadStateException.class, () -> {
            BinarySearchRangeContainer container = new BinarySearchRangeContainer(SAMPLE_DATA, false, true, false);
            container.findIdsInRange(2, 3, true, true);
            // This should fail because we didn't fully drain.
            container.findIdsInRange(2, 3, true, true);
        });
    }

    @Test
    public void testFullyDrainedOnlyOnce() {
        Assertions.assertThrows(IllegalThreadStateException.class, () -> {
            BinarySearchRangeContainer container = new BinarySearchRangeContainer(SAMPLE_DATA, false, true, false);
            Ids ids = container.findIdsInRange(2, 3, true, true);
            // This should fail because we drained more than once.
            for (int i = 0; i < 10; i++) {
                ids.nextId();
            }
        });
    }

}
