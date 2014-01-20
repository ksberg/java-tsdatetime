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

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
//import static org.testng.Assert.assertEquals;

public class Test4DateNames {

    public static final boolean VERBOSE = true;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    @Test
    public void testDefaultNames() {
        DateTime d = new ImmutableDateTime(2004,5,6);
        String s = d.toString();

        System.out.println("Date = " + d.toString());

    }


    @Ignore
    public void testCustomNames() {
        if (VERBOSE) System.out.println("testCustomNames");

        DaysAndMonths daysAndMonthsMock = mock(DaysAndMonths.class);
        when(daysAndMonthsMock.dayName(0)).thenReturn("Monday");
        when(daysAndMonthsMock.dayName(1)).thenReturn("Tuesday");


        System.out.println(daysAndMonthsMock.dayName(0));
        System.out.println(daysAndMonthsMock.dayName(1));

        ImmutableDateTime dBefore1 = new ImmutableDateTime(2003,4,15);
        MutableDateTime dBefore2 = new MutableDateTime(2003,4,15);

        assertEquals("Mutable weekOfYear() == Immutable weekOfYear()", dBefore1.weekOfYear(),dBefore2.weekOfYear());
        int before = dBefore1.weekOfYear();
        DateTime dAfter1 = dBefore1.addWeeks(1);
        DateTime dAfter2 = dBefore2.addWeeks(1);
        int after = dAfter1.weekOfYear();
        assertTrue("Sequential Weeks", after == before + 1);

        assertTrue("Immutable weekOfYear() Unchanged after addWeeks()",   dBefore1.weekOfYear() != dBefore2.weekOfYear());
        assertTrue("Immutable weekOfYear() == mutable.weekOfYear()",    dAfter1.weekOfYear() == dAfter2.weekOfYear());
        assertNotSame("Expect different",dBefore1, dAfter1);
        assertEquals("Expect same object",dBefore2, dAfter2);
    }

}
