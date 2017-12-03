package com.workday.interview.andrewwilson.combining;

import com.workday.interview.Ids;
import com.workday.interview.RangeContainer;
import com.workday.interview.andrewwilson.binarySearch.BinarySearchRangeContainer;
import com.workday.interview.andrewwilson.empty.EmptyIds;
import com.workday.interview.andrewwilson.scanning.ScanningRangeContainer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

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
    public static final Logger LOG = Logger.getLogger(CombiningRangeContainer.class);

    private final Map<Thread, Pair<BinarySearchRangeContainer, ScanningRangeContainer>> threadPairMap = new ConcurrentHashMap<>();
    private final long[] data;
    private final boolean checkThreadEachTime;

    public CombiningRangeContainer(long[] data, boolean checkThreadEachTime) {
        this.checkThreadEachTime = checkThreadEachTime;
        if(data.length > Short.MAX_VALUE) {
            throw new ArrayIndexOutOfBoundsException("Data has length greater than 32K : " + data.length);
        }
        this.data = data;
    }

    @Override
    public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        Pair<BinarySearchRangeContainer, ScanningRangeContainer> pair = threadPairMap.computeIfAbsent(Thread.currentThread(),
                k -> new ImmutablePair<>(new BinarySearchRangeContainer(data, checkThreadEachTime), new ScanningRangeContainer(data, checkThreadEachTime)));

        // @todo we could warn here if the pool gets too large, which suggests large thread usage.
        // I'd prefer to throw an Exception, but we probably need to keep going in a PROD situation.
        if(threadPairMap.size() * 2 > Runtime.getRuntime().availableProcessors()) {
            LOG.error("Range Container is being used incorrectly and probably leaking");
        }

        BinarySearchRangeContainer binarySearchRangeContainer = pair.getLeft();

        if(binarySearchRangeContainer.useEmpty(fromValue, toValue, fromInclusive, toInclusive)) {
            return EmptyIds.getInstance();
        }

        RangeContainer rangeContainer = binarySearchRangeContainer.worthUsing(fromValue,toValue) ?
                binarySearchRangeContainer : pair.getRight();  // otherwise use the Scanner.

        return rangeContainer.findIdsInRange(fromValue, toValue, fromInclusive, toInclusive);
    }

    public static boolean checkThread(Thread owningThread) {
        if(!Thread.currentThread().equals(owningThread)) {
            throw new IllegalThreadStateException("Caller thread " + Thread.currentThread() + " is not creating thread " + owningThread);
        }
        return true;
    }
}
