/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2001-2014, Kevin Sven Berg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ***** END LICENSE BLOCK ***** */

package bitzguild.ts.datetime;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test4DateTimeValues extends TestCase {

    public static final boolean VERBOSE = false;

    // ----------------------------------------------
    // JUnit METHODS
    // ----------------------------------------------

    @Before
    public void setUp() {
    }

    @After
    protected void tearDown() {
    }

    @Test
    public void testYear() {
        if (VERBOSE) System.out.println("testYear");

        ImmutableDateTime 	dti1 = null;

        dti1 = new ImmutableDateTime(1921,8,7);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.year()", dti1.year() == 1921);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day()", dti1.day() == 7);

        dti1 = new ImmutableDateTime(31234,8,7);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.year()", dti1.year() == 31234);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day()", dti1.day() == 7);

        dti1 = new ImmutableDateTime(0,8,7);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.year()", dti1.year() == 0);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day()", dti1.day() == 7);

        dti1 = new ImmutableDateTime(-1,8,7);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.year()", dti1.year() == -1);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day()", dti1.day() == 7);
    }

    @Test
    public void testMonth() {
        if (VERBOSE) System.out.println("testMonth");

        ImmutableDateTime 	dti1 = null;

        dti1 = new ImmutableDateTime(1921,8,7);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.year()", dti1.year() == 1921);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day()", dti1.day() == 7);

        dti1 = new ImmutableDateTime(1921,0,7);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.year()", dti1.year() == 1921);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 1);
        assertTrue("ImmutableDateTime.day()", dti1.day() == 7);


        dti1 = new ImmutableDateTime(1921,13,7);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());

        dti1 = new ImmutableDateTime(1921,-1,7);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());

    }

    @Test
    public void testDay() {
        if (VERBOSE) System.out.println("testDay");

        ImmutableDateTime 	dti1 = null;

        dti1 = new ImmutableDateTime(1921,8,1);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.year()", dti1.year() == 1921);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day()", dti1.day() == 1);

        dti1 = new ImmutableDateTime(1921,8,0);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.year()", dti1.year() == 1921);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day()", dti1.day() == 1);

        dti1 = new ImmutableDateTime(1921,8,365);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.year()", dti1.year() == 1921);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day()", dti1.day() == 31);

    }

    /**
     * Time values are summed ... don't really care
     * if
     */
    @Test
    public void testHour() {
        if (VERBOSE) System.out.println("testHour");

        ImmutableDateTime 	dti1 = null;

        dti1 = new ImmutableDateTime(1921,8,1,0,0,0,0);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.year()", dti1.year() == 1921);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day()", dti1.day() == 1);
        assertTrue("ImmutableDateTime.day()", dti1.hours() == 0);

        dti1 = new ImmutableDateTime(1921,8,1,1,0,0,0);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.day()", dti1.hours() == 1);

        dti1 = new ImmutableDateTime(1921,8,5,25,0,0,0);
        if (VERBOSE) System.out.println("DATE  = " + dti1.toString());
        assertTrue("ImmutableDateTime.day()", dti1.hours() == 25);

    }

    @Test
    public void testNumberOfDaysInYear() {
        if (VERBOSE) System.out.println("testNumberOfDaysInYear");

        ImmutableDateTime dLeap       = new ImmutableDateTime(2012,2,29);     // 2012 is leap year
        ImmutableDateTime dBefore1    = new ImmutableDateTime(2011,4,15);     // 2011 is not
        ImmutableDateTime dAfter1     = new ImmutableDateTime(2013,4,15);     // 2013 is not
        ImmutableDateTime dLeapNext   = new ImmutableDateTime(2016,2,29);     // 2016 is leap year


        assertTrue("Leap year days > year before",       dLeap.numberOfDaysInYear() > dBefore1.numberOfDaysInYear());
        assertTrue("Leap year days > year after",        dLeap.numberOfDaysInYear() > dAfter1.numberOfDaysInYear());
        assertTrue("Day count 2 different leap years",   dLeap.numberOfDaysInYear() == dLeapNext.numberOfDaysInYear());

        assertTrue("2012 Feb 29 is Wednesday",  dLeap.dayOfWeek() == DaysAndMonths.WEDNESDAY);
        assertTrue("2016 Feb 29 is Monday", dLeapNext.dayOfWeek() == DaysAndMonths.MONDAY);

    }

    @Test
    public void testDaysLeftInYear() {
        if (VERBOSE) System.out.println("testDaysLeftInYear");

        ImmutableDateTime dt = new ImmutableDateTime(2003,4,5);
        MutableDateTime dtm = new MutableDateTime(2003,4,5);

        assertEquals("Mutable daysLeftOfYear() == Immutable daysLeftOfYear()", dt.daysLeftOfYear(),dtm.daysLeftOfYear());
        int beforeDayOfYear = dt.dayOfYear();
        int beforeDaysLeft = dt.daysLeftOfYear();
        assertTrue("Days add to 365", 366 > beforeDayOfYear + beforeDaysLeft);

    }

    @Test
    public void testWeekOfMonth() {
        if (VERBOSE) System.out.println("testWeekOfMonth");

        ImmutableDateTime 	dti1 = null;

        dti1 = new ImmutableDateTime(1921,1,1);
        assertTrue("Week of Month 1",   dti1.weekOfMonth() == 1);

        dti1 = new ImmutableDateTime(1921,1,8);
        assertTrue("Week of Month 2",   dti1.weekOfMonth() == 2);

        dti1 = new ImmutableDateTime(1921,1,15);
        assertTrue("Week of Month 3",   dti1.weekOfMonth() == 3);

        dti1 = new ImmutableDateTime(1921,1,22);
        assertTrue("Week of Month 4",   dti1.weekOfMonth() == 4);

        dti1 = new ImmutableDateTime(1921,1,29);
        assertTrue("Week of Month 5",   dti1.weekOfMonth() == 5);


        dti1 = new ImmutableDateTime(1921,2,1);
        assertTrue("Week of Month 1",   dti1.weekOfMonth() == 1);

        dti1 = new ImmutableDateTime(1921,2,8);
        assertTrue("Week of Month 2",   dti1.weekOfMonth() == 2);

        dti1 = new ImmutableDateTime(1921,2,15);
        assertTrue("Week of Month 3",   dti1.weekOfMonth() == 3);

        dti1 = new ImmutableDateTime(1921,2,22);
        assertTrue("Week of Month 4",   dti1.weekOfMonth() == 4);

        dti1 = new ImmutableDateTime(1921,2,29);
        assertTrue("Week of Month 4",   dti1.weekOfMonth() == 4);


        dti1 = new ImmutableDateTime(1921,12,1);
        assertTrue("Week of Month 1",   dti1.weekOfMonth() == 1);

        dti1 = new ImmutableDateTime(1921,12,8);
        assertTrue("Week of Month 2",   dti1.weekOfMonth() == 2);

        dti1 = new ImmutableDateTime(1921,12,15);
        assertTrue("Week of Month 3",   dti1.weekOfMonth() == 3);

        dti1 = new ImmutableDateTime(1921,12,22);
        assertTrue("Week of Month 4",   dti1.weekOfMonth() == 4);

        dti1 = new ImmutableDateTime(1921,12,29);
        assertTrue("Week of Month 5",   dti1.weekOfMonth() == 5);
    }

}
