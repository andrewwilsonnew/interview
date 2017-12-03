package com.workday.interview.andrewwilson;

import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchIds;
import com.workday.interview.andrewwilson.combining.CombiningRangeContainer;
import com.workday.interview.andrewwilson.scanning.ScanningIds;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by drewwilson on 03/12/2017.
 */
public class CombiningRangeContainerTest extends BinarySearchRangeContainerTest {

    @Override
    protected RangeContainer getRangeContainer(long[] data) {
        return new CombiningRangeContainer(data);
    }

    /**
     * If the range is below half we should search
     */
    @Test
    public void testBelowHalfRangeSearching() {
        assertEquals("If the range is below half we should search", BinarySearchIds.class, rc.findIdsInRange(10,12,true,true).getClass() );
    }

    /**
     * Above half range we should scan.
     */
    @Test public void testAboveHalfRangeScanning() {
        assertEquals("Above half range we should scan",ScanningIds.class, rc.findIdsInRange(2,20,true,true).getClass() );
    }
}
