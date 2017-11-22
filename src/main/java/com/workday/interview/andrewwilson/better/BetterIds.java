package com.workday.interview.andrewwilson.better;

import com.workday.interview.Ids;
import com.workday.interview.andrewwilson.SimpleIds;

/**
 * Created by drewwilson on 20/11/2017.
 */
public class BetterIds implements Ids {
    private final short[] output;
    private short offset;

    BetterIds(short[] output) {
        this.output = output;
    }

    @Override
    public short nextId() {
        return output[offset++];
    }
}