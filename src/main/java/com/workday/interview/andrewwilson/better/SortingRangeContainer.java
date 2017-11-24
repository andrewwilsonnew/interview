package com.workday.interview.andrewwilson.better;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by drewwilson on 20/11/2017.
 */
public class SortingRangeContainer implements RangeContainer {
    private final short[] sortedKeys;
    private final long[] sortedValues;
    private final short[] output = new short[10];
    private final long bottomLimit;
    private final long topLimit;


    public SortingRangeContainer(long[] data) {
        List<Pair<Long, Short>> sorted = new ArrayList<>(data.length);
        for(short i=0;i<data.length;i++) {
            sorted.add(new ImmutablePair<>(data[i], i));
        }
        // sort them
        sorted.sort(new Comparator<Pair<Long, Short>>() {
            @Override
            public int compare(Pair<Long, Short> a, Pair<Long, Short> b) {
                return (int) (a.getLeft() - b.getLeft());
            }
        });

        sortedKeys = new short[data.length];
        sortedValues = new long[data.length];

        // write out the 2 arrays in sorted order.
        for(short i=0;i<data.length;i++) {
            sortedKeys[i] = sorted.get(i).getRight();
            sortedValues[i] = sorted.get(i).getLeft();
        }

        bottomLimit = sortedValues[0];
        topLimit = sortedValues[data.length-1];
    }

    @Override
    public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        long realFrom = fromValue + (fromInclusive ? 0 : + 1);
        long realTo = toValue + (toInclusive ? 0 : -1);

        if(realFrom > topLimit || realTo < bottomLimit || realFrom > realTo) {
            // we are out of range, so tell them!
            // I'd prefer to throw an exception, but don't want to break the harness I can't see.
            return EmptyRange.getInstance();
        }
        Pair<Integer, Integer> range = range(sortedValues, realFrom, realTo );
        int length = range.getRight() - range.getLeft();
        System.arraycopy(sortedKeys, range.getLeft(), output, 0, length+1);
        Arrays.sort(output,0,length+1);
        output[length+1] = -1;
        return new BetterIds(output);
    }

    public Pair<Integer, Integer> range(long[] data, long fromValue, long toValue) {
        MyHandler bottom = new MyHandler(data, fromValue, false);
        MyHandler top = new MyHandler(data, toValue,true);
        while(bottom.next() || top.next()) {} // find the ids.
        return new ImmutablePair<>(bottom.result(), top.result());
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
            //System.out.println(bottomOffset + ":" + topOffset + ":" + testOffset + ":" + data[testOffset]+":"+value);
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
                while(offset > 0 && data[offset-1] >= value) {
                    offset--;
                }
            }
            return offset;
        }
    }
}
