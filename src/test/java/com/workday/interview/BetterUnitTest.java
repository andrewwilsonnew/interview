package com.workday.interview;

import com.workday.interview.andrewwilson.YourRangeContainerFactory;
import com.workday.interview.andrewwilson.better.BetterIds;
import com.workday.interview.andrewwilson.better.EmptyRange;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by drewwilson on 19/11/2017.
 */
public class BetterUnitTest {
    private static RangeContainer rc;
    private static RangeContainerFactory rf;

    @BeforeClass
    public static void setUp(){
        rf = new YourRangeContainerFactory();
        rc = rf.createContainer(new long[]{2,21,10,13,14,13,14,12,17,21,2,15,16,17,16,17,2,21}) ;
    }

    @Test public void testRangeFalseFalse() {
        assertEquals(createShortList(11,12,14), findIdsInRange(14, 17, false, false));
    }

    @Test public void testRangeFalseTrue() {
        assertEquals(createShortList(8,11,12,13,14,15), findIdsInRange(14, 17, false, true));
    }

    @Test public void testRangeTrueFalse() {
        assertEquals(createShortList(4,6,11,12,14), findIdsInRange(14, 17, true, false) );
    }

    @Test public void testRangeTrueTrue() {
        assertEquals(createShortList(4,6,8,11,12,13,14,15), findIdsInRange(14, 17, true, true) );
    }

    @Test public void testOverRange() {
        assertEquals(createShortList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17), findIdsInRange(0,22,true, true));
    }

    @Test public void testBelowRange() {
        System.out.println(rc.findIdsInRange(0,1,true,true) instanceof EmptyRange);
    }

    @Test public void testAboveRange() {
        assertTrue(rc.findIdsInRange(23,24,true,true) instanceof EmptyRange);
    }

    @Test public void testWrongWayRound() {
        assertTrue(rc.findIdsInRange(16,15,true,true) instanceof EmptyRange);
    }

    @Test public void testSingleValueShouldNotCopy() {
        assertFalse(((BetterIds)rc.findIdsInRange(16,16,true,true)).isCopy());
    }

    @Test public void testMinValue() {
        assertEquals(createShortList(0), findIdsInRange(rf.createContainer(new long[] { Long.MIN_VALUE } ),Long.MIN_VALUE, Long.MIN_VALUE, true, true));
    }

    @Test public void testMaxValue() {
        assertEquals(createShortList(0), findIdsInRange(rf.createContainer(new long[] { Long.MAX_VALUE } ), Long.MAX_VALUE, Long.MAX_VALUE, true, true));
    }

    private List<Short> findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        return findIdsInRange(rc, fromValue,toValue,fromInclusive,toInclusive);
    }

    private List<Short> findIdsInRange(RangeContainer myRc, long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        Ids ids = myRc.findIdsInRange(fromValue, toValue, fromInclusive, toInclusive);
        List<Short> result = new ArrayList<>();
        short id;
        while((id = ids.nextId()) != -1) {
            result.add(id);
        }
        return result;
    }

    private List<Short> createShortList(int... x) {
        List<Short> result = new ArrayList<>(x.length);
        x.toString();
        int[] clone = x.clone();
        for(int i=0;i<clone.length;i++) {
            result.add((short)clone[i]);
        }
        return result;
    }
}
