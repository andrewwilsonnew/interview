package com.workday.interview.andrewwilson.scanning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.AbstractRangeContainerTest;

/**
 * Created by drewwilson on 03/12/2017.
 */
public class ScanningRangeContainerTest
    implements AbstractRangeContainerTest {

    @Override
    public RangeContainer getRangeContainer(long[] data) {
        return new ScanningRangeContainer(data, true, true);
    }

    @Test
    void testFullyDrained() {
        Assertions.assertThrows(IllegalThreadStateException.class, () -> {
            ScanningRangeContainer container = new ScanningRangeContainer(SAMPLE_DATA, false, true);
            container.findIdsInRange(2, 3, true, true);
            // This should fail because we didn't fully drain.
            container.findIdsInRange(2, 3, true, true);
        });
    }

    @Test
    void testFullyDrainedOnlyOnce() {
        Assertions.assertThrows(IllegalThreadStateException.class, () -> {
            ScanningRangeContainer container = new ScanningRangeContainer(SAMPLE_DATA, false, true);
            Ids ids = container.findIdsInRange(2, 3, true, true);
            // This should fail because we drained more than once.
            for(int i=0;i<10;i++) {
                ids.nextId();
            }
        });
    }
}
