package com.workday.interview.andrewwilson.combining;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchRangeContainer;
import com.workday.interview.andrewwilson.scanning.ScanningRangeContainer;

/**
 * Created by drewwilson on 03/12/2017.
 */
public class CombiningRangeContainer implements RangeContainer {

    private final BinarySearchRangeContainer binarySearchRangeContainer;
    private final ScanningRangeContainer scanningRangeContainer;

    public CombiningRangeContainer(long[] data) {
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
