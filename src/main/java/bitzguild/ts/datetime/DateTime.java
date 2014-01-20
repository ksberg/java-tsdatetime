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


import java.util.Date;

/**
 * <p>
 * The DateTime interface is the central abstraction for BitzGuild DateTime.
 * </p>
 * <p>
 * Class for data and operations on gregorian calendar date. This is a
 * specialized replacement for <code>java.util.Date</code> which is motivated
 * by the following:
 * </p>
 * <ol>
 *     <li>Support a broad range of dates extending at least from 1800 and forward well beyond 2000</li>
 *     <li>Provide a compact storage for reading and writing using one or more primitive types</li>
 *     <li>Provide an equivalent non-object representation to save on object overhead</li>
 *     <li>Provide efficient conversion between object and non-object forms</li>
 *     <li>Compare two non-object representations and get same result as comparing objects</li>
 *     <li>Support extensive date arithmetic manipulations and queries</li>
 *     <li>Support notion of business days with related manipulation and queries</li>
 *     <li>Support mutable and non-mutable instances</li>
 *     <li>Perform well in real-time environments</li>
 * </ol>
 * <p>
 * The non-object requirement is motivated by the need to support millions of
 * datetimes in a time series (e.g. stock quotes, tick data, time event data).
 * Having an immutable, primitive type representation that can be filtered or
 * compared to other references saves GC time and overhead and lends itself
 * to use in real-time situations.
 * </p>
 * <ul>
 *     <li>There is no planned support for time zones</li>
 *     <li>There is minimal support for localization</li>
 *     <li>Does not support missing 10 days in 1582 (slows normal cases, handle in subclass)</li>
 *     <li>Does not support switch from 100/400 leap year rule prior to 1600 (special case)</li>
 * </ul>
 *
 * @see bitzguild.ts.datetime.MutableDateTime
 * @see bitzguild.ts.datetime.ImmutableDateTime
 * @see DateTimePredicate
 *
 * @author Kevin Sven Berg
 */

public interface DateTime extends DateTimeNames {

    // unit definitions
    public static final int MILLISECOND = 0;
    public static final int SECOND = 1;
    public static final int MINUTE = 2;
    public static final int HOUR = 3;
    public static final int DAY = 4;
    public static final int WEEK = 5;
    public static final int MONTH = 6;
    public static final int QUARTER = 7;
    public static final int YEAR = 8;

    public static final int MillisInSecond  = 1000;
    public static final int MillisInMinute  = 60 * MillisInSecond;
    public static final int MillisInHour    = 60 * MillisInMinute;
    public static final int MillisInDay     = 24 * MillisInHour;
    public static final int MillisInWeek    =  7 * MillisInDay;

    public static final int[] MilliFactors = { 1, MillisInSecond, MillisInMinute, MillisInHour, MillisInDay, MillisInWeek };


    
    /**
     * Answer the predicate function which evaluates if given 
     * DateTime is a holiday.
     * 
     * @return DateTimePredicate
     */
	public DateTimePredicate holidays();
    
    /**
     * Answer long representation of given date time, a compact form
     * that can be used for storage.
     * Note that this DOES NOT equal milliseconds from standard epoch.
     *
     * @return long
     */
    public long rep();
    
    /**
     * Integer representation
     *
     * @return int
     */
    public int intRep();


    // ------------------------------------------------------------------------------
    // DateTime Values
    // ------------------------------------------------------------------------------


    /**
     * Answer Gregorian Year as integer (e.g. 1947)
     *
     * @return int year
     */
    public int year();


    /**
     * Answer month as integer (1..12)
     *
     * @return int month
     */
    public int month();

    /**
     * Answer day as integer (1..31)
     *
     * @return int day
     */
    public int day();

    /**
     * Answer hour as integer (0..23)
     *
     * @return int hour
     */
    public int hours();

    /**
     * Answer minutes as integer (0..59)
     *
     * @return int minutes between hours
     */
    public int minutes();


    /**
     * Answer seconds as integer (0..59)
     *
     * @return int seconds
     */
    public int seconds();


    /**
     * Answer milliseconds as integer (0..999)
     *
     * @return int millis between seconds
     */
    public int millis();


    /**
     * Answer total minutes since midnight of current day
     *
     * @return int minutes
     */
    public int minutesSinceMidnight();


    /**
     * Answer total milliseconds since midnight of current day
     *
     * @return int
     */
    public int millisecondsSinceMidnight();


    /**
     * Answer day into year, where January 1st == 1 (e.g. 1..365)
     *
     * @return int
     */
    public int dayOfYear();


    /**
     * Answer day of week (Monday through Sunday is 0..6)
     *
     * @return int
     */
    public int dayOfWeek();


    /**
     * Answer sequential business day of month starting at 1.
     * This answer depends on holiday configuration.
     *
     * @return int
     */
    public int businessDayOfMonth();


    /**
     * Answer days in year (365 or 366 depending on leap year)
     *
     * @return int
     */
    public int numberOfDaysInYear();


    /**
     * Answer remainder of days left in year from current date.
     *
     * @return int
     */
    public int daysLeftOfYear();


    /**
     * Answer week count into current month, starting at 1.
     *
     * @return int
     */
    public int weekOfMonth();


    /**
     * Answer week count into current year, starting at 1.
     *
     * @return int
     */
    public int weekOfYear();


    // ------------------------------------------------------------------------------
    // Value Modifiers
    // ------------------------------------------------------------------------------



    public DateTime addMillis(int amount);

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addSeconds(int amount);

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addMinutes(int amount);

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addHours(int amount);


    /**
     *
     * @param numDays
     * @return
     */
    public DateTime addDays(int numDays);


    /**
     *
     * @param numDays
     * @return
     */
    public DateTime addBusinessDays(int numDays);


    /**
     *
     * @param numWeeks
     * @return
     */
    public DateTime addWeeks(int numWeeks);


    /**
     *
     * @param numberOfYears
     * @return
     */
    public DateTime addYears(int numberOfYears);


    /**
     *
     * @param dayOfWeek
     * @return
     */
    public DateTime rollbackToDayOfWeek(int dayOfWeek);


    /**
     *
     * @param num
     * @return
     */
    public DateTime rollMonths(int num);


    /**
     *
     * @param dayOfWeek
     * @return
     */
    public DateTime rollToDayOfWeek(int dayOfWeek);


    /**
     *
     * @return
     */
    public DateTime nextBusinessDay();


    /**
     *
     * @return
     */
    public DateTime nextWeekday();


    /**
     *
     * @return
     */
    public DateTime nextWeek();


    /**
     *
     * @return
     */
    public DateTime nextMonth();


    /**
     *
     * @return
     */
    public DateTime nextQuarter();


    /**
     *
     * @return
     */
    public DateTime nextYear();


    /**
     *
     * @param numYears
     * @return
     */
    public DateTime rollYears(int numYears);


    /**
     *
     * @return
     */
    public DateTime priorWeekday();


    /**
     *
     * @return
     */
    public DateTime priorBusinessDay();


    /**
     *
     * @param other
     * @return
     */
    public double yearsTo(DateTime other);


    /**
     *
     * @return
     */
    public Date toJavaDate();


}
