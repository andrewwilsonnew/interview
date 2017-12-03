package com.workday.interview.andrewwilson.empty;

import com.workday.interview.Ids;

/**
 * Return an empty range, useful for invalid range or above/below range.
 */
public class EmptyIds implements Ids {

    private static final EmptyIds sInstance = new EmptyIds();

    private EmptyIds() {} // private constructor.

    public static EmptyIds getInstance() { return sInstance; }

    @Override
    public short nextId() {
        return -1;
    }
}
