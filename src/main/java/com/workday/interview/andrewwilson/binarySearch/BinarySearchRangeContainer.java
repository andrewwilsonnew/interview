package com.workday.interview.andrewwilson.binarySearch;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This is the main work in the class, it is explained here
 * https://junocube.atlassian.net/wiki/spaces/COH/pages/119635969/Interview
 */
public class BinarySearchRangeContainer implements RangeContainer {
    private static final Logger LOG = Logger.getLogger(BinarySearchRangeContainer.class);

    //@Contended
    private final short[] sortedKeys;
    //@Contended
    private final long[] sortedValues;
    //@Contended
    private final short[] output;

    private final long bottomLimit;
    private final long topLimit;
    private final BinaryHandler bottomHandler = new BinaryHandler(false);
    private final BinaryHandler topHandler = new BinaryHandler(true);
    private final BinarySearchIds ids;


    public BinarySearchRangeContainer(long[] data, boolean checkThreadEachTime, boolean nio) {
        ids = new BinarySearchIds(checkThreadEachTime);
        output = new short[data.length];
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

        sortedKeys = nio ? ShortBuffer.allocate(data.length).array() : new short[data.length];
        sortedValues = nio ? LongBuffer.allocate(data.length).array() : new long[data.length];

        // write out the 2 arrays in sorted order.
        for(short i=0;i<data.length;i++) {
            sortedKeys[i] = sorted.get(i).getRight();
            sortedValues[i] = sorted.get(i).getLeft();
        }

        bottomLimit = sortedValues[0];
        topLimit = sortedValues[data.length-1];
    }

    public boolean worthUsing(long fromValue, long toValue) {
        long max = Math.min(topLimit, toValue);
        long min = Math.max(bottomLimit, fromValue);
        long half = (topLimit - bottomLimit) / 2;
        return (max - min < half);
    }

    @Override
    public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {



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

    public boolean useEmpty(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        return  (fromInclusive ? fromValue > topLimit : fromValue >= topLimit) || // we are over the topLimit
                (toInclusive ? toValue < bottomLimit : toValue <= bottomLimit) || // we are below the bottomLimit
                ( toInclusive && fromInclusive ? fromValue > toValue : fromValue >= toValue );  // ranges don't overlap
    }

    public class BinaryHandler {
        private long value;
        private final boolean isTop;
        private boolean include;
        private int bottomOffset;
        private int topOffset;

        public BinaryHandler(boolean isTop) {
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
            if(LOG.isDebugEnabled()) { LOG.debug(bottomOffset + ":" + topOffset + ":" + testOffset + ":" + sortedValues[testOffset]+":"+value); }
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
