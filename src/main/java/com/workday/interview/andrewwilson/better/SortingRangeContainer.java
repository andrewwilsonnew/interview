package com.workday.interview.andrewwilson.better;

import com.sun.tools.javac.util.Pair;
import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.SimpleIds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by drewwilson on 20/11/2017.
 */
public class SortingRangeContainer implements RangeContainer {
    private final short[] sortedKeys;
    private final long[] sortedValues;
    private final short[] output = new short[10];

    public SortingRangeContainer(long[] data) {
        List<Pair<Long, Short>> sorted = new ArrayList<>(data.length);
        for(short i=0;i<data.length;i++) {
            sorted.add(new Pair<>(data[i], i));
        }
        // sort them
        sorted.sort((a,b) -> (int)(a.fst - b.fst));

        sortedKeys = new short[data.length];
        sortedValues = new long[data.length];

        // write out the 2 arrays in sorted order.
        for(short i=0;i<data.length;i++) {
            sortedKeys[i] = sorted.get(i).snd;
            sortedValues[i] = sorted.get(i).fst;
        }
    }

    @Override
    public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        Pair<Integer, Integer> range = range(sortedValues, fromValue, toValue, fromInclusive, toInclusive);
        System.arraycopy(sortedKeys, range.fst, output, 0, range.snd - range.fst);
        return new BetterIds(output);
    }

    public Pair<Integer, Integer> range(long[] data, long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        MyHandler bottom = new MyHandler(data, fromValue, false);
        MyHandler top = new MyHandler(data, toValue,true);
        while(bottom.next() || top.next()) {} // find the ids.
        return new Pair<>(bottom.result(), top.result());



    }

    public static class MyHandler {
        private final long[] data;
        private final long value;
        private final boolean isTop;
        private int bottomOffset = 0;
        private int topOffset;

        public MyHandler(long[] data, long value, boolean isTop) {
            this.data = data;
            this.value = value;
            this.isTop = isTop;
            topOffset = data.length;
        }

        public boolean next() {
            int testOffset = bottomOffset + (topOffset-bottomOffset)/2;
            System.out.println(bottomOffset + ":" + topOffset + ":" + testOffset + ":" + data[testOffset]+":"+value);
            if( data[testOffset] >= value) {
                topOffset = testOffset;
            } else {
                bottomOffset = testOffset;
            }
            return topOffset - bottomOffset == 1;
        }

        public int result() {
            int offset = isTop ? bottomOffset : topOffset;
            if (isTop) {
                // need to scroll to the last.
                while(offset < data.length - 1 && data[offset+1] <= value) {
                    offset++;
                }
            } else {
                // need to scroll back to the first.
                while(offset > 0 && data[offset-1] > value) {
                    offset--;
                }
            }
            return offset;
        }
    }
}
