package com.workday.interview.andrewwilson.scanning;

import com.workday.interview.RangeContainer;

/**
 * Very simple scanning container.
 */
public class ScanningRangeContainer implements RangeContainer {
    private long[] data;

    public ScanningRangeContainer(long[] data) {
        this.data = data;
    }

    public ScanningIds findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        return new ScanningIds(fromValue, toValue, fromInclusive, toInclusive, data);
    }
}
