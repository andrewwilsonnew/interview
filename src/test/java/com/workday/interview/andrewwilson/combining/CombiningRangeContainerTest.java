package com.workday.interview.andrewwilson.combining;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchIds;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchRangeContainerTest;
import com.workday.interview.andrewwilson.empty.EmptyIds;
import com.workday.interview.andrewwilson.scanning.ScanningIds;
import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Explicit choice to extend BinarySearchRangeContainerTest - this should do everything and more.
 */
public class CombiningRangeContainerTest extends BinarySearchRangeContainerTest {

    @Override
    protected RangeContainer getRangeContainer(long[] data) {
        return new CombiningRangeContainer(data,true,true,false,true);
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

    @Test public void TwoThreadsShouldGetDifferentInstances() throws InterruptedException, ExecutionException, TimeoutException {
        final RangeContainer rangeContainer = getRangeContainer(SAMPLE_DATA);
        Ids ids = rangeContainer.findIdsInRange(0, 20, true, true);
        Future<Ids> submit = Executors.newCachedThreadPool().submit(() -> rangeContainer.findIdsInRange(0, 20, true, true));
        Ids check = submit.get(10, TimeUnit.SECONDS);
        assertFalse("Should be different", check.equals(ids));

    }

    @Test(expected = ExecutionException.class)
    public void HarshCheckingThreadsFailsCorrectly() throws InterruptedException, TimeoutException, ExecutionException {
        final RangeContainer rangeContainer = getRangeContainer(SAMPLE_DATA);
        final Ids idsInRange = rangeContainer.findIdsInRange(1, 20, true, true);
        Future<Short> submit = Executors.newCachedThreadPool().submit(() -> idsInRange.nextId());
        submit.get(10, TimeUnit.SECONDS);
    }

    @Test public void testBelowRange() {
        assertEquals("Below range should return Empty Range", EmptyIds.class, rc.findIdsInRange(0,1,true,true).getClass() );
    }

    @Test public void testAboveRange() {
        assertEquals("Above range should return Empty Range", EmptyIds.class, rc.findIdsInRange(23,24,true,true).getClass() );
    }

    @Test public void testWrongWayRound() {
        assertEquals("Wrong way round should return Empty Range", EmptyIds.class, rc.findIdsInRange(16,15,true,true).getClass());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testNullData() {
        new CombiningRangeContainer(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testEmptyData() {
        new CombiningRangeContainer(new long[0]);
    }
//
//    @Test(expected = IllegalThreadStateException.class)
//    public void testTooManyThreadException() {
//        for(int i=0;i<Runtime.getRuntime().availableProcessors()*2;i++) {
//            new Thread(() -> {
//                System.out.println("Created");
//                rc.findIdsInRange(2,3,true,true);
//            }).start();
//        }
//    }
}
