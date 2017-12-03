package com.workday.interview.andrewwilson.better;

import com.workday.interview.Ids;

/**
 * Created by drewwilson on 20/11/2017.
 */
public class BinarySearchIds implements Ids {
    private short[] output;
    private int end;
    private int offset;
    private boolean copy;

    BinarySearchIds() {
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
        return output[offset++];
    }

    public boolean isCopy() { return copy; }
}