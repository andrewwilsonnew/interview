package com.workday.interview;

/**
 * Created by drewwilson on 22/11/2017.
 */
public class WorstTestCases {

    /**
     * The worst test cases.
     * 1. return the entire 32K row - its slow and ugly - iterate through and return is probably the best - my original solution.
     * 2. A single id at the end.  This would be really bad with the linear search.
     * 3. Things that return nothing - this should return a singleton - ranges backwards.  ranges below our range.  ranges above our range.
     * I like my solution, it respects the ranges and does what it needs to do.
     * Now lets optimise it.  Java - prof probably the best way to start.
      */


}
