package com.workday;

import com.workday.interview.andrewwilson.better.SortingRangeContainer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by drewwilson on 21/11/2017.
 */
public class HandlerTest {
    private static long[] data = { 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4};

    @Test
    public void testBottom() {
        SortingRangeContainer.MyHandler myHandler = new SortingRangeContainer.MyHandler(data, 2, false);
        while(!myHandler.next()) {}
        assertEquals(1, myHandler.result());
    }

    @Test
    public void testTop() {
        SortingRangeContainer.MyHandler myHandler = new SortingRangeContainer.MyHandler(data, 2, true);
        while(!myHandler.next()) {}
        assertEquals(4, myHandler.result());
    }

    @Test
    public void testTopOutofRange() {
        SortingRangeContainer.MyHandler myHandler = new SortingRangeContainer.MyHandler(data, 5, true);
        while(!myHandler.next()) {}
        assertEquals(10, myHandler.result());
    }

    @Test
    public void testBottomOutofRange() {
        SortingRangeContainer.MyHandler myHandler = new SortingRangeContainer.MyHandler(data, 0, false);
        while(!myHandler.next()) {}
        assertEquals(0, myHandler.result());
    }

}
