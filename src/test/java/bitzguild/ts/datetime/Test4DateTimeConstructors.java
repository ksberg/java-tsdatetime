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

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;

public class Test4DateTimeConstructors extends TestCase {

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

    @Test
    public void testYearMonthDay() {
        if (VERBOSE) System.out.println("testYearMonthDay");

        ImmutableDateTime 	dti1 = new ImmutableDateTime(2003,4,5);
        MutableDateTime 	dtm1 = new MutableDateTime(2003,4,5);

        assertTrue("Zero Time - new new ImmutableDateTime(2003,4,5)", dti1.minutesSinceMidnight() == 0);
        assertTrue("Zero Time - new new MutableDateTime(2003,4,5)", dtm1.minutesSinceMidnight() == 0);
       
        assertTrue("Zero Time Millis - ImmutableDateTime", dti1.millis() == 0);
        assertTrue("Zero Time Millis - MutableDateTime", dtm1.millis() == 0);

        assertTrue("ImmutableDateTime.year()", dti1.year() == 2003);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 4);
        assertTrue("ImmutableDateTime.day() ", dti1.day() == 5);
        
        assertTrue("MutableDateTime.year()", dtm1.year() == 2003);
        assertTrue("MutableDateTime.month()", dtm1.month() == 4);
        assertTrue("MutableDateTime.day() ", dtm1.day() == 5);
    }
    
    
    
    @Test
    public void testYearAndDayCount() {
        if (VERBOSE) System.out.println("testYearAndDayCount");

        ImmutableDateTime dti1 = new ImmutableDateTime(2002,5);
        MutableDateTime dtm1 = new MutableDateTime(2002,5);
        
        assertTrue("Zero Time - ImmutableDateTime.millisecondsSinceMidnight() == 0", dti1.millisecondsSinceMidnight() == 0);
        assertTrue("Zero Time - MutableDateTime.millisecondsSinceMidnight()   == 0", dtm1.millisecondsSinceMidnight() == 0);

        assertTrue("ImmutableDateTime.dayOfYear() ", dti1.dayOfYear() == 5);
        assertTrue("MutableDateTime.dayOfYear() ", dtm1.dayOfYear() == 5);
        
        assertTrue("ImmutableDateTime.year()", dti1.year() == 2002);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 1);
        assertTrue("ImmutableDateTime.day() ", dti1.day() == 5);
        
        assertTrue("MutableDateTime.year()", dtm1.year() == 2002);
        assertTrue("MutableDateTime.month()", dtm1.month() == 1);
        assertTrue("MutableDateTime.day() ", dtm1.day() == 5);
    }
    
    
    
    @Test
    public void testRep() {
        if (VERBOSE) System.out.println("testRep");

        ImmutableDateTime 	dti1 = new ImmutableDateTime(2003,4,5);
        MutableDateTime 	dtm1 = new MutableDateTime(2003,4,5);

        long rep = dtm1.rep();
        
        ImmutableDateTime dti2 	= new ImmutableDateTime(rep);
        MutableDateTime dtm2 	= new MutableDateTime(rep);
        
        assertTrue("Zero Time - ImmutableDateTime(2002,1)", dti2.minutesSinceMidnight() == 0);
        assertTrue("Zero Time - MutableDateTime(2002,1)", dtm2.minutesSinceMidnight() == 0);
        
        assertEquals("Rep Copy == Original - new ImmutableDateTime(rep)",dti1,dti2);
        assertEquals("Rep Copy == Original - new MutableDateTime(rep)",dtm1,dtm2);
        
        ImmutableDateTime dti3 	= new ImmutableDateTime(0L);
        MutableDateTime dtm3 	= new MutableDateTime(0L);

        assertNotNull(dti3);
        assertNotNull(dtm3);
    }
    
    @Test
    public void testYearMonthDayHourMinuteSecondMillis() {
        if (VERBOSE) System.out.println("testYearMonthDayHourMinuteSecondMillis");

        ImmutableDateTime 	dti1 = new ImmutableDateTime(2009,8,7,6,54,32,1);
        MutableDateTime 	dtm1 = new MutableDateTime(2009,8,7,6,54,32,1);

        if (VERBOSE) {
            System.out.println("ImmutableDateTime.millis  = " + dti1.millis());
            System.out.println("ImmutableDateTime = " + dti1.toString());
            System.out.println("MutableDateTime   = " + dtm1.toString());
        }
        
        assertTrue("ImmutableDateTime.year()", dti1.year() == 2009);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day() ", dti1.day() == 7);
        assertTrue("ImmutableDateTime.hours() ", dti1.hours() == 6);
        assertTrue("ImmutableDateTime.minutes() ", dti1.minutes() == 54);
        assertTrue("ImmutableDateTime.seconds() ", dti1.seconds() == 32);
        assertTrue("ImmutableDateTime.millis() ", dti1.millis() == 1);
        
        assertTrue("MutableDateTime.year()", dtm1.year() == 2009);
        assertTrue("MutableDateTime.month()", dtm1.month() == 8);
        assertTrue("MutableDateTime.day() ", dtm1.day() == 7);
        assertTrue("MutableDateTime.hours() ", dtm1.hours() == 6);
        assertTrue("MutableDateTime.minutes() ", dtm1.minutes() == 54);
        assertTrue("MutableDateTime.seconds() ", dtm1.seconds() == 32);
        assertTrue("MutableDateTime.millis() ", dtm1.millis() == 1);
    }
    
    @Test
    public void testCrossMutableImmutable() {
        if (VERBOSE) System.out.println("testCrossMutableImmutable");
    	
        ImmutableDateTime 	dti1 = new ImmutableDateTime(2009,8,7,6,54,32,1);
        MutableDateTime 	dtm1 = new MutableDateTime(dti1);
        
        if (VERBOSE) {
            System.out.println("ImmutableDateTime.hours   = " + dti1.hours());
            System.out.println("ImmutableDateTime.minutes = " + dti1.minutes());
            System.out.println("ImmutableDateTime.seconds = " + dti1.seconds());
            System.out.println("ImmutableDateTime.millis  = " + dti1.millis());
            
            System.out.println("ImmutableDateTime = " + dti1.toString());
            System.out.println("MutableDateTime   = " + dtm1.toString());
        }
        
        assertEquals("MutableDateTime(ImmutableDateTime)",dti1,dtm1);
        
        MutableDateTime 	dtm2 = new MutableDateTime(2009,8,7,6,54,32,1); 
        ImmutableDateTime 	dti2 = new ImmutableDateTime(dtm2);

        if (VERBOSE) {
            System.out.println("ImmutableDateTime = " + dti2.toString());
            System.out.println("MutableDateTime   = " + dtm2.toString());
        }
        
        assertEquals("ImmutableDateTime(MutableDateTime)",dtm2,dti2);
        
        MutableDateTime 	dtm3a = new MutableDateTime(2009,8,7,6,54,32,1); 
        MutableDateTime 	dtm3b = new MutableDateTime(dtm3a);
        
        assertEquals("MutableDateTime(MutableDateTime)",dtm3a,dtm3b);
        
        ImmutableDateTime 	dti3a = new ImmutableDateTime(2009,8,7,6,54,32,1); 
        ImmutableDateTime 	dti3b = new ImmutableDateTime(dtm3a);
        
        assertEquals("ImmutableDateTime(ImmutableDateTime)",dti3a,dti3b);
        
        
    }
    
    @Test
    public void testStaticYearMonthDay() {
        if (VERBOSE) System.out.println("testStaticYearMonthDay");

        ImmutableDateTime 	dti1 = ImmutableDateTime.yearMonthDay(2009,8,7);
        MutableDateTime 	dtm1 = MutableDateTime.yearMonthDay(2009,8,7);

        if (VERBOSE) {
            System.out.println("ImmutableDateTime = " + dti1.toString());
            System.out.println("MutableDateTime   = " + dtm1.toString());
        }
        
        assertTrue("ImmutableDateTime.year()", dti1.year() == 2009);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day() ", dti1.day() == 7);
        assertTrue("ImmutableDateTime.hours() ", dti1.hours() == 0);
        assertTrue("ImmutableDateTime.minutes() ", dti1.minutes() == 0);
        assertTrue("ImmutableDateTime.seconds() ", dti1.seconds() == 0);
        assertTrue("ImmutableDateTime.millis() ", dti1.millis() == 0);
        
        assertTrue("MutableDateTime.year()", dtm1.year() == 2009);
        assertTrue("MutableDateTime.month()", dtm1.month() == 8);
        assertTrue("MutableDateTime.day() ", dtm1.day() == 7);
        assertTrue("MutableDateTime.hours() ", dtm1.hours() == 0);
        assertTrue("MutableDateTime.minutes() ", dtm1.minutes() == 0);
        assertTrue("MutableDateTime.seconds() ", dtm1.seconds() == 0);
        assertTrue("MutableDateTime.millis() ", dtm1.millis() == 0);
    }
    
    @Test
    public void testStaticYearMonthDayHourMinuteSecondMillis() {
        if (VERBOSE) System.out.println("testStaticYearMonthDayHourMinuteSecondMillis");

        ImmutableDateTime 	dti1 = ImmutableDateTime.yearMonthDayHourMinuteSecond(2009,8,7,6,54,32);
        MutableDateTime 	dtm1 = MutableDateTime.yearMonthDayHourMinuteSecond(2009,8,7,6,54,32);

        if (VERBOSE) {
            System.out.println("ImmutableDateTime.millis  = " + dti1.millis());
            System.out.println("ImmutableDateTime = " + dti1.toString());
            System.out.println("MutableDateTime   = " + dtm1.toString());
        }
        
        assertTrue("ImmutableDateTime.year()", dti1.year() == 2009);
        assertTrue("ImmutableDateTime.month()", dti1.month() == 8);
        assertTrue("ImmutableDateTime.day() ", dti1.day() == 7);
        assertTrue("ImmutableDateTime.hours() ", dti1.hours() == 6);
        assertTrue("ImmutableDateTime.minutes() ", dti1.minutes() == 54);
        assertTrue("ImmutableDateTime.seconds() ", dti1.seconds() == 32);
        assertTrue("ImmutableDateTime.millis() ", dti1.millis() == 0);
        
        assertTrue("MutableDateTime.year()", dtm1.year() == 2009);
        assertTrue("MutableDateTime.month()", dtm1.month() == 8);
        assertTrue("MutableDateTime.day() ", dtm1.day() == 7);
        assertTrue("MutableDateTime.hours() ", dtm1.hours() == 6);
        assertTrue("MutableDateTime.minutes() ", dtm1.minutes() == 54);
        assertTrue("MutableDateTime.seconds() ", dtm1.seconds() == 32);
        assertTrue("MutableDateTime.millis() ", dtm1.millis() == 0);
    }
    
    @Test
    public void testNow() {
        if (VERBOSE) System.out.println("testNow");

        ImmutableDateTime 	dti1 = ImmutableDateTime.now();
        MutableDateTime 	dtm1 = MutableDateTime.now();

        if (VERBOSE) {
            System.out.println("ImmutableDateTime = " + dti1.toString());
            System.out.println("MutableDateTime   = " + dtm1.toString());
        }
        
        assertTrue("MutableDateTime.year() ", dtm1.year() == dti1.year());
        assertTrue("MutableDateTime.month() ", dtm1.month() == dti1.month());
        assertTrue("MutableDateTime.day() ", dtm1.day() == dti1.day());
        assertTrue("MutableDateTime.hours() ", dtm1.hours() == dti1.hours());
        assertTrue("MutableDateTime.minutes() ", dtm1.minutes() == dti1.minutes());
        assertTrue("MutableDateTime.seconds() ", Math.abs(dtm1.seconds() - dti1.seconds()) < 1);
        assertTrue("MutableDateTime.millis() ", Math.abs(dtm1.millis() - dti1.millis()) < 10);
    }
    
    
    
    
 

}
