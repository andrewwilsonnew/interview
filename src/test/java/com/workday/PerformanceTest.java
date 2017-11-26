package com.workday;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.SimpleRangeContainer;
import com.workday.interview.andrewwilson.YourRangeContainerFactory;

import java.util.Random;

/**
 * Created by drewwilson on 22/11/2017.
 */
public class PerformanceTest {
    public static void main(String[] args) {
        // populate.
        long[] data = new long[Short.MAX_VALUE];
        Random random = new Random(System.currentTimeMillis());
        for(int i=0;i<data.length;i++) {
            data[i] = random.nextLong();
        }

        RangeContainer container = new YourRangeContainerFactory().createContainer(data);
        //RangeContainer container = new SimpleRangeContainer(data);

        long begin = System.currentTimeMillis();
        int count = 0;

        for(int i=0;i<10000;i++) {
            long start = (long)(Short.MAX_VALUE/2 - Math.random()*5);
            long stop = (long)(Short.MAX_VALUE/2 + Math.random()*5);
            //System.out.println(start + " : " + stop);
            Ids idsInRange = container.findIdsInRange(start, stop, false, false);
            while(idsInRange.nextId() != -1) {
                count++;
            }
        }

        System.out.println("Time : " + (System.currentTimeMillis()-begin) + " : " + count);
    }
}
