package com.workday.interview.andrewwilson.scanning;

import com.workday.interview.Ids;

/**
 * Very simple solution, just scan through.
 */
public class ScanningIds implements Ids {
    private final long fromValue;
    private final long toValue;
    private final boolean fromInclusive;
    private final boolean toInclusive;
    private long[] data;
    private short offset = -1;

    public ScanningIds(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive, long[] data) {
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.fromInclusive = fromInclusive;
        this.toInclusive = toInclusive;
        this.data = data;
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
