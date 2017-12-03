package com.workday.interview.andrewwilson.scanning;

import com.workday.interview.RangeContainer;

/**
 * Very simple scanning container.
 */
public class ScanningRangeContainer implements RangeContainer {
    private final ScanningIds ids;

    public ScanningRangeContainer(long[] data, boolean checkThreadEachTime) {
        this.ids = new ScanningIds(data, checkThreadEachTime);
    }

    public ScanningIds findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        ids.setValues(fromValue, toValue, fromInclusive, toInclusive);
        return ids;
    }
}
