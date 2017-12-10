package com.workday.interview.andrewwilson;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchRangeContainer;
import com.workday.interview.andrewwilson.combining.CombiningRangeContainer;
import com.workday.interview.andrewwilson.scanning.ScanningRangeContainer;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Performance Test for Suite.  Would consider using JMH.
 */
public class PerformanceTest {
    public static final Logger LOGGER = Logger.getLogger(PerformanceTest.class);

    @Test public void testPerformance() {

        // populate the inital data.
        long[] data = new long[Short.MAX_VALUE];
        for(int i=0;i<data.length;i++) {
            // Chose this explicitly, so we get a good normal range.
            data[i] = (long)(Short.MIN_VALUE + (Math.random()*2*Short.MAX_VALUE));
        }

        RangeContainer[] handlers = {
                new ScanningRangeContainer(data, false),
                new BinarySearchRangeContainer(data, false),
                new CombiningRangeContainer(data)};
        long results[][] = new long[handlers.length][11];

        int count = 0;

        for(int range=0;range<=10;range++) {
            for(int handler=0;handler<handlers.length;handler++) {
                long start = System.currentTimeMillis();
                long finish = Short.MIN_VALUE + (((long)Short.MAX_VALUE)-((long)Short.MIN_VALUE))*range/10;
                for(int loopCount=0;loopCount<10000;loopCount++) {
                    Ids idsInRange = handlers[handler].findIdsInRange(Short.MIN_VALUE, finish, true, true);
                    while (idsInRange.nextId() != -1) {
                        count++;
                    }
                }
                results[handler][range] = System.currentTimeMillis() - start;
            }
        }

        // print out the results.
        LOGGER.info("=========== Cut here and paste into excel =======================");
        for(int i=0;i<handlers.length;i++) {
            System.out.print(handlers[i].getClass().getSimpleName()+" || ");
            for(int j=0;j<=10;j++) {
                System.out.print(results[i][j]+" | ");
            }
            System.out.println();
        }
        LOGGER.info("=========== Cut here and paste into excel =======================");

        System.out.println("Just forcing count so JIT doesn't optimize : " + count);
    }
}
