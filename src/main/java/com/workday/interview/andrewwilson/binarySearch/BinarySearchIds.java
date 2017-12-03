package com.workday.interview.andrewwilson.binarySearch;

import com.workday.interview.Ids;
import com.workday.interview.andrewwilson.combining.CombiningRangeContainer;

/**
 * This is for returning ranges.
 * The range may be allocated directly (if sorted) or an offset from the original range and length.
 */
public class BinarySearchIds implements Ids {
    private short[] output;
    private int end;
    private int offset;
    private boolean copy;
    private final Thread owningThread;
    private final boolean checkThreadEachTime;

    BinarySearchIds(boolean checkThreadEachTime) {
        this.checkThreadEachTime = checkThreadEachTime;
        owningThread = Thread.currentThread();
    }

    void setValues(short[] output, int begin, int end, boolean copy) {
        this.output = output;
        this.offset = begin;
        this.end = end;
        this.copy = copy;
    }

    @Override
    public short nextId() {
        if(offset > end) { return -1; }
        if(checkThreadEachTime && CombiningRangeContainer.checkThread(owningThread)) {} // dont do this every time for performance.
        return output[offset++];
    }

    /**
     * this is really a testing method and smells a bit, but refactored from public.
     */
    protected boolean isCopy() { return copy; }
}