package com.workday.interview.andrewwilson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * The worst test cases.
 * 1. return the entire 32K row - its slow and ugly - iterate through and return is probably the best - my original solution.
 * 2. A single id at the end.  This would be really bad with the linear search.
 * 3. Things that return nothing - this should return a singleton - ranges backwards.  ranges below our range.  ranges above our range.
 * I like my solution, it respects the ranges and does what it needs to do.
 * Now lets optimise it.  Java - prof probably the best way to start.
 */
public interface AbstractRangeContainerTest {
    long[] SAMPLE_DATA = new long[]{2,21,10,13,14,13,14,12,17,21,2,15,16,17,16,17,2,21};

    RangeContainer getRangeContainer(long[] data);

    @Test
    default void testRangeFalseFalse() {
        assertEquals(createShortList(11,12,14), findIdsInRange(14, 17, false, false));
    }

    @Test
    default void testRangeFalseTrue() {
        assertEquals(createShortList(8,11,12,13,14,15), findIdsInRange(14, 17, false, true));
    }

    @Test
    default void testRangeTrueFalse() {
        assertEquals(createShortList(4,6,11,12,14), findIdsInRange(14, 17, true, false) );
    }

    @Test
    default void testRangeTrueTrue() {
        assertEquals(createShortList(4,6,8,11,12,13,14,15), findIdsInRange(14, 17, true, true) );
    }

    @Test
    default void testOverRange() {
        assertEquals(createShortList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17), findIdsInRange(0,22,true, true));
    }

    @Test
    default void testMinValue() {
        assertEquals(createShortList(0), findIdsInRange(getRangeContainer(new long[] { Long.MIN_VALUE } ),Long.MIN_VALUE, Long.MIN_VALUE, true, true));
    }

    @Test
    default void testMaxValue() {
        assertEquals(createShortList(0), findIdsInRange(getRangeContainer(new long[] { Long.MAX_VALUE } ), Long.MAX_VALUE, Long.MAX_VALUE, true, true));
    }

    @Test
    default void testNegatives() {
        assertEquals(createShortList(0,2,3,5,6,7), findIdsInRange(getRangeContainer(new long[] {-1,3,-2,1,-3,1,2,-2,-3,Long.MIN_VALUE, Long.MAX_VALUE}), -2,2,true,true));
    }

    default List<Short> findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        return findIdsInRange(getRangeContainer(SAMPLE_DATA), fromValue,toValue,fromInclusive,toInclusive);
    }

    default List<Short> findIdsInRange(RangeContainer myRc, long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        Ids ids = myRc.findIdsInRange(fromValue, toValue, fromInclusive, toInclusive);
        List<Short> result = new ArrayList<>();
        short id;
        while((id = ids.nextId()) != -1) {
            result.add(id);
        }
        return result;
    }

    /**
     * Helper method to convert varargs into a {@link List}.
     *
     * @param ints the input parameters
     * @return a {@link List} of {@link Short}s
     */
    default List<Short> createShortList(int... ints) {
        List<Short> result = new ArrayList<>(ints.length);
        int[] clone = ints.clone();
        for (final int j : clone) {
            result.add((short) j);
        }
        return result;
    }
}
