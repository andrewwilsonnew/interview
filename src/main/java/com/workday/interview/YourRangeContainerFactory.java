package com.workday.interview;

import com.workday.interview.andrewwilson.combining.CombiningRangeContainer;

/**
 * Created by drewwilson on 19/11/2017.
 */
public class YourRangeContainerFactory implements RangeContainerFactory {
    public RangeContainer createContainer(long[] data) {
        return new CombiningRangeContainer(data);
    }
}
