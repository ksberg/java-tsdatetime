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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Performance Benchmark: MutableDateTime vs Joda ImmutableDateTimeScrap vs Java Date
 *
 * Examines operations that are commonplace in managing large sets of datetime data
 * as related to time-series information; namely conversion to and from non-object
 * format, formatting, access, filtering, and incrementing.
 *
 */
public class Perf4DateTime {

    public Perf4DateTime() {}

	public static void main(String[] args) {
        Perf4DateTime prog = new Perf4DateTime();
        prog.setUp();
        prog.testPerformance();;
        prog.tearDown();
	}

	protected ArrayList<String> testName;
	protected ArrayList<Long>   timeBizGuild;
	protected ArrayList<Long>   timeJoda;
	protected ArrayList<Long>   timeJava;
	protected ArrayList<Double> hiloFactor;

    protected ColumnHelper      column4TestName;
    protected ColumnHelper      column4BitzGuild;
    protected ColumnHelper      column4JavaDate;
    protected ColumnHelper      column4JodaDate;
    protected ColumnHelper      column4Factor;
	
	/**
	 *
	 */
	public void setUp() {
        column4TestName = new ColumnHelper("Test", 20);

		testName = new ArrayList<String>();
		timeBizGuild = new ArrayList<Long>();
		timeJoda = new ArrayList<Long>();
		timeJava = new ArrayList<Long>();
		hiloFactor = new ArrayList<Double>();

        column4BitzGuild = new ColumnHelper("BitzGuild", 12);
        column4JodaDate = new ColumnHelper("Joda", 12);
        column4JavaDate = new ColumnHelper("Java", 12);
        column4Factor = new ColumnHelper("Factor",14);
	}
	
	/**
	 * Clean up the test.
	 */
	protected void tearDown() {

		testName.add("Total");

		long sum = 0L;
		int size = timeBizGuild.size();
        int last = size-1;

		sum = 0L; for(int i=0;i<size;i++) sum += timeBizGuild.get(i); timeBizGuild.add(sum);
		sum = 0L; for(int i=0;i<size;i++) sum += timeJoda.get(i); timeJoda.add(sum);
		sum = 0L; for(int i=0;i<size;i++) sum += timeJava.get(i); timeJava.add(sum);

        double min = (double)Math.min(timeBizGuild.get(last), Math.min(timeJoda.get(last), timeJava.get(last)));
        double max = (double)Math.max(timeBizGuild.get(last), Math.max(timeJoda.get(last), timeJava.get(last)));
        hiloFactor.add(max/min);

		System.out.println();
		System.out.println("BENCHMARK RESULTS");
		System.out.println();

        StringBuffer strb = new StringBuffer();

        column4TestName.renderLabel(strb);
        column4BitzGuild.renderLabel(strb);
        column4JodaDate.renderLabel(strb);
        column4JavaDate.renderLabel(strb);
        column4Factor.renderLabel(strb);
        System.out.println(strb.toString());
        System.out.println();

        for(int i=0;i<size;i++) {
            strb.setLength(0);

            column4TestName.renderString(strb, testName.get(i));
            column4BitzGuild.renderLong(strb, timeBizGuild.get(i));
            column4JodaDate.renderLong(strb, timeJoda.get(i));
            column4JavaDate.renderLong(strb, timeJava.get(i));
            column4Factor.renderDouble(strb, hiloFactor.get(i));
            System.out.println(strb.toString());
        }
        System.out.println();
	}
	
	public void testPerformance() {
		this.testCreation();
        this.testPrint();
        this.testParse();
//		this.testToAndFromString();
		this.testObjToSerial();
		this.testDateComparison();
        this.testYearMonthDay();
		this.testTimeIteration();
		this.testDayIteration();
        this.testYearIteration();
        this.testBizDayIteration();
	}


    public void testPrint() {

        long nsA, nsZ;
        int iterations = 1000000;

        MutableDateTime bzgDT = MutableDateTime.yearMonthDay(2000, 1, 1);
        bzgDT.setHoursMinutesSecondsMillis(9, 4, 56, 123);

        org.joda.time.DateTime jodaDT = org.joda.time.DateTime.parse("2000-01-01T09:04:56.123"); // LocalDateTime.parse("2013-03-30T09:04:56.123");

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2000);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.set(Calendar.HOUR, 9);
        myCal.set(Calendar.MINUTE, 4);
        myCal.set(Calendar.SECOND, 56);
        myCal.set(Calendar.MILLISECOND, 123);
        Date javaDT = myCal.getTime();

        nsA = System.nanoTime();
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<iterations; i++) {
            bzgDT.toBuffer(sb).append("\n");
        }
        nsZ = System.nanoTime();
        long nsIterPsc = nsZ - nsA;

        nsA = System.nanoTime();
        for(int i=0; i<iterations; i++) {
            String str = jodaDT.toString();
        }
        nsZ = System.nanoTime();
        long nsIterJoda = nsZ - nsA;

        nsA = System.nanoTime();
        Date tmpDate = javaDT;
        for(int i=0; i<iterations; i++) {
            String str = javaDT.toString();
        }
        javaDT = tmpDate;
        nsZ = System.nanoTime();
        long nsIterJava = nsZ - nsA;

        double min = (double)Math.min(nsIterPsc, Math.min(nsIterJoda, nsIterJava));
        double max = (double)Math.max(nsIterPsc, Math.max(nsIterJoda, nsIterJava));
        testName.add("Format");
        timeBizGuild.add(nsIterPsc / 1000000L);
        timeJoda.add(nsIterJoda / 1000000L);
        timeJava.add(nsIterJava / 1000000L);
        hiloFactor.add(max/min);
    }


    public void testParse() {

        long nsA, nsZ;
        int iterations = 1000000;

        MutableDateTime bzgDT = MutableDateTime.yearMonthDay(2000, 1, 1);
        bzgDT.setHoursMinutesSecondsMillis(9, 4, 56, 123);

        org.joda.time.DateTime jodaDT = org.joda.time.DateTime.parse("2000-01-01T09:04:56.123"); // LocalDateTime.parse("2013-03-30T09:04:56.123");

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2000);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.set(Calendar.HOUR, 9);
        myCal.set(Calendar.MINUTE, 4);
        myCal.set(Calendar.SECOND, 56);
        myCal.set(Calendar.MILLISECOND, 123);
        Date javaDT = myCal.getTime();

        String str = null;

        str = bzgDT.toString();
        nsA = System.nanoTime();
        for(int i=0; i<iterations; i++) {
            try {
                MutableDateTime pdt = MutableDateTime.parse(str);
            } catch (Exception e) {
            }
        }
        nsZ = System.nanoTime();
        long nsIterPsc = nsZ - nsA;

        str = jodaDT.toString();
        nsA = System.nanoTime();
        for(int i=0; i<iterations; i++) {
            @SuppressWarnings("unused")
            org.joda.time.DateTime jdt = org.joda.time.DateTime.parse(str);
        }
        nsZ = System.nanoTime();
        long nsIterJoda = nsZ - nsA;

        str = javaDT.toString();
        nsA = System.nanoTime();
        Date tmpDate = javaDT;
        SimpleDateFormat parserSDF=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        for(int i=0; i<iterations; i++) {
            try {
                @SuppressWarnings("unused")
                Date jd = parserSDF.parse(str);
            } catch (Exception e) {
            }
        }
        javaDT = tmpDate;
        nsZ = System.nanoTime();
        long nsIterJava = nsZ - nsA;

        double min = (double)Math.min(nsIterPsc, Math.min(nsIterJoda, nsIterJava));
        double max = (double)Math.max(nsIterPsc, Math.max(nsIterJoda, nsIterJava));
        testName.add("Parse");
        timeBizGuild.add(nsIterPsc / 1000000L);
        timeJoda.add(nsIterJoda / 1000000L);
        timeJava.add(nsIterJava / 1000000L);
        hiloFactor.add(max/min);
    }

    public void testToAndFromString() {

        long nsA, nsZ;
        int iterations = 1000000;

        MutableDateTime bzgDT = MutableDateTime.yearMonthDay(2000, 1, 1);
        bzgDT.setHoursMinutesSecondsMillis(9, 4, 56, 123);

        org.joda.time.DateTime jodaDT = org.joda.time.DateTime.parse("2000-01-01T09:04:56.123"); // LocalDateTime.parse("2013-03-30T09:04:56.123");

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2000);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.set(Calendar.HOUR, 9);
        myCal.set(Calendar.MINUTE, 4);
        myCal.set(Calendar.SECOND, 56);
        myCal.set(Calendar.MILLISECOND, 123);
        Date javaDT = myCal.getTime();

        nsA = System.nanoTime();
        for(int i=0; i<iterations; i++) {
            String str = bzgDT.toString();
            try {
                MutableDateTime pdt = MutableDateTime.parse(str);
            } catch (Exception e) {
            }
        }
        nsZ = System.nanoTime();
        long nsIterPsc = nsZ - nsA;

        nsA = System.nanoTime();
        for(int i=0; i<iterations; i++) {
            String str = jodaDT.toString();
            @SuppressWarnings("unused")
            org.joda.time.DateTime jdt = org.joda.time.DateTime.parse(str);
        }
        nsZ = System.nanoTime();
        long nsIterJoda = nsZ - nsA;

        nsA = System.nanoTime();
        Date tmpDate = javaDT;
        SimpleDateFormat parserSDF=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        for(int i=0; i<iterations; i++) {
            try {
                String str = javaDT.toString();
                @SuppressWarnings("unused")
				Date jd = parserSDF.parse(str);
            } catch (Exception e) {
            }
        }
        javaDT = tmpDate;
        nsZ = System.nanoTime();
        long nsIterJava = nsZ - nsA;

        double min = (double)Math.min(nsIterPsc, Math.min(nsIterJoda, nsIterJava));
        double max = (double)Math.max(nsIterPsc, Math.max(nsIterJoda, nsIterJava));
        testName.add("String/Parse");
        timeBizGuild.add(nsIterPsc / 1000000L);
        timeJoda.add(nsIterJoda / 1000000L);
        timeJava.add(nsIterJava / 1000000L);
        hiloFactor.add(max/min);
    }

    public void testDateComparison() {

        long nsA, nsZ;
        int iterations = 1000000;

        MutableDateTime bzgDT1 = MutableDateTime.yearMonthDay(2000, 1, 1);
        MutableDateTime bzgDT2 = MutableDateTime.yearMonthDay(2000, 1, 2);

        org.joda.time.DateTime jodaDT1 = org.joda.time.DateTime.parse("2000-01-01");
        org.joda.time.DateTime jodaDT2 = org.joda.time.DateTime.parse("2000-01-02");

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2000);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.set(Calendar.HOUR, 0);
        myCal.set(Calendar.MINUTE, 0);
        myCal.set(Calendar.SECOND, 0);
        Date javaDT1 = myCal.getTime();

        myCal.set(Calendar.DAY_OF_MONTH, 2);
        Date javaDT2 = myCal.getTime();

        nsA = System.nanoTime();
        for(int i=0; i<iterations; i++) {
            bzgDT1.compareTo(bzgDT2);
        }
        nsZ = System.nanoTime();
        long nsIterPsc = nsZ - nsA;


        nsA = System.nanoTime();
        for(int i=0; i<iterations; i++) {
            jodaDT1.compareTo(jodaDT2);
        }
        nsZ = System.nanoTime();
        long nsIterJoda = nsZ - nsA;

        nsA = System.nanoTime();
        for(int i=0; i<iterations; i++) {
            javaDT1.compareTo(javaDT2);
        }
        nsZ = System.nanoTime();
        long nsIterJava = nsZ - nsA;

        double min = (double)Math.min(nsIterPsc, Math.min(nsIterJoda, nsIterJava));
        double max = (double)Math.max(nsIterPsc, Math.max(nsIterJoda, nsIterJava));
        testName.add("Date Comparison");
        timeBizGuild.add(nsIterPsc / 1000000L);
        timeJoda.add(nsIterJoda / 1000000L);
        timeJava.add(nsIterJava / 1000000L);
        hiloFactor.add(max/min);
    }

	public void testObjToSerial() {

		long nsA, nsZ;
		int iterations = 1000000;
		
		MutableDateTime pscDT = MutableDateTime.yearMonthDay(2000, 1, 1);
		org.joda.time.DateTime jodaDT = org.joda.time.DateTime.parse("2000-01-01"); // LocalDateTime.parse("2013-03-30T09:04:56.123");

		Calendar myCal = Calendar.getInstance();
		myCal.set(Calendar.YEAR, 2000);
		myCal.set(Calendar.MONTH, 0);
		myCal.set(Calendar.DAY_OF_MONTH, 1);
		myCal.set(Calendar.HOUR, 0);
		myCal.set(Calendar.MINUTE, 0);
		myCal.set(Calendar.SECOND, 0);
		
		Date javaDT = myCal.getTime();
		
		nsA = System.nanoTime();
		for(int i=0; i<iterations; i++) {
			long t = pscDT.rep();
            @SuppressWarnings("unused")
            MutableDateTime pdt = new MutableDateTime(t);
		}
		nsZ = System.nanoTime();
		long nsIterPsc = nsZ - nsA;
		

		nsA = System.nanoTime();
		for(int i=0; i<iterations; i++) {
			long t = jodaDT.getMillis();
            @SuppressWarnings("unused")
            org.joda.time.DateTime jdt = new org.joda.time.DateTime(t);
		}
		nsZ = System.nanoTime();
		long nsIterJoda = nsZ - nsA;
		
		nsA = System.nanoTime();
		Date tmpDate = javaDT;
		for(int i=0; i<iterations; i++) {
			long t = javaDT.getTime();
            @SuppressWarnings("unused")
			Date jd = new Date(t);
		}
		javaDT = tmpDate;
		nsZ = System.nanoTime();
		long nsIterJava = nsZ - nsA;
		
		double min = (double)Math.min(nsIterPsc, Math.min(nsIterJoda, nsIterJava));
		double max = (double)Math.max(nsIterPsc, Math.max(nsIterJoda, nsIterJava));
		testName.add("Serial");
		timeBizGuild.add(nsIterPsc / 1000000L);
		timeJoda.add(nsIterJoda / 1000000L);
		timeJava.add(nsIterJava / 1000000L);
		hiloFactor.add(max/min);
	}
	
	public void testCreation() {

		long nsA, nsZ;
		int iterations = 1000000;
		
		nsA = System.nanoTime();
		for(int i=0; i<iterations; i++) {
            @SuppressWarnings("unused")
            MutableDateTime pdt = MutableDateTime.yearMonthDay(2000, 1, 1);
		}
		nsZ = System.nanoTime();
		long nsIterPsc = nsZ - nsA;
		

		nsA = System.nanoTime();
		for(int i=0; i<iterations; i++) {
            @SuppressWarnings("unused")
            org.joda.time.DateTime jdt = new org.joda.time.DateTime(2000,1,1,0,0,0,0);
		}
		nsZ = System.nanoTime();
		long nsIterJoda = nsZ - nsA;
		
		nsA = System.nanoTime();
		for(int i=0; i<iterations; i++) {
			Calendar myCal = Calendar.getInstance();
			myCal.set(Calendar.YEAR, 2000);
			myCal.set(Calendar.MONTH, 0);
			myCal.set(Calendar.DAY_OF_MONTH, 1);
			myCal.set(Calendar.HOUR, 0);
			myCal.set(Calendar.MINUTE, 0);
			myCal.set(Calendar.SECOND, 0);
            @SuppressWarnings("unused")
			Date javaDT = myCal.getTime();
		}
		nsZ = System.nanoTime();
		long nsIterJava = nsZ - nsA;
		
		double min = (double)Math.min(nsIterPsc, Math.min(nsIterJoda, nsIterJava));
		double max = (double)Math.max(nsIterPsc, Math.max(nsIterJoda, nsIterJava));
		testName.add("Creation");
		timeBizGuild.add(nsIterPsc / 1000000L);
		timeJoda.add(nsIterJoda / 1000000L);
		timeJava.add(nsIterJava / 1000000L);
		hiloFactor.add(max/min);
		
	}

    public void testTimeIteration() {

        long nsA, nsZ;
        int iterations = 1000000;

        MutableDateTime pscDT = MutableDateTime.yearMonthDay(2000, 1, 1);
        org.joda.time.DateTime jodaDT = org.joda.time.DateTime.parse("2000-01-01"); // LocalDateTime.parse("2013-03-30T09:04:56.123");

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2000);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.set(Calendar.HOUR, 0);
        myCal.set(Calendar.MINUTE, 0);
        myCal.set(Calendar.SECOND, 0);

        Date javaDT = myCal.getTime();

        final long ONE_MINUTE_IN_MILLIS=60000;	//millisecs

        nsA = System.nanoTime();
        for(int i=0; i<iterations; i++) {
            pscDT.addMinutes(1);
        }
        nsZ = System.nanoTime();
        long nsIterPsc = nsZ - nsA;


        nsA = System.nanoTime();
        org.joda.time.DateTime tmpDateTime = jodaDT;
        for(int i=0; i<iterations; i++) {
            tmpDateTime = tmpDateTime.plusMinutes(1);
        }
        jodaDT = tmpDateTime;
        nsZ = System.nanoTime();
        long nsIterJoda = nsZ - nsA;

        nsA = System.nanoTime();
        Date tmpDate = javaDT;
        for(int i=0; i<iterations; i++) {
            long t = tmpDate.getTime();
            tmpDate = new Date(t + ONE_MINUTE_IN_MILLIS);
        }
        javaDT = tmpDate;
        nsZ = System.nanoTime();
        long nsIterJava = nsZ - nsA;

        double min = (double)Math.min(nsIterPsc, Math.min(nsIterJoda, nsIterJava));
        double max = (double)Math.max(nsIterPsc, Math.max(nsIterJoda, nsIterJava));
        testName.add("Iterate by Minute");
        timeBizGuild.add(nsIterPsc / 1000000L);
        timeJoda.add(nsIterJoda / 1000000L);
        timeJava.add(nsIterJava / 1000000L);
        hiloFactor.add(max/min);
    }

    public void testDayIteration() {

        long nsA, nsZ;
        int iterations = 1000000;

        MutableDateTime pscDT = MutableDateTime.yearMonthDay(2000, 1, 1);
        org.joda.time.DateTime jodaDT = new org.joda.time.DateTime(2000,1,1,0,0,0,0);

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2000);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.set(Calendar.HOUR, 0);
        myCal.set(Calendar.MINUTE, 0);
        myCal.set(Calendar.SECOND, 0);

        Date javaDT = myCal.getTime();

        final long ONE_MINUTE_IN_MILLIS=60000;	//millisecs
        final long ONE_DAY_IN_MILLIS = ONE_MINUTE_IN_MILLIS * 60 * 24;

        nsA = System.nanoTime();
        for(int i=0; i<iterations; i++) {
            pscDT.addDays(1);
        }
        nsZ = System.nanoTime();
        long nsIterPsc = nsZ - nsA;


        nsA = System.nanoTime();
        org.joda.time.DateTime tmpDateTime = jodaDT;
        for(int i=0; i<iterations; i++) {
            tmpDateTime = tmpDateTime.plusDays(1);
        }
        jodaDT = tmpDateTime;
        nsZ = System.nanoTime();
        long nsIterJoda = nsZ - nsA;

        nsA = System.nanoTime();
        Date tmpDate = javaDT;
        for(int i=0; i<iterations; i++) {
            long t = tmpDate.getTime();
            tmpDate = new Date(t + ONE_DAY_IN_MILLIS);
        }
        javaDT = tmpDate;
        nsZ = System.nanoTime();
        long nsIterJava = nsZ - nsA;


        double min = (double)Math.min(nsIterPsc, Math.min(nsIterJoda, nsIterJava));
        double max = (double)Math.max(nsIterPsc, Math.max(nsIterJoda, nsIterJava));
        testName.add("Iterate by Day");
        timeBizGuild.add(nsIterPsc / 1000000L);
        timeJoda.add(nsIterJoda / 1000000L);
        timeJava.add(nsIterJava / 1000000L);
        hiloFactor.add(max / min);
    }

    public void testYearIteration() {

        long nsA, nsZ;
        int iterations = 1000000;

        MutableDateTime pscDT = MutableDateTime.yearMonthDay(2000, 1, 1);
        org.joda.time.DateTime jodaDT = new org.joda.time.DateTime(2000,1,1,0,0,0,0);

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2000);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.set(Calendar.HOUR, 0);
        myCal.set(Calendar.MINUTE, 0);
        myCal.set(Calendar.SECOND, 0);
        myCal.set(Calendar.MILLISECOND, 0);

        Date javaDT = myCal.getTime();

        nsA = System.nanoTime();
        @SuppressWarnings("unused")
        DateTime tmpLDateTime;
        for(int i=0; i<iterations; i++) {
            tmpLDateTime = pscDT.addYears(1);
        }
        nsZ = System.nanoTime();
        long nsIterPsc = nsZ - nsA;


        nsA = System.nanoTime();
        org.joda.time.DateTime tmpDateTime = jodaDT;
        for(int i=0; i<iterations; i++) {
            tmpDateTime = tmpDateTime.plusYears(1);
        }
        jodaDT = tmpDateTime;
        nsZ = System.nanoTime();
        long nsIterJoda = nsZ - nsA;

        java.util.GregorianCalendar gc = new java.util.GregorianCalendar(1999, 11, 31);
        nsA = System.nanoTime();
        Date tmpDate = javaDT;
        for(int i=0; i<iterations; i++) {
            gc.roll(java.util.Calendar.YEAR, 1);
            tmpDate = gc.getTime();
        }
        javaDT = tmpDate;
        nsZ = System.nanoTime();
        long nsIterJava = nsZ - nsA;


        double min = (double)Math.min(nsIterPsc, Math.min(nsIterJoda, nsIterJava));
        double max = (double)Math.max(nsIterPsc, Math.max(nsIterJoda, nsIterJava));
        testName.add("Iterate by Year");
        timeBizGuild.add(nsIterPsc / 1000000L);
        timeJoda.add(nsIterJoda / 1000000L);
        timeJava.add(nsIterJava / 1000000L);
        hiloFactor.add(max / min);
    }

    public void testBizDayIteration() {

        long nsA, nsZ;
        int iterations = 1000000;

        MutableDateTime pscDT = MutableDateTime.yearMonthDay(2000, 1, 1);

        org.joda.time.DateTime jodaDT = org.joda.time.DateTime.parse("2000-01-01");
        // LocalDateTime dt1 = LocalDateTime.parse("2013-03-30T09:04:56.123");

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2000);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.set(Calendar.HOUR, 0);
        myCal.set(Calendar.MINUTE, 0);
        myCal.set(Calendar.SECOND, 0);

        Date javaDT = myCal.getTime();

        final long ONE_MINUTE_IN_MILLIS=60000;	//millisecs
        final long ONE_DAY_IN_MILLIS = ONE_MINUTE_IN_MILLIS * 60 * 24;

        nsA = System.nanoTime();
        for(int i=0; i<iterations; i++) {
            pscDT.addBusinessDays(1, MutableDateTime.DefaultHolidays);
        }
        nsZ = System.nanoTime();
        long nsIterPsc = nsZ - nsA;


        nsA = System.nanoTime();
        org.joda.time.DateTime tmpDateTime = jodaDT;
        for(int i=0; i<iterations; i++) {
            tmpDateTime = tmpDateTime.plusDays(1);
            int dow = tmpDateTime.getDayOfWeek();
            if (dow > 5) tmpDateTime = tmpDateTime.plusDays(7-dow);
        }
        jodaDT = tmpDateTime;
        nsZ = System.nanoTime();
        long nsIterJoda = nsZ - nsA;

        nsA = System.nanoTime();
        Date tmpDate = javaDT;
        for(int i=0; i<iterations; i++) {
            long t = tmpDate.getTime();
            tmpDate = new Date(t + ONE_DAY_IN_MILLIS);
            Calendar c = Calendar.getInstance();
            c.setTime(tmpDate);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek > 5) tmpDate = new Date(t + (7-dayOfWeek)*ONE_DAY_IN_MILLIS);
        }
        javaDT = tmpDate;
        nsZ = System.nanoTime();
        long nsIterJava = nsZ - nsA;


        double min = (double)Math.min(nsIterPsc, Math.min(nsIterJoda, nsIterJava));
        double max = (double)Math.max(nsIterPsc, Math.max(nsIterJoda, nsIterJava));
        testName.add("Iterate by Biz Day");
        timeBizGuild.add(nsIterPsc / 1000000L);
        timeJoda.add(nsIterJoda / 1000000L);
        timeJava.add(nsIterJava / 1000000L);
        hiloFactor.add(max / min);
    }


    public void testYearMonthDay() {

        long nsA, nsZ;
        int iterations = 1000000;

        MutableDateTime bzgDT = MutableDateTime.yearMonthDay(2000, 1, 1);
        org.joda.time.DateTime jodaDT = new org.joda.time.DateTime(2000,1,1,0,0,0,0);

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2000);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.set(Calendar.HOUR, 0);
        myCal.set(Calendar.MINUTE, 0);
        myCal.set(Calendar.SECOND, 0);
        myCal.set(Calendar.MILLISECOND, 0);

        Date javaDT = myCal.getTime();

        nsA = System.nanoTime();
        @SuppressWarnings("unused")
        DateTime tmpLDateTime;
        for(int i=0; i<iterations; i++) {
            int year = bzgDT.year();
            int month = bzgDT.month();
            int day = bzgDT.day();
        }
        nsZ = System.nanoTime();
        long nsIterPsc = nsZ - nsA;


        nsA = System.nanoTime();
        org.joda.time.DateTime tmpDateTime = jodaDT;
        for(int i=0; i<iterations; i++) {
            org.joda.time.DateTime.Property yp = jodaDT.year();
            org.joda.time.DateTime.Property mp = jodaDT.monthOfYear();
            org.joda.time.DateTime.Property dp = jodaDT.dayOfMonth();
            int year = yp.get();
            int month = mp.get();
            int day = dp.get();
        }
        jodaDT = tmpDateTime;
        nsZ = System.nanoTime();
        long nsIterJoda = nsZ - nsA;

        java.util.GregorianCalendar gc = new java.util.GregorianCalendar(1999, 11, 31);
        nsA = System.nanoTime();
        Date tmpDate = javaDT;
        for(int i=0; i<iterations; i++) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(javaDT);
            int iYear = calendar.get(java.util.Calendar.YEAR);
            int iMonth = calendar.get(java.util.Calendar.MONTH);
            int iDay = calendar.get(java.util.Calendar.DATE);
        }
        javaDT = tmpDate;
        nsZ = System.nanoTime();
        long nsIterJava = nsZ - nsA;


        double min = (double)Math.min(nsIterPsc, Math.min(nsIterJoda, nsIterJava));
        double max = (double)Math.max(nsIterPsc, Math.max(nsIterJoda, nsIterJava));
        testName.add("Get Year/Month/Day");
        timeBizGuild.add(nsIterPsc / 1000000L);
        timeJoda.add(nsIterJoda / 1000000L);
        timeJava.add(nsIterJava / 1000000L);
        hiloFactor.add(max / min);
    }
}