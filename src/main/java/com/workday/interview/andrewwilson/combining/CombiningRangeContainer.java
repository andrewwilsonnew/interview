package com.workday.interview.andrewwilson.combining;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchRangeContainer;
import com.workday.interview.andrewwilson.scanning.ScanningRangeContainer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This pulls everything together.
 * Chooses :
 *  <li>BinarySearch</li>
 *  <li>Scanning</li>
 *  <li>Empty</li>
 *
 *  Handles threading.
 */
public class CombiningRangeContainer implements RangeContainer {
    private final Map<Thread, Pair<BinarySearchRangeContainer, ScanningRangeContainer>> threadPairMap = new ConcurrentHashMap<>();
    private final long[] data;

    public CombiningRangeContainer(long[] data) {
        if(data.length > Short.MAX_VALUE) {
            throw new ArrayIndexOutOfBoundsException("Data has length greater than 32K : " + data.length);
        }
        this.data = data;
    }

    @Override
    public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        Pair<BinarySearchRangeContainer, ScanningRangeContainer> pair = threadPairMap.computeIfAbsent(Thread.currentThread(),
                k -> new ImmutablePair<>(new BinarySearchRangeContainer(data), new ScanningRangeContainer(data)));

        BinarySearchRangeContainer binarySearchRangeContainer = pair.getLeft();

        if(binarySearchRangeContainer.worthUsing(fromValue,toValue)) {
            return binarySearchRangeContainer.findIdsInRange(fromValue, toValue, fromInclusive, toInclusive);
        } else {
            return pair.getRight().findIdsInRange(fromValue, toValue, fromInclusive, toInclusive);
        }
    }
}
