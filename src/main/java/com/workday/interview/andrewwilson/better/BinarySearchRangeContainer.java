package com.workday.interview.andrewwilson.better;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.ScanningIds;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by drewwilson on 20/11/2017.
 */
public class BinarySearchRangeContainer implements RangeContainer {
    //@Contended
    private final short[] sortedKeys;
    //@Contended
    private final long[] sortedValues;
    //@Contended
    private final short[] output;

    private final long bottomLimit;
    private final long topLimit;
    private final MyHandler bottomHandler = new MyHandler(false);
    private final MyHandler topHandler = new MyHandler(true);
    private final BinarySearchIds ids = new BinarySearchIds();
    private final Thread thread;
    private final long[] data;


    public BinarySearchRangeContainer(long[] data) {
        this.data = data;
        output = new short[data.length];
        thread = Thread.currentThread();
        List<Pair<Long, Short>> sorted = new ArrayList<>(data.length);
        for(short i=0;i<data.length;i++) {
            sorted.add(new ImmutablePair<>(data[i], i));
        }
        // sort them, removed the lambda because of java -prof
        sorted.sort(new Comparator<Pair<Long, Short>>() {
            @Override
            public int compare(Pair<Long, Short> a, Pair<Long, Short> b) {
                long left = a.getLeft();
                long right = b.getLeft();
                if(left != right) {
                    // sort by the long first.
                    return left > right ? 1 : -1;
                } else {
                    // then sort by the id, this is ok, because we know they are positive numbers.
                   return a.getRight() > b.getRight() ? 1 : -1;
                }
            }
        });
        //System.out.println(sorted);

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
        if(!Thread.currentThread().equals(thread)) {
            throw new IllegalThreadStateException("Can only be used by a single thread for performance reasons");
        }

        if(     (fromInclusive ? fromValue > topLimit : fromValue >= topLimit) || // we are over the topLimit
                (toInclusive ? toValue < bottomLimit : toValue <= bottomLimit) || // we are below the bottomLimit
                ( toInclusive && fromInclusive ? fromValue > toValue : fromValue >= toValue ))  // ranges don't overlap
        {
            // we are out of range, so tell them!
            return EmptyRange.getInstance();
        }

        long max = Math.min(topLimit, toValue);
        long min = Math.max(bottomLimit, fromValue);
        long half = (topLimit - bottomLimit) / 2;
        if(max - min > half) {
            return new ScanningIds(fromValue, toValue, fromInclusive, toInclusive, data);
        }

        bottomHandler.setValue(fromValue, fromInclusive);
        topHandler.setValue(toValue,toInclusive);
        while(bottomHandler.next() || topHandler.next()) {} // find the ids.

        int left = bottomHandler.result();
        int right = topHandler.result();
        int length = right - left + 1;

        if(sortedValues[left] != sortedValues[right]) {
            // We will need to copy and sort.
            System.arraycopy(sortedKeys, left, output, 0, length);
            Arrays.sort(output, 0, length);
            ids.setValues(output,0, length-1, true);
        } else {
            // already sorted.
            ids.setValues(sortedKeys, left, right, false);
        }
        return ids;
    }

    public class MyHandler {
        private long value;
        private final boolean isTop;
        private boolean include;
        private int bottomOffset;
        private int topOffset;

        public MyHandler(boolean isTop) {
            this.isTop = isTop;
        }

        public void setValue(long value, boolean include) {
            this.value = value;
            this.include = include;
            bottomOffset = 0;
            topOffset = sortedValues.length;
        }

        public boolean next() {
            int testOffset = bottomOffset + (topOffset-bottomOffset)/2;
            //System.out.println(bottomOffset + ":" + topOffset + ":" + testOffset + ":" + data[testOffset]+":"+value);
            if( include ? sortedValues[testOffset] >= value : sortedValues[testOffset] > value) {
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
                while(offset < sortedValues.length -1  && (include ? sortedValues[offset+1] <= value : sortedValues[offset+1] < value)) {
                    offset++;
                }
            } else {
                // need to scroll back to the first.
                while(offset > 0 && (include ? sortedValues[offset-1] >= value : sortedValues[offset-1] > value)) {
                    offset--;
                }
            }
            return offset;
        }
    }
}
