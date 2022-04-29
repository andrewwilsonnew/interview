package com.workday.interview.andrewwilson.binarySearch;

import com.workday.interview.RangeContainer;

/**
 * Created by drewwilson on 10/12/2017.
 */
class BinaryNioSearchRangeContainerTest extends BinarySearchRangeContainerTest {
    @Override
    public RangeContainer getRangeContainer(long[] data) {
        return new BinarySearchRangeContainer(data, true, false, true);
    }
}
