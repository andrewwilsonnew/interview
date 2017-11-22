package com.workday.interview;

import com.workday.interview.andrewwilson.SimpleIds;
import com.workday.interview.andrewwilson.YourRangeContainerFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by drewwilson on 19/11/2017.
 */
public class BetterUnitTest {
    private  RangeContainer rc;

    @Before
    public void setUp(){
        RangeContainerFactory rf = new YourRangeContainerFactory();
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

    @Test public void testWrongWayRound() {
        assertEquals(Collections.emptyList(), findIdsInRange(17,14,false,false));
    }

    @Test public void testOverRange() {
        assertEquals(createShortList(1), findIdsInRange(0,22,true, true));
    }

    private List<Short> findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        Ids ids = rc.findIdsInRange(fromValue, toValue, fromInclusive, toInclusive);
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
