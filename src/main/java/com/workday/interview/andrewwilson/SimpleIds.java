package com.workday.interview.andrewwilson;

import com.workday.interview.Ids;

/**
 * Created by drewwilson on 19/11/2017.
 */
public class SimpleIds implements Ids {
    private final long fromValue;
    private final long toValue;
    private final boolean fromInclusive;
    private final boolean toInclusive;
    private long[] data;
    private short offset = -1;

    public SimpleIds(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive, long[] data) {
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
