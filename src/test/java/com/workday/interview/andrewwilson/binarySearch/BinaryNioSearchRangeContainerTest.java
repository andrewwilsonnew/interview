package com.workday.interview.andrewwilson.binarySearch;

import com.workday.interview.RangeContainer;

/**
 * Created by drewwilson on 10/12/2017.
 */
public class BinaryNioSearchRangeContainerTest extends BinarySearchRangeContainerTest {
    @Override
    protected RangeContainer getRangeContainer(long[] data) {
        return new BinarySearchRangeContainer(data,true, true);
    }
}
