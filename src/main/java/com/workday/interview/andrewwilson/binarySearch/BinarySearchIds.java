package com.workday.interview.andrewwilson.binarySearch;

import com.workday.interview.Ids;
import com.workday.interview.andrewwilson.combining.CombiningRangeContainer;

/**
 * This is for returning ranges.
 * The range may be allocated directly (if sorted) or an offset from the original range and length.
 */
public class BinarySearchIds implements Ids {

    // @TODO these should probably be in some helper class...
    public static final String EXCEPTION_FULLY_DRAINED_ONCE = "Ids should be fully drained only once";
    public static final String EXCEPTION_NOT_FULLY_DRAINED = "Previous Ids was not fully drained";

    private short[] output;
    private int end;
    private int offset;
    private boolean copy;
    private final Thread owningThread;
    private final boolean checkFullyDrainedOnceOnly;
    private final boolean checkThreadEachTime;
    private boolean reachedEnd = true;

    BinarySearchIds(boolean checkThreadEachTime, boolean checkFullyDrainedOnceOnly) {
        this.checkThreadEachTime = checkThreadEachTime;
        this.checkFullyDrainedOnceOnly = checkFullyDrainedOnceOnly;
        owningThread = Thread.currentThread();
    }

    void setValues(short[] output, int begin, int end, boolean copy) {
        if(checkFullyDrainedOnceOnly && !reachedEnd) {
            throw new IllegalThreadStateException(EXCEPTION_NOT_FULLY_DRAINED);
        }
        reachedEnd = false;
        this.output = output;
        this.offset = begin;
        this.end = end;
        this.copy = copy;
    }

    @Override
    public short nextId() {
        if(offset > end) {
            if(reachedEnd && checkFullyDrainedOnceOnly) {
               throw new IllegalThreadStateException(EXCEPTION_FULLY_DRAINED_ONCE);
            }
            reachedEnd = true;
            return -1;
        }
        if(checkThreadEachTime) { CombiningRangeContainer.checkThread(owningThread); } // dont do this every time for performance.
        return output[offset++];
    }

    /**
     * this is really a testing method and smells a bit, but refactored from public.
     */
    protected boolean isCopy() { return copy; }
}