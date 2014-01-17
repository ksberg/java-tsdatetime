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
import java.util.ArrayList;
import java.util.Calendar;

/**
 * <p>
 * The MutableDate class is designed for state-based operations that do not
 * require the creation of a new object. In particular, incrementing and changing
 * values should take minimal time.
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
 * @see IDateTimePredicate
 *
 * @author Kevin Sven Berg
 */
public class MutableDateTime extends AbstractDateTime implements java.io.Serializable {

	public static final long serialVersionUID = 1L;
	
	public static final boolean DEBUG = false;
	

	// --------------------------------------------
	// Existence
	// --------------------------------------------

	/**
	 * <p>
	 * Default Date constructor. The value of the new _date is undefined.
	 * Use this form to quickly create dates that need to be manipulated
	 * using external src (e.g. file reads, etc).
	 * </p>
	 */
	public MutableDateTime() {
		super();
	}

	/**
	 * Construct a new date from the given serial representation. The
	 * serial representation is a compact integer form that is strictly
	 * increasing, like the Date object it represents, so that comparing
	 * any two representations yields the same result as comparing two 
	 * IDateTime objects.
	 *
	 * @param serial compact date representation
	 */
	public MutableDateTime(long serial) {
		super();
        _setRep(serial);
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
	public MutableDateTime(int yearInteger, int dayCount) {
		super();
		setYearAndDayCount(yearInteger, dayCount);
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
	public MutableDateTime(int theYear, int theMonth, int theDay) {
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
	public MutableDateTime(int theYear, int theMonth, int theDay, int hours, int minutes, int seconds, int smillis) {
		super(theYear, theMonth, theDay);
		this._setHoursMinutesSecondsMillis(hours, minutes, seconds, smillis);
	}


	/**
	 * Copy constructor
	 * 
	 * @param other
	 */
    public MutableDateTime(IDateTime other) {
    	super(other);
    }


    /**
     * <p>
     * Create a new date set to now's _dayOfYear, month, and year.
     * </p>
     *
     * @return MutableDateTime now
     */
    public static MutableDateTime now() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        MutableDateTime md = MutableDateTime.yearMonthDay(
                cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH) + 1,
                cal.get(java.util.Calendar.DAY_OF_MONTH)
        );
        md._time = (cal.get(java.util.Calendar.HOUR_OF_DAY) * MillisInHour)
                + (cal.get(java.util.Calendar.MINUTE) * MillisInMinute)
                + (cal.get(java.util.Calendar.SECOND) * MillisInSecond)
                + cal.get(java.util.Calendar.MILLISECOND);
        return md;
    }

    /**
     * Answer new MutableDateTime from given year, month, and day (e.g. 2000, 1, 1)
     *
     * @param iyear integer year
     * @param monthIndex integer month (1..12)
     * @param dayIndex integer _dayOfYear (1..28+)
     * @return MutableDateTime
     */
    public static MutableDateTime yearMonthDay(int iyear, int monthIndex, int dayIndex) {
        return new MutableDateTime(iyear,monthIndex,dayIndex);
    }

    /**
     * Answer new MutableDateTime from given year, month, and day (e.g. 2000, 1, 1), 
     * with time as hour, minute, second (e.g. 2:30PM is 14,30,0). 
     *
     * @param iyear integer year
     * @param monthIndex integer month (1..12)
     * @param dayIndex integer day (1..28+)
     * @param hour (0..23)
     * @param minute (0..59)
     * @param second (0..59)
     * @return MutableDateTime
     */
    public static MutableDateTime yearMonthDayHourMinuteSecond(int iyear, int monthIndex, int dayIndex, int hour, int minute, int second) {
        return new MutableDateTime(iyear,monthIndex,dayIndex, hour, minute, second, 0);
    }

    

    // --------------------------------------------------------
    // Predicates
    // --------------------------------------------------------

//    public boolean isToday() { return compareTo(now()) == 0; }


    // --------------------------------------------------------
    // Java Date Conversion
    // --------------------------------------------------------

    /**
     * <p>
     * Assigns the PSC Date from the Java native Date.
     * </p>
     *
     * @param jDate new date
     */
    public void setJavaDate(java.util.Date jDate) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(jDate);
        int iYear = calendar.get(java.util.Calendar.YEAR);
        int iMonth = calendar.get(java.util.Calendar.MONTH);
        int iDay = calendar.get(java.util.Calendar.DATE);
        int iHour = calendar.get(Calendar.HOUR);
        int iMinute = calendar.get(Calendar.MINUTE);
        int iSecond = calendar.get(Calendar.SECOND);
        int iMillis = calendar.get(Calendar.MILLISECOND);

        _setFromYearMonthDay(iYear, iMonth + 1, iDay);
        _setHoursMinutesSecondsMillis(iHour,iMinute,iSecond,iMillis);
        
    }

	// ------------------------------------------------------------------------------------
	// Instance Methods - ~Accessors
    // ------------------------------------------------------------------------------------

    /**
     * Setter for primitive representation.
     * Primitives can be stored, sequenced,
     * and manipulated without creating a
     * ImmutableDateTimeScrap object.
     *
     * @param dtSerial long representation
     */
    public void setRep(long dtSerial) {
        int dserial = (int)(dtSerial >> 28);
        this.setIntRep(dserial);
        _time = (int)dtSerial & 0xFFFFFFF;
    }

	/**
	 * Set the date using commonly encountered format. The algorithm is not strict,
	 * and will take invalid month/_dayOfYear combinations. This is a convenience
	 * format for dayCount and Year.
	 *
	 * @param year int (-4,194,303 .. +4,194,304)
	 * @param month int (1..12)
	 * @param day int (1..31)
	*/
    public MutableDateTime setFromYearMonthDay(int year, int month, int day) {
        _setFromYearMonthDay(year, month, day);
        return this;
    }
      
	
	/**
	 * Set the date using _dayOfYear offset into the given year. If dayCount is greater
	 * that number of days in the given year, the constructor will wrap into
	 * the appropriate year. The specified year can be positive or negative.
	 * Negative yearsTo correspond to B.C. and work appropriately. Year may
	 * by any number between +4,194,303 and -4,194,304.
	 * <br><br>
	 * This method does not trigger any calculation or object creation.
	 * <br><br>
	 *
	 * @param year number of days into given year
	 * @param dayCount year (-4,194,303 .. +4,194,304)
	 */
	protected void setYearAndDayCount(int year, int dayCount) {
		this._year = year;
		this._dayOfYear = dayCount;
	}

	/**
	 * Set the date using the compact serial representation. Useful
	 * for reading and writing dates to integers. Allows efficient
	 * storage, retrieval and setting. A single Date instance can
	 * be used to quickly service any number of date settings.
	 * <br><br>
	 * Serial representations are monotonicly increasing, and
	 * can be compared directly without special functions, just
	 * like their Date object counterparts.
	 * <br><br>
	 *
	 * @param serial compact date representation
	 */
	public void setIntRep(int serial) {
		_dayOfYear = serial & 0x1FF;
		_year = serial >> 9;
	}

    /**
     * <p>
     * Accessor for setting _time in HH:MM:SS.
     * </p>
     *
     * @param hours hours from 0 to 23
     * @param mins minutes from 0 to 59
     * @param secs seconds from 0 to 59
     * @return int milliseconds from midnight of any _dayOfYear
     */
    public IDateTime setHoursMinutesSeconds(int hours, int mins, int secs) {
        _time = (hours * MillisInHour) + (mins * MillisInMinute) + (secs * MillisInSecond);
        return this;
    }

    /**
     * Setter for _time in HH:MM:SS.ms.
     *
     * @param hours integer hours (0..23)
     * @param mins integer minutes (0..59)
     * @param secs integer seconds (0..59)
     * @param smillis integer milliseonds (0..999)
     * @return MutableDateTime
     */
    public IDateTime setHoursMinutesSecondsMillis(int hours, int mins, int secs, int smillis) {
        int pinMillis = smillis % MillisInSecond;
        _time = (hours * MillisInHour) + (mins * MillisInMinute) + (secs * MillisInSecond) + pinMillis;
        return this;
    }

    /**
     * Setter for total milliseconds since midnight. Some market
     * data services use this to timestamp real-time data.
     *
     * @param millis integer
     * @return MutableDateTime
     */
    public IDateTime setMillisSinceMidnight(int millis) {
        _time = millis % MillisInDay;
        return this;
    }


    /**
     * Set the _holidays function for this date only.
     * The Holiday function is a IDateTimePredicate that
     * returns true if the given date is considered
     * a holiday. The Date object supports pluggable
     * _holidays at either class and instance levels.
     *
     * @param holidayz holiday function
     *
     * @see AbstractDateTime#setDefaultHolidays Holiday Default
     * @see IDateTimePredicate
     * @see AbstractDateTime#nextBusinessDay Business Day
     * @see AbstractDateTime#isHoliday Holiday Query
     */
    public void setHolidays(IDateTimePredicate holidayz) {
        _holidays = holidayz;
    }


    // -----------------------------------------------------------
	// Instance Methods - Effectors
	// -----------------------------------------------------------


	/**
	 * Answer date bumped by numYears
	 *
	 * @param numYears number of yearsTo to increment/decrement
	 */
	public IDateTime rollYears(int numYears) {
		_year += numYears;
		return this;
	}

    /**
     * <p>
     * Answer the business day of the month.
     * This counts consecutive business days
     * since the start of the month.
     * </p>
     * @return int BDOM
     */
    public int businessDayOfMonth() {
        MutableDateTime date = new MutableDateTime(this.intRep());

        int bizDOM = 0;
        int month = date.month();
        while(month == date.month()) {
            date.priorBusinessDay();
            bizDOM++;
        }
        return bizDOM;
    }

    // -----------------------------------------------------------
    // Bounding Ranges
    // -----------------------------------------------------------

    /**
     * Answer _year spanning range that includes current datetime
     *
     * @return DateTimeRange
     */
    public DateTimeRange boundsForYear() {
        MutableDateTime dtA = new MutableDateTime(year(),1,1);
        MutableDateTime dtZ = new MutableDateTime(dtA);
        dtZ.rollYears(1);
        dtZ.addMillis(-1);
        return new DateTimeRange(dtA,dtZ);
    }


    /**
     * Answer month spanning range that includes current datetime
     *
     * @return DateTimeRange
     */
    public DateTimeRange boundsForMonth() {
        MutableDateTime dtA = new MutableDateTime(year(),month(),1);
        MutableDateTime dtZ = new MutableDateTime(dtA);
        dtZ.nextMonth();
        dtZ.addMillis(-1);
        return new DateTimeRange(dtA,dtZ);
    }

    /**
     * Answer week spanning range that includes current datetime
     *
     * @return DateTimeRange
     */
    public DateTimeRange boundsForWeek() {
        MutableDateTime dtA = new MutableDateTime(this);
        dtA.priorWeekday();
        dtA.addWeeks(-1);
        dtA.nextWeek();
        MutableDateTime dtZ = new MutableDateTime(dtA);
        dtZ.nextWeek();
        dtZ.addMillis(-1);
        return new DateTimeRange(dtA,dtZ);
    }

    /**
     * Answer _dayOfYear spanning range that includes current ImmutableDateTime
     *
     * @return DateTimeRange
     */
    public DateTimeRange boundsForDay() {
        MutableDateTime dtA = new MutableDateTime(this);
        dtA.setHoursMinutesSecondsMillis(0, 0, 0, 0);
        MutableDateTime dtZ = new MutableDateTime(dtA);
        dtZ._time = MillisInDay - 1;
        return new DateTimeRange(dtA,dtZ);
    }

    /**
     * Answer hour/minute on given _dayOfYear spanning range that includes current ImmutableDateTime.
     * This is useful for pinning business hours between 9:00 AM and 4:30 PM, for example.
     *
     * @return DateTimeRange
     */
    public DateTimeRange boundsForDayHours(int startHours, int startMinutes, int endHours, int endMinutes) {
        MutableDateTime dtA = new MutableDateTime(this);
        dtA.setHoursMinutesSecondsMillis(startHours, startMinutes, 0, 0);
        MutableDateTime dtZ = new MutableDateTime(dtA);
        dtA.setHoursMinutesSecondsMillis(endHours, endMinutes, 0, 0);
        return new DateTimeRange(dtA,dtZ);
    }

    // -----------------------------------------------------------------
    // Generation Methods
    // -----------------------------------------------------------------



    /**
     * Answer a generator that will iterate over business days in given month.
     *
     * @return DateTimeGenerator
     */
    public DateTimeIterator businessDaysInMonth() {
        return new BoundedDateTimeIterator(boundsForMonth(), DateTimeIterator.businessDay());
    }


    /**
     * Answer generator for business days in this month up until given datetime
     * @param before MutableDateTime
     * @return DateTimeGenerator
     */
    public DateTimeIterator businessDaysInMonthBefore(IDateTime before) {
        DateTimeRange range = boundsForMonth();
        range = new DateTimeRange(range.lower(), before);
        return new BoundedDateTimeIterator(range, DateTimeIterator.businessDay());
    }

    /**
     * Answer generator for business days left until given datetime
     * @param before MutableDateTime
     * @return DateTimeGenerator
     */
    public DateTimeIterator businessDaysBefore(IDateTime before) {
        DateTimeRange range = new DateTimeRange(this, before);
        return new BoundedDateTimeIterator(range, DateTimeIterator.businessDay());
    }

    public DateTimeIterator businessDaysBeforeNthWeekday(IDateTime before, int nth, int weekdayIndex) {
        DateTimeRange range = new DateTimeRange(this, before);
        return new BoundedDateTimeIterator(range, DateTimeIterator.businessDay());
    }


    // -----------------------------------------------------------------
    // Specific Relative Days
    // -----------------------------------------------------------------


    /**
     * Answer the nth business _dayOfYear of the month. Appears in Futures first
     * notice _dayOfYear (FND).
     * <p>Examples</p>
     * <ul>
     *     <li>1st business _dayOfYear of contract month (Paladium, Platinum)</li>
     * </ul>
     *
     * @param n occurrence
     * @return MutableDateTime
     */
    public IDateTime nthBusinessDayOfMonth(int n) {
        DateTimeIterator bizdays = businessDaysInMonth();
        int target = 0;
        IDateTime dt = null;
        while(bizdays.hasNext()) {
            target++;
            dt = bizdays.next();
            if (target == n) return dt;
        }
        return dt;
    }


    /**
     * Answer the nth occurrence of a given weekday in the month. Legal examples
     * include the 3rd Thursday, 2nd Wednesday, 4th Saturday, 1st Sunday, etc.
     * Result will be null if either dayOfWeek or n is out of range.
     *
     * @param nth repetition of given _dayOfYear-of-week (0,1,2,3...)
     * @param dayOfWeek day-of-week (0..6)
     * @return MutableDateTime
     */
    public IDateTime nthWeekdayOfMonth(int nth, int dayOfWeek) {
        if (dayOfWeek > 6) return null;
        return nthWeekdayHelper(nth,dayOfWeek,false);
    }

    /**
     * Answer the nth occurrence of a given business weekday. Business days
     * exclude _holidays.
     *
     * @param nth (0,1,2...) occurrence
     * @param dayOfWeek (0..6) starting on Monday
     * @return MutableDateTime
     */
    public IDateTime nthBusinessWeekdayOfMonth(int nth, int dayOfWeek) {
        if (dayOfWeek > 4) return null;
        return nthWeekdayHelper(nth,dayOfWeek,true);
    }

    /**
     * Common algorithms between nthWeekdayOfMonth & nthBusinessWeekdayOfMonth
     *
     * @param nth (0,1,2...) occurrence
     * @param dayOfWeek (0..6) starting on Monday
     * @param checkHolidays boolean
     * @return MutableDateTime
     */
    private IDateTime nthWeekdayHelper(int nth, int dayOfWeek, boolean checkHolidays) {
        if (nth < 1) return null;
        if (dayOfWeek < 0) return null;

        int month = this.month();
        MutableDateTime dt = MutableDateTime.yearMonthDay(year(), month, 1);
        dt.rollToDayOfWeek(dayOfWeek);
        int count = 0;
        while(count < nth && dt.month() == month) {
            dt.addDays(7);
            if (checkHolidays && dt.isHoliday()) dt.addDays(7);
            count++;
        }
        return (dt.month() == month) ? dt : null;
    }

    /**
     * Answer the nth last business _dayOfYear counting backward in the month. This
     * measure frequently appears for contract expiration in Futures and Options.
     * <p>Examples</p>
     * <ul>
     *     <li>last business _dayOfYear of prior month (Bonds)</li>
     *     <li>2nd last business _dayOfYear of month preceding contract month (Metals)</li>
     *     <li>5th last business _dayOfYear of month prior to contract month (Cotton)</li>
     *     <li>7th last business _dayOfYear of month prior to contract month (Coffee)</li>
     *     <li>10th last business _dayOfYear of month prior to contract month (Cocoa)</li>
     * </ul>
     *
     * @param nth last nth index (0..N counting from last _dayOfYear)
     * @return MutableDateTime
     */
    public IDateTime lastNthBusinessDayOfMonth(int nth) {
        return lastNthBusinessDayOfMonthBefore(nth, null);
    }

    /**
     * Answer the nth last business _dayOfYear counting backward in the month.
     * This
     * measure frequently appears for contract expiration in Futures and Options.
     * <p>Examples</p>
     * <ul>
     *     <li>XXX (Bonds)</li>
     * </ul>
     *
     * @param nth last nth index (0..N counting from last _dayOfYear)
     * @return MutableDateTime
     */
    public IDateTime lastNthBusinessDayOfMonthBefore(int nth, IDateTime before) {
        DateTimeIterator gen = (before != null) ? businessDaysInMonthBefore(before) : businessDaysInMonth();
        ArrayList<IDateTime> days = new ArrayList<>();
        while(gen.hasNext()) days.add(gen.next());
        int size = days.size();
        if (nth >= size) return null;
        return days.get(size-nth-1);
    }


    /**
     * This measure appears for last trading _dayOfYear (expiration) for Futures and Options.
     *
     * <p>Examples</p>
     * <ul>
     *     <li>2nd business _dayOfYear preceding 3rd wednesday of contract month (Currencies)</li>
     * </ul>
     * @param nth
     * @param ith
     * @param wkday
     * @return
     */
    public IDateTime lastNthBusinessDayOfMonthBeforeIthWeekday(int nth, int ith, int wkday) {
        IDateTime dt = nthBusinessWeekdayOfMonth(ith,wkday);
        if (dt == null) return null;
        DateTimeIterator gen = this.businessDaysBefore(dt);
        ArrayList<IDateTime> days = new ArrayList<>();
        while(gen.hasNext()) days.add(gen.next());
        int size = days.size();
        if (nth >= size) return null;
        return days.get(size-nth-1);
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
    public static MutableDateTime parse(String dtString) throws ParseException {
    	return parse(dtString, DefaultParserRenderer);
    }

    /**
     * Answer new MutableDateTime based on input string and given format
     *  
     * @param dtString
     * @param format IDateTimeFormat
     * @return MutableDateTime
     * @throws ParseException
     */
    public static MutableDateTime parse(String dtString, IDateTimeFormat format) throws ParseException {
        return (MutableDateTime)format.parseToDateTime(new MutableDateTime(), dtString);
    }
    
    
    // -----------------------------------------------------------
	// Conversion methods
	// -----------------------------------------------------------


	/**
	 * <p>
	 * Assigns the Java native Date to PSC Date.
	 * </p>
	 *
	 * @param jDate new date
	 */
	public void setFromJavaDate(java.util.Date jDate) {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTime(jDate);
		int iYear = calendar.get(java.util.Calendar.YEAR);
		int iMonth = calendar.get(java.util.Calendar.MONTH);
		int iDay = calendar.get(java.util.Calendar.DATE);
        int iHour = calendar.get(Calendar.HOUR);
        int iMinute = calendar.get(Calendar.MINUTE);
        int iSecond = calendar.get(Calendar.SECOND);
        int iMillis = calendar.get(Calendar.MILLISECOND);

		this._setFromYearMonthDay(iYear, iMonth + 1, iDay);
        this.setHoursMinutesSecondsMillis(iHour, iMinute, iSecond, iMillis);
	}

	/**
	 * <p>
	 * Set from an integer year-month-_dayOfYear.
	 * Accepts an offset parameter which can
	 * be used with non-Y2K compliant dates (blech!).
	 * </p>
	 *
	 * @param ymd year-month-_dayOfYear composite (19980101 or 980101)
	 */
	public void setFromYYYYMMDD(int ymd) {
        int year = (ymd / 10000);
        int month = ((ymd - (year*10000)) / 100);
        int day = (ymd - (year*10000) - (month*100));
        this._setFromYearMonthDay(year, month, day);
	}


}