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
    private final boolean exceptionOnTooManyThreads;
    private final boolean checkFullyDrainedOnceOnly;
    private final boolean useNio;
    private boolean isWarnedThreadUsage;

    /**
     * Suggested usage constructor.
     * @param data The data to use
     */
    public CombiningRangeContainer(long data[]) {
        this(data, false, false, false, true);
    }

    /**
     * Constructor
     * @param data - the original data
     * @param checkThreadEachTime - should we check the thread each time
     * @param exceptionOnTooManyThreads - should we throw an exception if too many threads created.
     * @param checkFullyDrainedOnceOnly - check the result is fully drained once only each time.
     * @param useNio - use NIO rather than Java Heap memory.
     */
    public CombiningRangeContainer( long[] data,
                                    boolean checkThreadEachTime,
                                    boolean exceptionOnTooManyThreads,
                                    boolean checkFullyDrainedOnceOnly,
                                    boolean useNio) {
        this.checkThreadEachTime = checkThreadEachTime;
        this.exceptionOnTooManyThreads = exceptionOnTooManyThreads;
        this.checkFullyDrainedOnceOnly = checkFullyDrainedOnceOnly;
        this.useNio = useNio;
        if(data == null || data.length == 0 ) {
            throw new ArrayIndexOutOfBoundsException("Data should not be empty in Range Container");
        }
        if(data.length > Short.MAX_VALUE) {
            throw new ArrayIndexOutOfBoundsException("Data has length greater than 32K : " + data.length);
        }
        this.data = data;
    }

    @Override
    public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
        Pair<BinarySearchRangeContainer, ScanningRangeContainer> pair = threadPairMap.computeIfAbsent(Thread.currentThread(),
                k -> new ImmutablePair<>(new BinarySearchRangeContainer(data, checkThreadEachTime, checkFullyDrainedOnceOnly, useNio), new ScanningRangeContainer(data, checkThreadEachTime, checkFullyDrainedOnceOnly)));

        // we could warn here if the pool gets too large, which suggests large thread usage.
        // I'd prefer to throw an Exception, but we probably need to keep going in a PROD situation.
        if( threadPairMap.size() * 2 > Runtime.getRuntime().availableProcessors()) {
            String message = "Range Container is being used incorrectly and probably leaking size : " + threadPairMap.size();
            if( exceptionOnTooManyThreads) {
                throw new IllegalThreadStateException(message);
            } else if(isWarnedThreadUsage ) {
                LOG.error(message);
                isWarnedThreadUsage = true;
            }
        }

        BinarySearchRangeContainer binarySearchRangeContainer = pair.getLeft();

        if(binarySearchRangeContainer.useEmpty(fromValue, toValue, fromInclusive, toInclusive)) {
            return EmptyIds.getInstance();
        }

        RangeContainer rangeContainer = binarySearchRangeContainer.worthUsing(fromValue,toValue) ?
                binarySearchRangeContainer : pair.getRight();  // otherwise use the Scanner.

        return rangeContainer.findIdsInRange(fromValue, toValue, fromInclusive, toInclusive);
    }

    public static void checkThread(Thread owningThread) {
        if(!Thread.currentThread().equals(owningThread)) {
            throw new IllegalThreadStateException("Caller thread " + Thread.currentThread() + " is not creating thread " + owningThread);
        }
    }
}
