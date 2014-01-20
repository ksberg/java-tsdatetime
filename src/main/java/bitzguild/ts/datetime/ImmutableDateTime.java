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

import java.text.ParseException;

/**
 * <p>
 * The ImmutableDate class is designed for thread-safe operations that require
 * state and change isolation.
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
 * @see bitzguild.ts.datetime.AbstractDateTime
 * @see bitzguild.ts.datetime.MutableDateTime
 * @see bitzguild.ts.datetime.ImmutableDateTime
 * @see DateTimePredicate
 *
 * @author Kevin Sven Berg
 */
public class ImmutableDateTime extends AbstractDateTime implements java.io.Serializable {

	public static final long serialVersionUID = 1L;

	public static final boolean DEBUG = false;


	// --------------------------------------------
	// Attributes
	// --------------------------------------------



	// --------------------------------------------
	// Existence
	// --------------------------------------------

	/**
     * Default Constructor - not useful for Immutable Dates
	 */
	protected ImmutableDateTime() {
		super();
	}

    /**
     * Component Constructor
     *
     * @param year int
     * @param days int
     * @param holidays DateTimePredicate
     */
    protected ImmutableDateTime(int year, int days, DateTimePredicate holidays) {
        super(year,days,holidays);
    }


    /**
     * Copy constructor
     *
     * @param d ImmutableDateTime
     */
    public ImmutableDateTime(DateTime d) {
        super(d);
    }


	/**
	 * Construct a new date from the given serial representation. The
	 * serial representation is a compact integer form that is strictly
	 * increasing, like the Date object it represents, so that comparing
	 * any two representations yields the same result as comparing two 
	 * DateTime objects.
	 *
	 * @param rep compact date representation
	 */
	public ImmutableDateTime(long rep) {
		super(rep);
	}

	/**
	 * <p>
	 * Date constructor with an offset into the given year. If dayCount is greater
	 * that number of days in the given year, the constructor will wrap into
	 * the appropriate year. The specified year can be positive or negative.
	 * Negative yearsTo correspond to B.C. and work appropriately. Year may
	 * by any number between +4,194,303 and -4,194,304.
	 * </p>
	 *
     * @param yearInteger year (-4,194,303 .. +4,194,304)
     * @param dayCount number of days into given year, starting at zero
     */
	public ImmutableDateTime(int yearInteger, int dayCount) {
		super();
		_setYearAndDayCount(yearInteger, dayCount);
	}

	/**
	 * <p>
	 * A constructor for a common encountered format. The algorithm is not strict,
	 * and will take invalid month/_dayOfYear combinations. This is a convenience
	 * format for dayCount and Year.
	 * </p>
	 *
	 * @param theYear year (-4,194,303 .. +4,194,304)
	 * @param theMonth month (1..12)
	 * @param theDay _dayOfYear (1..31)
	 */
	public ImmutableDateTime(int theYear, int theMonth, int theDay) {
		super(theYear, theMonth, theDay);
	}

	/**
	 * <p>
	 * A constructor for a common encountered format. The algorithm is not strict,
	 * and will take invalid month/_dayOfYear combinations. This is a convenience
	 * format for dayCount and Year.
	 * </p>
	 *
	 * @param theYear year (-4,194,303 .. +4,194,304)
	 * @param theMonth month (1..12)
	 * @param theDay _dayOfYear (1..31)
	 */
	public ImmutableDateTime(int theYear, int theMonth, int theDay, int hours, int minutes, int seconds, int smillis) {
		super(theYear, theMonth, theDay);
		_setHoursMinutesSecondsMillis(hours, minutes, seconds, smillis);
	}


    /**
     * Create a new date set to today's day, month, year
     * with hour, minute, second, and milliseconds.
     *
     * @return ImmutableDateTime now
     */
    public static ImmutableDateTime now() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        ImmutableDateTime dt = ImmutableDateTime.yearMonthDay(
                cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH) + 1,
                cal.get(java.util.Calendar.DAY_OF_MONTH)
        );
        dt._time = (cal.get(java.util.Calendar.HOUR_OF_DAY) * MillisInHour)
                + (cal.get(java.util.Calendar.MINUTE) * MillisInMinute)
                + (cal.get(java.util.Calendar.SECOND) * MillisInSecond)
                + cal.get(java.util.Calendar.MILLISECOND);
        return dt;
    }

    /**
     * Answer new ImmutableDateTime from given year, month, and _dayOfYear (e.g. 2000, 1, 1)
     *
     * @param iyear integer year
     * @param monthIndex integer month (1..12)
     * @param dayIndex integer _dayOfYear (1..28+)
     * @return ImmutableDateTime
     */
    public static ImmutableDateTime yearMonthDay(int iyear, int monthIndex, int dayIndex) {
        return new ImmutableDateTime(iyear,monthIndex,dayIndex);
    }

    /**
     * Answer new ImmutableDateTime from given year, month, and day (e.g. 2000, 1, 1), 
     * with time as hour, minute, second (e.g. 2:30PM is 14,30,0). 
     *
     * @param iyear integer year
     * @param monthIndex integer month (1..12)
     * @param dayIndex integer day (1..28+)
     * @param hour (0..23)
     * @param minute (0..59)
     * @param second (0..59)
     * @return ImmutableDateTime
     */
    public static ImmutableDateTime yearMonthDayHourMinuteSecond(int iyear, int monthIndex, int dayIndex, int hour, int minute, int second) {
        return new ImmutableDateTime(iyear,monthIndex,dayIndex, hour, minute, second, 0);
    }

    // --------------------------------------------------------
    // Predicates
    // --------------------------------------------------------

    public boolean isToday() { return compareTo(now()) == 0; }


	// -----------------------------------------------------------
	// Accessor Methods
	// -----------------------------------------------------------



	// ------------------------------------------------------------------------------------
	// Instance Methods - ~Accessors
    // ------------------------------------------------------------------------------------



	/**
	 * <p>
	 * Answer the business day of the month.
	 * This counts consecutive business days
	 * since the start of the month.
	 * </p>
	 * @return int BDOM
	 */
	public int businessDayOfMonth() {
        MutableDateTime date = new MutableDateTime(this);

		int bizDOM = 0;
		int month = date.month();
		while(month == date.month()) {
			date.priorBusinessDay();
			bizDOM++;
		}
		return bizDOM;
	}

    /**
     * Answer the dayOfYear number in year (1 ... 366 max)
     *
     * @return int
     */
    public int dayOfYear() { return _dayOfYear; }


    /**
	 * Answer the number of days in year including this date
     *
	 * @return int days in year
	 */
	public int numberOfDaysInYear() {
		return daysInYear(_year);
	}

	/**
	 * Answer number of days left until new year
     *
	 * @return int number of days remaining in year
	 */
	public int daysLeftOfYear() {
		return numberOfDaysInYear() - _dayOfYear;
	}

    /**
     * Answer week count into current month, starting at 1.
     *
     * @return int
     */
    public int weekOfMonth() {
        return weekOfMonth(_dayOfYear, month());
    }



	// -----------------------------------------------------------
	// Immutable Modifiers - returns new modified object
	// -----------------------------------------------------------


    /**
     * Increment or decrement by the given milliseconds
     *
     * @param amount milliseconds
     * @return MutableDateTime
     */
    public DateTime addMillis(int amount) {
        return new ImmutableDateTime((new MutableDateTime(this)).addMillis(amount));
    }

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addSeconds(int amount) {
        return new ImmutableDateTime((new MutableDateTime(this)).addSeconds(amount));
    }

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addMinutes(int amount) {
        return new ImmutableDateTime((new MutableDateTime(this)).addMinutes(amount));
    }

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addHours(int amount) {
        return new ImmutableDateTime((new MutableDateTime(this)).addHours(amount));
    }


    public DateTime addDays(int numDays) {
        return new ImmutableDateTime(((new MutableDateTime(this)).addDays(numDays)).intRep());
    }

    public DateTime addBusinessDays(int numDays) {
        return new ImmutableDateTime(((new MutableDateTime(this)).addBusinessDays(numDays)).intRep());
    }

    public DateTime addWeeks(int numWeeks) {
        return new ImmutableDateTime(((new MutableDateTime(this)).addWeeks(numWeeks)).intRep());
    }

    public DateTime addYears(int numberOfYears) {
        return new ImmutableDateTime(((new MutableDateTime(this)).addYears(numberOfYears)).intRep());
    }

    public DateTime rollbackToDayOfWeek(int dayOfWeek) {
        return new ImmutableDateTime(((new MutableDateTime(this)).rollbackToDayOfWeek(dayOfWeek)).intRep());
    }

    public DateTime rollMonths(int num) {
        return new ImmutableDateTime(((new MutableDateTime(this)).rollMonths(num)).intRep());
    }

    public DateTime rollToDayOfWeek(int dayOfWeek) {
        return new ImmutableDateTime(((new MutableDateTime(this)).rollToDayOfWeek(dayOfWeek)).intRep());
    }

    public DateTime nextBusinessDay() {
        return new ImmutableDateTime(((new MutableDateTime(this)).nextBusinessDay()).intRep());
    }

    public DateTime nextWeekday() {
        return new ImmutableDateTime(((new MutableDateTime(this)).nextWeekday()).intRep());
    }

    public DateTime nextWeek() {
        return new ImmutableDateTime(((new MutableDateTime(this)).nextWeek()).intRep());
    }

    public DateTime nextMonth() {
        return new ImmutableDateTime(((new MutableDateTime(this)).nextMonth()).intRep());
    }

    public DateTime nextQuarter() {
        return new ImmutableDateTime(((new MutableDateTime(this)).nextQuarter()).intRep());
    }

    public DateTime nextYear() {
        return new ImmutableDateTime(((new MutableDateTime(this)).nextYear()).intRep());
    }

    public DateTime rollYears(int numYears) {
        return new ImmutableDateTime(((new MutableDateTime(this)).rollYears(numYears)).intRep());
    }

    public DateTime priorWeekday() {
        return new ImmutableDateTime(((new MutableDateTime(this)).priorWeekday()).intRep());
    }

    public DateTime priorBusinessDay() {
        return new ImmutableDateTime(((new MutableDateTime(this)).priorBusinessDay()).intRep());
    }


    // -----------------------------------------------------------
	// Format methods
    // -----------------------------------------------------------

    /**
     * Answer new MutableDateTime based on input string and default format
     * 
     * @param dtString
     * @return MutableDateTime
     * @throws ParseException
     */
    public static ImmutableDateTime parse(String dtString) throws ParseException {
    	return parse(dtString, DefaultParserRenderer);
    }

    /**
     * Answer new MutableDateTime based on input string and given format
     *  
     * @param dtString
     * @param format DateTimeFormat
     * @return MutableDateTime
     * @throws ParseException
     */
    public static ImmutableDateTime parse(String dtString, DateTimeFormat format) throws ParseException {
    	MutableDateTime mdt = new MutableDateTime();
        mdt = (MutableDateTime)format.parseToDateTime(mdt, dtString);
        return new ImmutableDateTime(mdt); 
    }


	// -----------------------------------------------------------
	// Conversion methods
	// -----------------------------------------------------------


    /**
	 * Answers an integer equivalent from YMD (March 17th 1964 becomes 19640317).
	 *
	 * @return int equivalent integer
	 */
	 public int toInt() {
		int yr = this.year();
		int mo = this.month();
		int dy = this.day();
		return (yr*10000) + (mo*100) + dy;
	 }

	/**
	 * <p>
	 * Converts PSC Date to native Java Date.
	 * </p>
	 *
	 * @return java.util.Date date
	 */
	public java.util.Date toJavaDate() {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(this.year(), this.month()-1, this.day());
		return calendar.getTime();
	}

}