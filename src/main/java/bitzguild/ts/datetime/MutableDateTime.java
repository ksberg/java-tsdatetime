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
 * @see DateTimePredicate
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
	 * DateTime objects.
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
    public MutableDateTime(DateTime other) {
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

    /**
     * Answer Copy (or not) of given datetime
     *
     * @param dt AbstractDateTime
     * @return DateTime as ImmutableDateTime
     */
    protected DateTime _withInstance(DateTime dt) {
        return dt;
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
    public DateTime setHoursMinutesSeconds(int hours, int mins, int secs) {
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
    public DateTime setHoursMinutesSecondsMillis(int hours, int mins, int secs, int smillis) {
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
    public DateTime setMillisSinceMidnight(int millis) {
        _time = millis % MillisInDay;
        return this;
    }



    // -----------------------------------------------------------
	// Instance Methods - Effectors
	// -----------------------------------------------------------


	/**
	 * Answer date bumped by numYears
	 *
	 * @param numYears number of yearsTo to increment/decrement
	 */
	public DateTime rollYears(int numYears) {
		_year += numYears;
		return this;
	}

    // -----------------------------------------------------------------
    // Generation Methods
    // -----------------------------------------------------------------





    // -----------------------------------------------------------------
    // Specific Relative Days
    // -----------------------------------------------------------------



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
     * @param format DateTimeFormat
     * @return MutableDateTime
     * @throws ParseException
     */
    public static MutableDateTime parse(String dtString, DateTimeFormat format) throws ParseException {
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