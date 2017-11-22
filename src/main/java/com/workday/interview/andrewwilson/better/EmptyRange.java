package com.workday.interview.andrewwilson.better;

import com.workday.interview.Ids;

/**
 * Return an empty range, useful for invalid range or above/below range.
 */
public class EmptyRange implements Ids {

    private static final EmptyRange sInstance = new EmptyRange();

    private EmptyRange() {} // private constructor.

    public static EmptyRange getInstance() { return sInstance; }

    @Override
    public short nextId() {
        return -1;
    }
}
