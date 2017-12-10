package com.workday.interview.andrewwilson.scanning;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.AbstractRangeContainerTest;
import org.junit.Test;

/**
 * Created by drewwilson on 03/12/2017.
 */
public class ScanningRangeContainerTest extends AbstractRangeContainerTest {

    @Override
    protected RangeContainer getRangeContainer(long[] data) {
        return new ScanningRangeContainer(data, true, true);
    }

    @Test(expected = IllegalThreadStateException.class)
    public void testFullyDrained() {
        ScanningRangeContainer container = new ScanningRangeContainer(SAMPLE_DATA, false, true);
        container.findIdsInRange(2,3,true,true);
        // This should fail because we didn't fully drain.
        container.findIdsInRange(2,3,true,true);
    }

    @Test(expected = IllegalThreadStateException.class)
    public void testFullyDrainedOnlyOnce() {
        ScanningRangeContainer container = new ScanningRangeContainer(SAMPLE_DATA, false, true);
        Ids ids = container.findIdsInRange(2, 3, true, true);
        // This should fail because we drained more than once.
        for(int i=0;i<10;i++) {
            ids.nextId();
        }
    }
}
