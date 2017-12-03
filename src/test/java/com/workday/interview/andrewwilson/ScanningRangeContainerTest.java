package com.workday.interview.andrewwilson;

import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.scanning.ScanningRangeContainer;

/**
 * Created by drewwilson on 03/12/2017.
 */
public class ScanningRangeContainerTest extends AbstractRangeContainerTest {

    @Override
    protected RangeContainer getRangeContainer(long[] data) {
        return new ScanningRangeContainer(data);
    }
}
