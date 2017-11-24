package com.workday.interview.andrewwilson;

import com.workday.interview.*;
import com.workday.interview.andrewwilson.better.SortingRangeContainer;

/**
 * Created by drewwilson on 19/11/2017.
 */
public class YourRangeContainerFactory implements RangeContainerFactory {
    public RangeContainer createContainer(long[] data) {
        if(data.length > Short.MAX_VALUE) {
            throw new RuntimeException("Data has length greater than 32K : " + data.length);
        }
        return new SortingRangeContainer(data);
    }
}
