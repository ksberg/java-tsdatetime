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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Test4DateTimeAddTime extends TestCase {

    public static final boolean VERBOSE = true;

    // ----------------------------------------------
    // JUnit METHODS
    // ----------------------------------------------

    @Before
    public void setUp() {
    }

    @After
    protected void tearDown() {
    }

    // CASES:
    // Nominal -- zero to 1
    // Negative -- zero to prior
    // Carry seconds, minutes, hours, days

    @Test
    public void testAddMillis() {
        if (VERBOSE) System.out.println("testAddMillis");

        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,5);
        MutableDateTime dBefore2 = new MutableDateTime(2003,4,5);

        int before1 = dBefore1.millisecondsSinceMidnight();
        int before2 = dBefore2.millisecondsSinceMidnight();
        assertTrue("Zero time", before1 == 0);
        assertEquals("Mutable == Immutable", before1, before2);

        IDateTime dAfter1 = dBefore1.addMillis(1);
        IDateTime dAfter2 = dBefore2.addMillis(1);
        int after1 = dAfter1.millisecondsSinceMidnight();
        int after2 = dAfter2.millisecondsSinceMidnight();
        assertTrue("1ms", after1 == 1);
        assertEquals("Mutable == Immutable", after1, after2);

        if (VERBOSE) {
            System.out.println("dAfter1.millisecondsSinceMidnight() = " + after1);
            System.out.println("dAfter2.millisecondsSinceMidnight() = " + after2);
        }
        assertNotSame("Expect different objects", dBefore1, dAfter1);
        assertEquals("Expect same object",dBefore2, dAfter2);
    }

    @Test
    public void testAddMillisRollDate() {
        if (VERBOSE) System.out.println("testAddMillisRollDate");

        ImmutableDateTime dBeforeI = new ImmutableDateTime(2003,4,10);
        MutableDateTime dBeforeM = new MutableDateTime(2003,4,10);

        int before1 = dBeforeI.millisecondsSinceMidnight();
        int before2 = dBeforeM.millisecondsSinceMidnight();
        assertTrue("Zero time", before1 == 0);
        assertEquals("Mutable == Immutable", before1, before2);

        IDateTime dAfterI = dBeforeI.addMillis(IDateTime.MillisInDay+1);
        IDateTime dAfterM = dBeforeM.addMillis(IDateTime.MillisInDay+1);

        int mssmI = dAfterI.millisecondsSinceMidnight();
        int mssmM = dAfterM.millisecondsSinceMidnight();

        if (VERBOSE) {
            System.out.println("dBefore1.day() = " + dBeforeI.day());
            System.out.println("dAfter1.day()  = " + dAfterI.day());
            System.out.println("dAfter2.day()  = " + dAfterM.day());

            System.out.println("dAfter1.millisecondsSinceMidnight() = " + mssmI);
            System.out.println("dAfter2.millisecondsSinceMidnight() = " + mssmM);
            System.out.println("MillisInDay = " + IDateTime.MillisInDay);
        }


        assertTrue("Roll Millis From Day Millis Max", mssmI == 1);
        assertEquals("Mutable == Immutable", mssmI, mssmM);
        assertTrue("Roll back Days", dAfterI.day() == dBeforeI.day() + 1);
        assertTrue("Roll back Days", dAfterM.day() == dBeforeI.day() + 1);
        assertNotSame("Expect different objects", dBeforeI, dAfterI);
        assertEquals("Expect same object",dBeforeM, dAfterM);


        IDateTime dAfterI2 = dAfterI.addMillis(1 + 2*IDateTime.MillisInDay);
        IDateTime dAfterM2 = dAfterM.addMillis(1 + 2*IDateTime.MillisInDay);

        mssmI = dAfterI2.millisecondsSinceMidnight();
        mssmM = dAfterM2.millisecondsSinceMidnight();

        if (VERBOSE) {
            System.out.println("dAfterI2.day() = " + dAfterI2.day());
            System.out.println("dAfterM2.day() = " + dAfterM2.day());

            System.out.println("dAfter1.millisecondsSinceMidnight() = " + mssmI);
            System.out.println("dAfter2.millisecondsSinceMidnight() = " + mssmM);
        }

        assertTrue("Roll Millis From Day Millis Max", mssmI == 2);
        assertEquals("Mutable == Immutable", mssmI, mssmM);
        assertTrue("Roll back Days dAfterI2", dAfterI2.day() == dAfterI.day() + 2);
        assertTrue("Roll back Days dAfterM2", dAfterM2.day() == dAfterI.day() + 2);
    }

    @Test
    public void testAddMillisZero() {
        if (VERBOSE) System.out.println("testAddMillisZero");

        ImmutableDateTime   dBeforeI = new ImmutableDateTime(2003,4,5);
        MutableDateTime     dBeforeM = new MutableDateTime(2003,4,5);

        int mssmI = dBeforeI.millisecondsSinceMidnight();
        int mssmM = dBeforeM.millisecondsSinceMidnight();
        assertTrue("Zero time", mssmI == 0);
        assertEquals("Mutable == Immutable", mssmI, mssmM);

        IDateTime dAfterI = dBeforeI.addMillis(0);
        IDateTime dAfterM = dBeforeM.addMillis(0);
        mssmI = dAfterI.millisecondsSinceMidnight();
        mssmM = dAfterM.millisecondsSinceMidnight();

        if (VERBOSE) {
            System.out.println("dAfter1.millisecondsSinceMidnight() = " + mssmI);
            System.out.println("dAfter2.millisecondsSinceMidnight() = " + mssmM);
        }

        assertTrue("No Millis Change Expected", mssmI == 0);
        assertEquals("Mutable == Immutable", mssmI, mssmM);
        assertTrue("No Day Change Expected", dAfterI.day() == dBeforeI.day());
        assertNotSame("Expect different objects", dBeforeI, dAfterI);
        assertEquals("Expect same object",dBeforeM, dAfterM);
    }

    @Test
    public void testAddMillisNegative() {
        if (VERBOSE) System.out.println("testAddMillisNegative");

        ImmutableDateTime dBeforeI = new ImmutableDateTime(2003,4,10);
        MutableDateTime dBeforeM = new MutableDateTime(2003,4,10);

        int before1 = dBeforeI.millisecondsSinceMidnight();
        int before2 = dBeforeM.millisecondsSinceMidnight();
        assertTrue("Zero time", before1 == 0);
        assertEquals("Mutable == Immutable", before1, before2);

        IDateTime dAfterI = dBeforeI.addMillis(-1);
        IDateTime dAfterM = dBeforeM.addMillis(-1);

        int mssmI = dAfterI.millisecondsSinceMidnight();
        int mssmM = dAfterM.millisecondsSinceMidnight();

        if (VERBOSE) {
            System.out.println("dBefore1.day() = " + dBeforeI.day());
            System.out.println("dAfter1.day()  = " + dAfterI.day());
            System.out.println("dAfter2.day()  = " + dAfterM.day());

            System.out.println("dAfter1.millisecondsSinceMidnight() = " + mssmI);
            System.out.println("dAfter2.millisecondsSinceMidnight() = " + mssmM);
            System.out.println("MillisInDay = " + IDateTime.MillisInDay);
        }


        assertTrue("Roll Millis From Day Millis Max", mssmI == IDateTime.MillisInDay-1);
        assertEquals("Mutable == Immutable", mssmI, mssmM);
        assertTrue("Roll back Days", dAfterI.day() == dBeforeI.day() - 1);
        assertTrue("Roll back Days", dAfterM.day() == dBeforeI.day() - 1);
        assertNotSame("Expect different objects", dBeforeI, dAfterI);
        assertEquals("Expect same object",dBeforeM, dAfterM);


        IDateTime dAfterI2 = dAfterI.addMillis(-IDateTime.MillisInDay);
        IDateTime dAfterM2 = dAfterM.addMillis(-IDateTime.MillisInDay);

        mssmI = dAfterI2.millisecondsSinceMidnight();
        mssmM = dAfterM2.millisecondsSinceMidnight();

        if (VERBOSE) {
            System.out.println("dAfterI2.day() = " + dAfterI2.day());
            System.out.println("dAfterM2.day() = " + dAfterM2.day());

            System.out.println("dAfter1.millisecondsSinceMidnight() = " + mssmI);
            System.out.println("dAfter2.millisecondsSinceMidnight() = " + mssmM);
        }

        assertTrue("Roll Millis From Day Millis Max", mssmI == IDateTime.MillisInDay-1);
        assertEquals("Mutable == Immutable", mssmI, mssmM);
        assertTrue("Roll back Days dAfterI2", dAfterI2.day() == dAfterI.day() - 1);
        assertTrue("Roll back Days dAfterM2", dAfterM2.day() == dAfterI.day() - 1);

        IDateTime dAfterI3 = dAfterI2.addMillis(-1-IDateTime.MillisInDay);
        IDateTime dAfterM3 = dAfterM2.addMillis(-1-IDateTime.MillisInDay);

        mssmI = dAfterI3.millisecondsSinceMidnight();
        mssmM = dAfterM3.millisecondsSinceMidnight();

        if (VERBOSE) {
            System.out.println("dAfterI3.day() = " + dAfterI3.day());
            System.out.println("dAfterM3.day() = " + dAfterM3.day());

            System.out.println("dAfter1.millisecondsSinceMidnight() = " + mssmI);
            System.out.println("dAfter2.millisecondsSinceMidnight() = " + mssmM);
        }

        assertTrue("Roll Millis From Day Millis Max", mssmI == IDateTime.MillisInDay-2);
        assertEquals("Mutable == Immutable", mssmI, mssmM);
        assertTrue("Roll back Days dAfterI3", dAfterI3.day() == dAfterI2.day() - 1);
        assertTrue("Roll back Days dAfterM3", dAfterM3.day() == dAfterI2.day() - 1);

    }

    @Test
    public void testSeconds() {
        if (VERBOSE) System.out.println("testAddSeconds");

        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,5);
        MutableDateTime dBefore2 = new MutableDateTime(2003,4,5);

        int before1 = dBefore1.seconds();
        int before2 = dBefore2.seconds();
        assertTrue("Zero time", before1 == 0);
        assertEquals("Mutable == Immutable", before1, before2);

        IDateTime dAfter1 = dBefore1.addSeconds(1);
        IDateTime dAfter2 = dBefore2.addSeconds(1);
        int after1 = dAfter1.seconds();
        int after2 = dAfter2.seconds();

        if (VERBOSE) {
            System.out.println("dAfter1.seconds() = " + after1);
            System.out.println("dAfter2.seconds() = " + after2);
        }
        assertNotSame("Expect different objects", dBefore1, dAfter1);
        assertEquals("Expect same object",dBefore2, dAfter2);
    }

    @Test
    public void testMinutes() {
        if (VERBOSE) System.out.println("testAddMinutes");

        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,5);
        MutableDateTime dBefore2 = new MutableDateTime(2003,4,5);

        int before1 = dBefore1.minutes();
        int before2 = dBefore2.minutes();
        assertTrue("Zero time", before1 == 0);
        assertEquals("Mutable == Immutable", before1, before2);

        IDateTime dAfter1 = dBefore1.addMinutes(1);
        IDateTime dAfter2 = dBefore2.addMinutes(1);
        int after1 = dAfter1.minutes();
        int after2 = dAfter2.minutes();

        if (VERBOSE) {
            System.out.println("dAfter1.minutes() = " + after1);
            System.out.println("dAfter2.minutes() = " + after2);
        }
        assertNotSame("Expect different objects", dBefore1, dAfter1);
        assertEquals("Expect same object",dBefore2, dAfter2);
    }

    @Test
    public void testHours() {
        if (VERBOSE) System.out.println("testAddHours");

        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,5);
        MutableDateTime dBefore2 = new MutableDateTime(2003,4,5);

        int before1 = dBefore1.hours();
        int before2 = dBefore2.hours();
        assertTrue("Zero time", before1 == 0);
        assertEquals("Mutable == Immutable", before1, before2);

        IDateTime dAfter1 = dBefore1.addHours(1);
        IDateTime dAfter2 = dBefore2.addHours(1);
        int after1 = dAfter1.hours();
        int after2 = dAfter2.hours();

        if (VERBOSE) {
            System.out.println("dAfter1.hours() = " + after1);
            System.out.println("dAfter2.hours() = " + after2);
        }
        assertNotSame("Expect different objects", dBefore1, dAfter1);
        assertEquals("Expect same object",dBefore2, dAfter2);
    }
    
    
//    @Test
//    public void testDay() {
//        if (VERBOSE) System.out.println("testDay");
//
//        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,5);
//        MutableDateTime dBefore2 = new MutableDateTime(2003,4,5);
//
//        int before = dBefore1.day();
//        assertEquals("Mutable day() == Immutable day()", before, dBefore2.day());
//
//        IDateTime dAfter1 = dBefore1.addDays(1);
//        IDateTime dAfter2 = dBefore2.addDays(1);
//        int after = dAfter1.day();
//
//        assertTrue("Expect Day 5", before == 5);
//        assertTrue("Expect Day 6", after == 6);
//
//        assertTrue("Immutable Unchanged after addDays()", dBefore1.day() != dBefore2.day());
//        assertTrue("Immutable return == mutable.addDays()", dAfter1.day() == dBefore2.day());
//        assertEquals("Expect same object",dBefore2, dAfter2);
//    }
//
//
//
//    @Test
//    public void testMonth() {
//        if (VERBOSE) System.out.println("testMonth");
//
//        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,5);
//        MutableDateTime dBefore2 = new MutableDateTime(2003,4,5);
//        assertEquals("Mutable month() == Immutable month()", dBefore1.month(),dBefore2.month());
//
//        int before = dBefore1.month();
//        IDateTime dAfter1 = dBefore1.rollMonths(1);
//        IDateTime dAfter2 = dBefore2.rollMonths(1);
//        int after = dAfter1.month();
//
//        assertTrue("Expect Month 4",before == 4);
//        assertTrue("Expect Month 5",after == 5);
//
//        assertTrue("Immutable Unchanged after rollMonths()",   dBefore1.month() != dBefore2.month());
//        assertTrue("Immutable return == mutable.rollMonths()", dAfter1.month() == dAfter2.month());
//        assertEquals("Expect same object", dBefore2, dAfter2);
//    }
//
//
//
//    @Test
//    public void testYear() {
//        if (VERBOSE) System.out.println("testYear");
//
//        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,5);
//        MutableDateTime dBefore2 = new MutableDateTime(2003,4,5);
//
//        assertEquals("Mutable year() == Immutable year()", dBefore1.year(),dBefore2.year());
//
//        int before = dBefore1.year();
//        IDateTime dAfter1 = dBefore1.addYears(1);
//        IDateTime dAfter2 = dBefore2.addYears(1);
//        int after = dAfter1.year();
//
//        assertTrue("Expect Month 4",before == 2003);
//        assertTrue("Expect Month 5",after == 2004);
//
//        assertTrue("Immutable year() Unchanged after addYears()",   dBefore1.year() != dBefore2.year());
//        assertTrue("Immutable addYears() == mutable.addYears()",    dAfter1.year() == dAfter2.year());
//        assertEquals("Expect same object", dBefore2, dAfter2);
//    }
//
//
//    @Test
//    public void testRep() {
//        if (VERBOSE) System.out.println("testRep");
//
//        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,5);
//        MutableDateTime dBefore2 = new MutableDateTime(2003,4,5);
//
//        assertEquals("Mutable intRep() == Immutable intRep()", dBefore1.intRep(),dBefore2.intRep());
//
//        IDateTime dAfter2 = dBefore2.addYears(1);
//        assertTrue("Immutable Rep Unchanged after addYears()",   dBefore1.intRep() != dBefore2.intRep());
//        assertEquals("Expect same object", dBefore2, dAfter2);
//    }
//
//
//    @Test
//    public void testDayOfYear() {
//        if (VERBOSE) System.out.println("testDayOfYear");
//
//        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,5);
//        MutableDateTime dBefore2 = new MutableDateTime(2003,4,5);
//
//        assertEquals("Mutable dayOfYear() == Immutable dayOfYear()", dBefore1.dayOfYear(),dBefore2.dayOfYear());
//
//        IDateTime dAfter1 = dBefore1.addDays(1);
//        IDateTime dAfter2 = dBefore2.addDays(1);
//        assertTrue("Immutable year() Unchanged after addYears()",   dBefore1.dayOfYear() != dBefore2.dayOfYear());
//        assertTrue("Immutable addYears() == mutable.addYears()",    dAfter1.dayOfYear() == dAfter2.dayOfYear());
//        assertEquals("Expect same object", dBefore2, dAfter2);
//    }
//
//
//    @Test
//    public void testDayOfWeek() {
//        if (VERBOSE) System.out.println("testDayOfWeek");
//
//        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,5);
//        MutableDateTime dBefore2 = new MutableDateTime(2003,4,5);
//
//        assertEquals("Mutable dayOfWeek() == Immutable dayOfWeek()", dBefore1.dayOfWeek(),dBefore2.dayOfWeek());
//
//        IDateTime dAfter1 = dBefore1.rollToDayOfWeek(2);
//        IDateTime dAfter2 = dBefore2.rollToDayOfWeek(2);
//        assertTrue("Immutable year() Unchanged after addYears()",   dBefore1.dayOfYear() != dBefore2.dayOfYear());
//        assertTrue("Immutable addYears() == mutable.addYears()",    dAfter1.dayOfYear() == dAfter2.dayOfYear());
//        assertTrue("Expected Day of Week", "Wednesday".equalsIgnoreCase(dAfter1.dayName()));
//        assertTrue("Expected Day of Week", "Wed".equalsIgnoreCase(dAfter1.dayAbbreviation()));
//        assertEquals("Expect same object", dBefore2, dAfter2);
//
//    }
//
//
////    @Test
////    public void testBusinessDayOfMonth() {
////        if (VERBOSE) System.out.println("testBusinessDayOfMonth");
////
////        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,15);
////        MutableDateTime dBefore2 = new MutableDateTime(2003,4,15);
////
////        assertEquals("Mutable dayOfWeek() == Immutable dayOfWeek()", dBefore1.businessDayOfMonth(),dBefore2.businessDayOfMonth());
////
////        IDateTime dAfter1 = dBefore1.nextBusinessDay();
////        IDateTime dAfter2 = dBefore2.nextBusinessDay();
////        assertTrue("Immutable year() Unchanged after addYears()",   dBefore1.businessDayOfMonth() != dBefore2.businessDayOfMonth());
////        assertTrue("Immutable addYears() == mutable.addYears()",    dAfter1.businessDayOfMonth() == dAfter2.businessDayOfMonth());
////        assertEquals("Expect same object", dBefore2, dAfter2);
////
////        assertTrue("Weekday", dAfter1.dayOfWeek() < 5);
////    }
//
//    @Test
//    public void testDaysInYear() {
//        if (VERBOSE) System.out.println("testDaysInYear");
//
//        ImmutableDateTime dLeap       = new ImmutableDateTime(2012,2,29);     // 2012 is leap year
//        ImmutableDateTime dBefore1    = new ImmutableDateTime(2011,4,15);     // 2011 is not
//        ImmutableDateTime dAfter1     = new ImmutableDateTime(2013,4,15);     // 2013 is not
//        ImmutableDateTime dLeapNext   = new ImmutableDateTime(2016,2,29);     // 2016 is leap year
//
//
//        assertTrue("Leap year days > year before",       dLeap.numberOfDaysInYear() > dBefore1.numberOfDaysInYear());
//        assertTrue("Leap year days > year after",        dLeap.numberOfDaysInYear() > dAfter1.numberOfDaysInYear());
//        assertTrue("Day count 2 different leap years",   dLeap.numberOfDaysInYear() == dLeapNext.numberOfDaysInYear());
//
//        assertTrue("2012 Feb 29 is Wednesday",  dLeap.dayOfWeek() == DaysAndMonths.WEDNESDAY);
//        assertTrue("2016 Feb 29 is Monday", dLeapNext.dayOfWeek() == DaysAndMonths.MONDAY);
//    }
//
//
//    @Test
//    public void testWeekOfMonth() {
//        if (VERBOSE) System.out.println("testWeekOfMonth");
//
//        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,15);
//        MutableDateTime dBefore2 = new MutableDateTime(2003,4,15);
//
//        assertEquals("Mutable weekOfMonth() == Immutable weekOfMonth()", dBefore1.weekOfMonth(),dBefore2.weekOfMonth());
//        int before = dBefore1.weekOfMonth();
//        IDateTime dAfter1 = dBefore1.addWeeks(1);
//        IDateTime dAfter2 = dBefore2.addWeeks(1);
//        int after = dAfter1.weekOfMonth();
//        assertTrue("Sequential Weeks", after == before + 1);
//
//        assertTrue("Immutable weekOfMonth() Unchanged after addWeeks()",   dBefore1.weekOfMonth() != dBefore2.weekOfMonth());
//        assertTrue("Immutable weekOfMonth() == mutable.weekOfMonth()",    dAfter1.weekOfMonth() == dAfter2.weekOfMonth());
//        assertNotSame("Expect different",dBefore1, dAfter1);
//        assertEquals("Expect same object",dBefore2, dAfter2);
//    }
//
//    @Test
//    public void testWeekOfYear() {
//        if (VERBOSE) System.out.println("testWeekOfYear");
//
//        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,15);
//        MutableDateTime dBefore2 = new MutableDateTime(2003,4,15);
//
//        assertEquals("Mutable weekOfYear() == Immutable weekOfYear()", dBefore1.weekOfYear(),dBefore2.weekOfYear());
//        int before = dBefore1.weekOfYear();
//        IDateTime dAfter1 = dBefore1.addWeeks(1);
//        IDateTime dAfter2 = dBefore2.addWeeks(1);
//        int after = dAfter1.weekOfYear();
//        assertTrue("Sequential Weeks", after == before + 1);
//
//        assertTrue("Immutable weekOfYear() Unchanged after addWeeks()",   dBefore1.weekOfYear() != dBefore2.weekOfYear());
//        assertTrue("Immutable weekOfYear() == mutable.weekOfYear()",    dAfter1.weekOfYear() == dAfter2.weekOfYear());
//        assertNotSame("Expect different",dBefore1, dAfter1);
//        assertEquals("Expect same object",dBefore2, dAfter2);
//    }

}
