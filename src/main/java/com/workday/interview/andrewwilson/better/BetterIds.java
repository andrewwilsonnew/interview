package com.workday.interview.andrewwilson.better;

import com.workday.interview.Ids;

/**
 * Created by drewwilson on 20/11/2017.
 */
public class BetterIds implements Ids {
    private final short[] output;
    private final int end;
    private int offset;
    private final boolean copy;

    BetterIds(short[] output, int begin, int end, boolean copy) {
        this.output = output;
        this.end = end;
        offset = begin;
        this.copy = copy;
    }

    @Override
    public short nextId() {
        if(offset >= end) { return -1; }
        return output[offset++];
    }

    public boolean isCopy() { return copy; }
}