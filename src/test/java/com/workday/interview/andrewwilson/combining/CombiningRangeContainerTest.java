package com.workday.interview.andrewwilson.combining;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchIds;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchRangeContainerTest;
import com.workday.interview.andrewwilson.empty.EmptyIds;
import com.workday.interview.andrewwilson.scanning.ScanningIds;

import java.util.concurrent.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Explicit choice to extend BinarySearchRangeContainerTest - this should do everything and more.
 */
class CombiningRangeContainerTest extends BinarySearchRangeContainerTest {

    @Override
    public RangeContainer getRangeContainer(long[] data) {
        return new CombiningRangeContainer(data,true,true,false,true);
    }

    /**
     * If the range is below half we should search
     */
    @Test
    void testBelowHalfRangeSearching() {
        assertEquals(BinarySearchIds.class,
            getRangeContainer(SAMPLE_DATA).findIdsInRange(10,12,true,true).getClass(),
            "If the range is below half we should search");
    }

    /**
     * Above half range we should scan.
     */
    @Test public void testAboveHalfRangeScanning() {
        assertEquals(ScanningIds.class, getRangeContainer(SAMPLE_DATA).findIdsInRange(2,20,true,true).getClass(),
            "Above half range we should scan");
    }

    @Test
    void greaterThan32KFails() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> getRangeContainer(new long[Short.MAX_VALUE+1]));
    }

    @Test
    void TwoThreadsShouldGetDifferentInstances() throws InterruptedException, ExecutionException, TimeoutException {
        final RangeContainer rangeContainer = getRangeContainer(SAMPLE_DATA);
        Ids ids = rangeContainer.findIdsInRange(0, 20, true, true);
        Future<Ids> submit = Executors.newCachedThreadPool().submit(() -> rangeContainer.findIdsInRange(0, 20, true, true));
        Ids check = submit.get(10, TimeUnit.SECONDS);
        assertNotEquals(check, ids, "Should be different");

    }

    @Test
    void HarshCheckingThreadsFailsCorrectly() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            final RangeContainer rangeContainer = getRangeContainer(SAMPLE_DATA);
            final Ids idsInRange = rangeContainer.findIdsInRange(1, 20, true, true);
            Future<Short> submit = Executors.newCachedThreadPool().submit(idsInRange::nextId);
            submit.get(10, TimeUnit.SECONDS);
        });
    }

    @Test
    void testBelowRange() {
        assertEquals(EmptyIds.class, getRangeContainer(SAMPLE_DATA).findIdsInRange(0,1,true,true).getClass(),
            "Below range should return Empty Range");
    }

    @Test
    void testAboveRange() {
        assertEquals(EmptyIds.class, getRangeContainer(SAMPLE_DATA).findIdsInRange(23,24,true,true).getClass(),
            "Above range should return Empty Range");
    }

    @Test
    void testWrongWayRound() {
        assertEquals(EmptyIds.class, getRangeContainer(SAMPLE_DATA).findIdsInRange(16,15,true,true).getClass(),
            "Wrong way round should return Empty Range");
    }

    @Test
    void testNullData() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> new CombiningRangeContainer(null));
    }

    @Test
    void testEmptyData() {
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> new CombiningRangeContainer(new long[0]));
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
