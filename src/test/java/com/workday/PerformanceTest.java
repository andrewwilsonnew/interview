package com.workday;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.SimpleRangeContainer;
import com.workday.interview.andrewwilson.better.SortingRangeContainer;

import java.util.Random;

/**
 * Created by drewwilson on 22/11/2017.
 */
public class PerformanceTest {
    public static void main(String[] args) {


        // populate.
        long[] data = new long[Short.MAX_VALUE];
        //Random random = new Random(System.currentTimeMillis());
        for(int i=0;i<data.length;i++) {
            // Chose this explicitly, so we get a good normal range.
            data[i] = (long)(Short.MIN_VALUE + (Math.random()*2*Short.MAX_VALUE));
            System.out.println(data[i]);
        }

        RangeContainer[] handlers = { new SimpleRangeContainer(data), new SortingRangeContainer(data)};
        long results[][] = new long[handlers.length][11];

        int count = 0;


        for(int range=0;range<=10;range++) {
            for(int handler=0;handler<handlers.length;handler++) {
                long start = System.currentTimeMillis();
                long finish = Short.MIN_VALUE + (((long)Short.MAX_VALUE)-((long)Short.MIN_VALUE))*range/10;
                System.out.println(finish);
                for(int loopCount=0;loopCount<100000;loopCount++) {
                    Ids idsInRange = handlers[handler].findIdsInRange(Short.MIN_VALUE, finish, true, true);
                    while (idsInRange.nextId() != -1) {
                        count++;
                    }
                }
                results[handler][range] = System.currentTimeMillis() - start;
            }
        }

        // print out the results.
        for(int i=0;i<handlers.length;i++) {
            System.out.print(handlers[i].getClass()+" || ");
            for(int j=0;j<=10;j++) {
                System.out.print(results[i][j]+" | ");
            }
            System.out.println();
        }

        System.out.println("Just forcing count so JIT doesn't optimize : " + count);
    }
}
