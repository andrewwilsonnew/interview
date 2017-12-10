package com.workday.interview;

import com.workday.interview.andrewwilson.combining.CombiningRangeContainer;

/**
 * Make this a very simple wrapper.
 */
public class YourRangeContainerFactory implements RangeContainerFactory {
    public RangeContainer createContainer(long[] data) {
        return new CombiningRangeContainer(data);
    }
}
