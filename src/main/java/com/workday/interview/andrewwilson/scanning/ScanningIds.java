package com.workday.interview.andrewwilson.scanning;

import com.workday.interview.Ids;

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

    public ScanningIds(long[] data) {
        this.data = data;
    }

    protected void setValues(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.fromInclusive = fromInclusive;
        this.toInclusive = toInclusive;
        offset = -1;
    }

    @Override
    public short nextId() {
        while(++offset < data.length) {
            long value = data[offset];
            boolean lowerRange = fromInclusive ? value >= fromValue : value > fromValue;
            boolean upperRange = toInclusive ? value <= toValue : value < toValue;
            if(lowerRange && upperRange) {
                return offset;
            }
        }
        return -1;

    }
}
