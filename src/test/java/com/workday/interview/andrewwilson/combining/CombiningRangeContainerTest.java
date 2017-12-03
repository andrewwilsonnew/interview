package com.workday.interview.andrewwilson.combining;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchIds;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchRangeContainerTest;
import com.workday.interview.andrewwilson.empty.EmptyIds;
import com.workday.interview.andrewwilson.scanning.ScanningIds;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Explicit choice to extend BinarySearchRangeContainerTest - this should do everything and more.
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

    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void greaterThan32KFails() {
        getRangeContainer(new long[Short.MAX_VALUE+1]);
    }

    @Test public void testSecondThreadCannotAccessFirst() throws InterruptedException {
        final RangeContainer rangeContainer = getRangeContainer(SAMPLE_DATA);
        msg = null;
        Thread thread = new Thread(() -> {
            try {
                rangeContainer.findIdsInRange(1, 1, true, true);
                msg = "We should not reach this statement since different threads cannot access";
            } catch (IllegalThreadStateException e) {} // expected
        });
        thread.start();
        thread.join();
        assertEquals(null, msg);
    }

    @Test public void TwoThreadsCanWorkAtTheSameTime() {

    }

    @Test public void HarshCheckingThreadsFailsCorrectly() throws InterruptedException {
        final RangeContainer rangeContainer = getRangeContainer(SAMPLE_DATA);
        Ids idsInRange = rangeContainer.findIdsInRange(1, 1, true, true);
        msg =  null;
        Thread thread = new Thread(() -> {
            try {
                idsInRange.nextId();
                msg = "We should not reach this statement since different threads cannot access 2";
            } catch (IllegalThreadStateException e) {} // expected
        });
        thread.start();
        thread.join();
        assertEquals(null, msg);
    }

    private String msg;

    private synchronized void setMessage(String msg) { this.msg = msg; }

    @Test public void testBelowRange() {
        assertEquals("Below range should return Empty Range", EmptyIds.class, rc.findIdsInRange(0,1,true,true).getClass() );
    }

    @Test public void testAboveRange() {
        assertEquals("Above range should return Empty Range", EmptyIds.class, rc.findIdsInRange(23,24,true,true).getClass() );
    }

    @Test public void testWrongWayRound() {
        assertEquals("Wrong way round should return Empty Range", EmptyIds.class, rc.findIdsInRange(16,15,true,true).getClass());
    }
}
