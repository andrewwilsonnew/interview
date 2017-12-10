package com.workday.interview.andrewwilson.scanning;

import com.workday.interview.Ids;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchIds;
import com.workday.interview.andrewwilson.combining.CombiningRangeContainer;

/**
 * Very simple solution, just scan through.
 */
public class ScanningIds implements Ids {
    private long fromValue;
    private long toValue;
    private boolean fromInclusive;
    private boolean toInclusive;
    private long[] data;
    private short offset;
    private boolean isDrained = true;

    private final Thread owningThread;
    private final boolean checkThreadEachTime;
    private final boolean checkFullyDrainedOnceOnly;


    ScanningIds(long[] data, boolean checkThreadEachTime, boolean checkFullyDrainedOnceOnly) {
        this.checkThreadEachTime = checkThreadEachTime;
        this.checkFullyDrainedOnceOnly = checkFullyDrainedOnceOnly;
        owningThread = Thread.currentThread();
        this.data = data;
    }

    protected void setValues(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        if(checkFullyDrainedOnceOnly && !isDrained) {
            throw new IllegalThreadStateException(BinarySearchIds.EXCEPTION_NOT_FULLY_DRAINED);
        }
        isDrained = false;
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.fromInclusive = fromInclusive;
        this.toInclusive = toInclusive;
        offset = -1;
    }

    @Override
    public short nextId() {
        if(checkThreadEachTime) { CombiningRangeContainer.checkThread(owningThread); } // don't do this every time for performance.
        while(++offset < data.length) {
            long value = data[offset];
            boolean lowerRange = fromInclusive ? value >= fromValue : value > fromValue;
            boolean upperRange = toInclusive ? value <= toValue : value < toValue;
            if(lowerRange && upperRange) {
                return offset;
            }
        }

        if(checkFullyDrainedOnceOnly && isDrained ) {
            throw new IllegalThreadStateException(BinarySearchIds.EXCEPTION_FULLY_DRAINED_ONCE);
        } else {
            isDrained = true;
            return -1;
        }


    }
}
