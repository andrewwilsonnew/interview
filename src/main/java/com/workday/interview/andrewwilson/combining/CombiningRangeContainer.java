package com.workday.interview.andrewwilson.combining;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchRangeContainer;
import com.workday.interview.andrewwilson.scanning.ScanningRangeContainer;

/**
 * Choose whether to use the BinarySearch or Scanning container and also handle threads.
 */
public class CombiningRangeContainer implements RangeContainer {

    private final BinarySearchRangeContainer binarySearchRangeContainer;
    private final ScanningRangeContainer scanningRangeContainer;

    public CombiningRangeContainer(long[] data) {
        if(data.length > Short.MAX_VALUE) {
            throw new ArrayIndexOutOfBoundsException("Data has length greater than 32K : " + data.length);
        }
        binarySearchRangeContainer = new BinarySearchRangeContainer(data);
        scanningRangeContainer = new ScanningRangeContainer(data);
    }

    @Override
    public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        if(binarySearchRangeContainer.worthUsing(fromValue,toValue)) {
            return binarySearchRangeContainer.findIdsInRange(fromValue, toValue, fromInclusive, toInclusive);
        } else {
            return scanningRangeContainer.findIdsInRange(fromValue, toValue, fromInclusive, toInclusive);
        }
    }
}
