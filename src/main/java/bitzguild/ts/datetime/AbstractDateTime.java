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

import bitzguild.ts.datetime.format.DaysAndMonthsForEnglish;
import bitzguild.ts.datetime.format.CompactDateTimeFormat;

/**
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
public abstract class AbstractDateTime extends DateUtil implements DateTime, java.io.Serializable {

	public static final long serialVersionUID = 1L;

	public static final boolean DEBUG = false;

	public static DateTimePredicate DefaultHolidays = new USHolidays();
    public static DateTimeFormat DefaultParserRenderer = new CompactDateTimeFormat();
    public static DaysAndMonths DefaultDayMonthNames = new DaysAndMonthsForEnglish();

    // --------------------------------------------
    // Static Accessors
    // --------------------------------------------

    /**
     *
     * @param pr
     * @return
     */
    public static DateTimeFormat defaultDateTimeFormat(DateTimeFormat pr) {
        DateTimeFormat prior = DefaultParserRenderer;
        DefaultParserRenderer = pr;
        return prior;
    }

    /**
     *
     * @return
     */
    public static DateTimeFormat getDefaultDateTimeFormat() {
        return DefaultParserRenderer;
    }


    /**
     *
     * @param h
     */
    public static void setDefaultHolidays(DateTimePredicate h) {
        DefaultHolidays = h;
    }

    // --------------------------------------------
	// Attributes
	// --------------------------------------------

    protected int 	_year;          // Gregorian Year, e.g. 1945
	protected int 	_dayOfYear;     // Day Of Year with January 1st = 1
    protected int	_time;			// milliseconds since midnight
	protected DateTimePredicate _holidays = DefaultHolidays;

	// --------------------------------------------
	// Existence
	// --------------------------------------------

	/**
     * Default Constructor - not public
	 */
	protected AbstractDateTime() {
        _time = 0;
        _holidays = DefaultHolidays;
	}

	/**
	 * Rep Constructor - not public
	 * 
	 * @param rep long representation
	 */
	protected AbstractDateTime(long rep) {
		_setRep(rep);
        _holidays = DefaultHolidays;
	}
	
	protected AbstractDateTime(DateTime other) {
		_year = other.year();
		_dayOfYear = other.dayOfYear();
		_time = other.millisecondsSinceMidnight();
		_holidays = other.holidays();
	}
	
    /**
     * Components Constructor
     *
     * @param year integer year (e.g. 1945)
     * @param dayOfYear int day of year (e.g. 365)
     * @param holidays DateTimePredicate
     */
    public AbstractDateTime(int year, int dayOfYear, DateTimePredicate holidays) {
        _year = year;
        _dayOfYear = dayOfYear;
        _time = 0;
        _holidays = holidays;
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
	public AbstractDateTime(int yearInteger, int dayCount) {
		super();
		_setYearAndDayCount(yearInteger, dayCount);
        _time = 0;
        _holidays = DefaultHolidays;
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
	public AbstractDateTime(int theYear, int theMonth, int theDay) {
		super();
		_setFromYearMonthDay(theYear, theMonth, theDay);
        _time = 0;
        _holidays = DefaultHolidays;
	}



    // ------------------------------------------------------
	// Comparable interface
	// ------------------------------------------------------

	/**
	 * <p>
	 * Compare two Date instances. Method will throw
	 * a ClassCastException if parameter is not a bitzguild.util.Date.
	 * </p>
	 * <p>
	 * An improvement/extension would include java.util.Date.
	 * <p>
	 *
	 * @param that date.
	 * @return int -1 if less, 0 if equal, 1 if greater
	 */
	public int compareTo(DateTime that) {

		if (this._year > that.year()) return 1;
		if (this._year < that.year()) return -1;

		if (this._dayOfYear > that.dayOfYear()) return 1;
		if (this._dayOfYear < that.dayOfYear()) return -1;

        if (this._time > that.millisecondsSinceMidnight()) return 1;
        if (this._time < that.millisecondsSinceMidnight()) return -1;

		return 0;
	}

	/**
	 * Answers if two Date instances are equal.
	 * Unlike <code>compareTo</code>, this menthod
	 * requires that <code>_holidays</code> attribute
	 * be equal as well, making it a more stringent
	 * measure of equality.
	 *
	 * @param o other date
	 * @return boolean whether equal
	 */
	public boolean equals(Object o) {
		if (!(o instanceof DateTime)) return false;
		if (_holidays != ((DateTime)o).holidays()) return false;
		return compareTo((DateTime)o) == 0;
	}





    /**
     * <p>
     * Answer the number of yearsTo between two dates as
     * a double value. Dates may be in any order.
     * </p>
     * @param dateZ ending date
     * @return double fractional yearsTo
     */
    public double yearsTo(DateTime dateZ) {
        double fracYears =  0.0;

        DateTime dateFrom, dateTo;
        if (this.compareTo(dateZ) < 0) {
            dateFrom = this;
            dateTo = dateZ;
        } else {
            dateFrom = dateZ;
            dateTo = this;
        }

        if (dateFrom.year() == dateTo.year()) {
            fracYears = (dateTo.dayOfYear() - dateFrom.dayOfYear() + 1)/(double)dateTo.numberOfDaysInYear();
        } else {
            int yrdelta = dateTo.year() - dateFrom.year();
            int partialFrom = dateFrom.daysLeftOfYear();
            int partialTo = dateTo.dayOfYear();
            double tails = (double)(partialFrom + partialTo);

            if (yrdelta > 1) {
                fracYears = ((double)yrdelta-1.0) + (tails/365.25);
            } else {
                fracYears = tails/365.25;
            }
        }

        return fracYears;
    }

	// ------------------------------------------------------------------------------------
	// Instance Methods - ~Accessors
    // ------------------------------------------------------------------------------------


    /**
     * Answer predicate function that indicates holidays.
     *
     * @return DateTimePredicate
     */
    public DateTimePredicate holidays() {
        return _holidays;
    }

    public long rep() {
        long dserial = (long)((_year << 9) | _dayOfYear);
        return (dserial << 28) | _time;
    }

    protected void _setRep(long rep) {
		int dserial = (int)(rep >> 28);
		_dayOfYear = dserial & 0x1FF;
		_year = dserial >> 9;
		_time = (int)rep & 0xFFFFFFF;
    }
    
    /**
     * Get the compact serial representation. Useful for reading and
     * writing dates to integers. Allows efficient storage, retrieval,
     * and setting. A single Date object can quickly service any number
     * of date settings without recalculation.
     * <br><br>
     * Serial representations are monotonicly increasing, and
     * can be compared directly without special functions, just
     * like their Date object counterparts.
     * <br><br>
     *
     * @return int compact date representation
     */
    public int intRep() {
        return (_year << 9) | _dayOfYear;
    }

    /**
     * Answer integer year (e.g. 1957)
     *
     * @return int
     */
    public int year() { return _year; }


    /**
     * Answer common use month index (e.g. 1..12)
     *
     * @return int month
     */
    public int month() {
        int leap = leap();
        for(int iMonth=11; iMonth>=0; iMonth--) {
            int firstDay = DateUtil.FirstDayOfMonth[iMonth] + ((iMonth > 1) ? leap : 0);
            if(firstDay <= _dayOfYear)
                return iMonth+1;
        }
        return -1;
    }


    /**
     * Answer the calendar day of the month (1 ... 31 max)
     *
     * @return int
     */
	public int day() {
		int imo = month()-1;
		return _dayOfYear - (DateUtil.FirstDayOfMonth[imo] + ((imo > 1) ? leapYearBalance(_year) : 0)) + 1;
	}

	
    public int hours() {
        return _time / MillisInHour;
    }

    public int minutes() {
        int balance = _time % MillisInHour;
        return balance / MillisInMinute;
    }

    public int seconds() {
        int balanceHours = (_time % MillisInHour) ;
        int balanceMinutes = balanceHours % MillisInMinute;
        return balanceMinutes / MillisInSecond;
    }

    public int millis() {
        int balanceHours = (_time % MillisInHour) ;
        return balanceHours % MillisInSecond;
    }

    
    public int minutesSinceMidnight() {
        return _time / MillisInMinute;
    }

    public int millisecondsSinceMidnight() {
    	return _time;
    }

	

	/**
	 * <p>
	 * Returns 0 through 6 corresponding to Monday through Sunday.
	 * </p>
	 * @return int dayOfYear of week, weekdays first
	 */
	public int dayOfWeek() {
		int yearIndex, dayIndex;

		if(_dayOfYear <= (DateUtil.FirstDayOfMonth[2])) {
			yearIndex = _year - 1;
			dayIndex = 307;
		} else {
			yearIndex = _year;
			dayIndex = -58 - this.leap();
		}
		return (dayIndex + _dayOfYear + yearIndex + (yearIndex / 4)
					+ (yearIndex / 400) - (yearIndex / 100)) % 7;
	}

	/**
	 * <p>
	 * Answer the business day of the month.
	 * This counts consecutive business days
	 * since the start of the month.
	 * </p>
	 * @return int BDOM
	 */
    public abstract int businessDayOfMonth();

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
     * Answer the nth week of the current month
     *
     * @return int
     */
    public int weekOfMonth() {
        return weekOfMonth(_dayOfYear, month());
    }

    /**
     * Answer common week of the year
     *
     * @return int
     */
    public int weekOfYear() {
        return (_dayOfYear / 7) + 1;
    }

    public DateTimePredicate getHolidays() {
		return _holidays;
	}

    // ------------------------------------------------------------------------------------
    // Names
    // ------------------------------------------------------------------------------------


    /**
     * Answer the name for day of the week (e.g. Monday, Tuesday, Wednesday ...)
     *
     * @return String
     */
    public String dayName() {
        return DefaultDayMonthNames.dayName(dayOfWeek());
    }

    /**
     * Answer the abbreviation for day of week (e.g. Mon, Tue, Wed ... )
     *
     * @return String
     */
    public String dayAbbreviation() {
        return DefaultDayMonthNames.dayAbbreviation(dayOfWeek());
    }


    /**
     * Answer the month name (e.g. January, February, March ...)
     *
     * @return String
     */
    public String monthName() {
        return DefaultDayMonthNames.monthName(month(), false);
    }

    /**
     * Answer the month abbreviation (e.g. Jan, Feb, Mar ...)
     *
     * @return String
     */
    public String monthAbbreviation() {
        return DefaultDayMonthNames.monthAbbreviation(month());
    }



    // -----------------------------------------------------------
    // XXX
    // -----------------------------------------------------------

	protected int leap() { return leapYearBalance(_year); }


	/**
	 * Set the date using commonly encountered format. The algorithm is not strict,
	 * and will take invalid month/_dayOfYear combinations. This is a convenience
	 * format for dayCount and Year.
	 *
	 * @param iyear year (-4,194,303 .. +4,194,304)
	 * @param monthIndex month (1..12)
	 * @param dayIndex _dayOfYear (1..31)
	 */
	protected void _setFromYearMonthDay(int iyear, int monthIndex, int dayIndex) {

        _year = iyear;
        _dayOfYear = 1;

		int daysInMonth;
		int firstDayOfMonth;

        int imo = Math.max(1,Math.min(monthIndex,12));

		if(imo == 2) {
			daysInMonth = DateUtil.DaysInMonth[imo-1] + leapYearBalance(iyear);
		} else {
			daysInMonth = DateUtil.DaysInMonth[imo-1];
		}
		if(imo > 2) {
			firstDayOfMonth = DateUtil.FirstDayOfMonth[imo-1] + leapYearBalance(iyear);
		} else {
			firstDayOfMonth = DateUtil.FirstDayOfMonth[imo-1];
		}

//        int iday = Math.max(1,dayIndex);
        int iday = Math.max(1,Math.min(dayIndex, daysInMonth));

		//if((dayIndex < 1) || (dayIndex > daysInMonth))return;
		_setYearAndDayCount(iyear, iday - 1 + firstDayOfMonth);
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
	protected void _setYearAndDayCount(int year, int dayCount) {
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
	public void _setIntRep(int serial) {
		_dayOfYear = serial & 0x1FF;
		_year = serial >> 9;
	}

    /**
     *
     * @param hours
     * @param mins
     * @param secs
     * @param smillis
     * @return
     */
    public DateTime _setHoursMinutesSecondsMillis(int hours, int mins, int secs, int smillis) {
    	int pinMillis = smillis % MillisInSecond;
    	
        _time = (hours * MillisInHour) + (mins * MillisInMinute) + (secs * MillisInSecond) + pinMillis;
        return this;
    }

    /**
     *
     * @param millis
     * @return
     */
	protected int _addMillisWithBalance(int millis) {
        if (millis < 0) return _subMillisWithBalance(millis);
		int extendedTime = _time + millis;
		int balanceDays = (extendedTime / MillisInDay);
		_time = extendedTime % MillisInDay;
		return balanceDays;
	}

    protected int _subMillisWithBalance(int millis) {
        int millisLeft = _time + millis;
        int balanceDays = (millisLeft / MillisInDay)-1;
        _time = MillisInDay + (millisLeft % MillisInDay);
        return balanceDays;
    }

    protected int _notRightSubMillisWithBalance(int millis) {
        int reflection = MillisInDay - _time;
        int extendedTime = reflection + millis;
        int balanceDays = -((reflection - millis) / MillisInDay);
        _time = extendedTime % MillisInDay;
        return balanceDays;
    }

    // -----------------------------------------------------------
	// Instance Methods - Effectors
	// -----------------------------------------------------------


    /**
     * Increment or decrement by the given milliseconds
     *
     * @param amount milliseconds
     * @return MutableDateTime
     */
    public DateTime addMillis(int amount) {
        return addDays(_addMillisWithBalance(amount));
    }

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addSeconds(int amount) {
        return addDays(_addMillisWithBalance(MilliFactors[SECOND] * amount));
    }

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addMinutes(int amount) {
        return addDays(_addMillisWithBalance(MilliFactors[MINUTE] * amount));
    }

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addHours(int amount) {
        return addDays(_addMillisWithBalance(MilliFactors[HOUR] * amount));
    }

	/**
	 * Adds the specified number of days to the given date.
	 * The number of days may be positive or negative.
	 * Will wrap month and year as appropriate. Returns the
	 * original object to support chained calls. For example,
	 * <br><br>
	 * theDate.addDays(1).toString();
	 * <br><br>
	 * ... is both legal and safe.
	 * <br><br>
	 *
	 * @param numDays number of days
	 * @return Date the original object
	 */
	public DateTime addDays(int numDays) {

        int day, year, daysInYear;

        day = this.dayOfYear() + numDays;
        year = this.year();
        while(day > (daysInYear = daysInYear(year))) {
            year += 1;
            day = day - daysInYear;
        }
        while(day <= 0) {
            year -= 1;
            day += daysInYear(year);
        }
        this._setYearAndDayCount(year, day);
		return this;
	}

	/**
	 * <p>
	 * Adds or subtracts the number of business days to
	 * the given date. Wraps month and year as appropriate.
	 * </p>
	 *
	 * @param numDays numDays
	 * @return Date instance
	 */
	public DateTime addBusinessDays(int numDays) {
		if (numDays > 0) {
			while(numDays-- > 0)
				nextBusinessDay();
		} else if (numDays < 0) {
			while(numDays++ < 0)
				priorBusinessDay();
		}
		return this;
	}


	/**
	 * Shorthand for adding 7 days. The number of weeks may
	 * be positive or negative. Returns the original object
	 * to support chained calls. For example,
	 * <br><br>
	 * theDate.addWeeks(3).toString();
	 * <br><br>
	 * ... is both legal and safe.
	 * <br><br>
	 *
	 * @param numWeeks number of weeks
	 * @return Date the original object
	 */
	public DateTime addWeeks(int numWeeks) {
        return addDays(numWeeks*7);
	}

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addMonths(int amount) {
        this.rollMonths(amount);
        return this;
    }

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addQuarters(int amount) {
        this.rollMonths(3 * amount);
        return this;
    }


    /**
	 * <p>
	 * Adds the number of yearsTo to the given date. CAUTION!
	 * This <bold>is not</bold> a short-hand for addDays(),
	 * and can have unexpected effects. The algorithm maintains
	 * the _dayOfYear offset into the given year, resetting the
	 * dayCount for December 31st only when moving to a leap year.
	 * </p>
	 * <p>
	 * If you need different behavior, investigate rollYears,
	 * which tries to maintain the starting month and _dayOfYear across
	 * leap year boundaries.
	 * </p>
	 *
	 * @param numberOfYears number of weeks
	 * @return Date the original object
	 *
	 * @see AbstractDateTime#rollYears
	 */
	public DateTime addYears(int numberOfYears) {
		_year += numberOfYears;
		int daysInYear = daysInYear(_year);
		_dayOfYear = (_dayOfYear > daysInYear) ? daysInYear : _dayOfYear;
		return this;
	}

	/**
	 *
	 * @return Date the original object
	 */
	public DateTime rollbackToDayOfWeek(int dayOfWeek) {
		int currentDayOfWeek = dayOfWeek();
		if(currentDayOfWeek == dayOfWeek)
			return this;
		if(currentDayOfWeek > dayOfWeek) {
			addDays(dayOfWeek - currentDayOfWeek);
		} else {
			addDays((dayOfWeek - currentDayOfWeek) - 7);
		}
		return this;
	}

	/**
	 *
	* May want to consider option for "pinning" high or low.
	* The following shows successing calls each case: <br>
	*   HIGH: Jan 31, Feb 28, Mar 31, Apr 30
	*   LOW:  Jan 31, Feb 28, Mar 28, Apr 28
	* @return Date the original object
	*/
	public DateTime rollMonths(int num) {

		int imo = month() - 1;		// zero based month
		int targetYear = year();
		int years = 0;
		int targetMonth = 0;

		if (imo + num < 0) {
			int diff = imo + num;
			years = -1;
			years -= -diff / 12;
			targetMonth = 12 - (-diff % 12);
			targetMonth++;								// common month
			targetYear += years;
		} else if (num < 0) {
			targetMonth = imo + num + 1;			// common month
			years = 0;
		} else {
			targetMonth = (imo + num) % 12 + 1;	// common month
			years = (imo + num) / 12;
			targetYear +=  years;
		}

		int targetDay = daysInMonthForYear(targetMonth, targetYear);
		int dayInMonth = day();

		targetDay = (targetDay < dayInMonth) ? targetDay : dayInMonth;
		_setFromYearMonthDay(targetYear, targetMonth, targetDay);
        return this;
	}

	public DateTime rollToDayOfWeek(int dayOfWeek) {
		int currentDayOfWeek = dayOfWeek();
		if(currentDayOfWeek == dayOfWeek)
			return this;

		if(currentDayOfWeek < dayOfWeek) {
			addDays(dayOfWeek - currentDayOfWeek);
		} else {
			addDays(7 - (currentDayOfWeek - dayOfWeek));
		}
		return this;
	}

	public DateTime nextBusinessDay() {
		nextWeekday();
		while(isHoliday()) {
			nextWeekday();
		}
        _time = 0;
		return this;
	}

	public DateTime nextWeekday() {
		addDays(1);
		int dayOfWeek = dayOfWeek();
		if(dayOfWeek > 4) {
			addDays(7 - dayOfWeek);
		}
        _time = 0;
		return this;
	}

	public DateTime priorBusinessDay() {
		priorWeekday();
		while(isHoliday()) {
			priorWeekday();
		}
        _time = 0;
		return this;
	}

	public DateTime priorWeekday() {
		addDays(-1);
		int dayOfWeek = dayOfWeek();
		if(dayOfWeek > 5) addDays(4 - dayOfWeek);
        _time = 0;
		return this;
	}

	public DateTime nextWeek() {
		int dayOfWeek = dayOfWeek();
		addDays(7 - dayOfWeek);
        _time = 0;
		return this;
	}

	public DateTime nextMonth() {

		int year = year();
		int month = month();
		_setFromYearMonthDay(year, month, 1);
		rollMonths(1);
        _time = 0;
		return this;
	}

	public DateTime nextQuarter() {
		int year = year();
		int month = month();
		_setFromYearMonthDay(year, month, 1);

		int rem = (month - 1) % 3;
		rollMonths(3 - rem);
        _time = 0;
		return this;
	}

	public DateTime nextYear() {
		int year = year();
		_setFromYearMonthDay(year + 1, 1, 1);
        _time = 0;
		return this;
	}

	/**
	 * Answer date bumped by numYears
	 *
	 * @param numYears number of yearsTo to increment/decrement
	 */
	public DateTime rollYears(int numYears) {
		_year += numYears;
        if (this._dayOfYear > 365 && leapYearBalance(_year)==0) _dayOfYear--;
		return this;
	}



    // -----------------------------------------------------------
	// Query methods
    // -----------------------------------------------------------


    /**
     * Answer whether current year is leap year.
     *
     * @return boolean
     */
    public boolean isLeapYear() {
        return leapYearBalance(_year) == 1;
    }

    /**
     * Answer whether current day falls on weekend.
     *
     * @return boolean
     */
	public boolean isWeekend() {
		return (dayOfWeek() > 4);
	}

	/**
	 * Answers whether date falls on a holiday. Default
     * holidays can be set on the class and overridden
     * on per object instance as necessary (if mutable).
	 *
	 * @return boolean whether given date falls on holiday
	 * @see DateTimePredicate Creating holiday functions <br>
	 */
	public boolean isHoliday() {
        return _holidays.apply(this);
	}

    // -----------------------------------------------------------
    // Bounding Ranges
    // -----------------------------------------------------------

    /**
     * Answer _year spanning range that includes current ImmutableDateTime
     *
     * @return DateTimeRange
     */
    public DateTimeRange boundsForYear() {
        MutableDateTime dtA = new MutableDateTime(this);
        int iyr = dtA.year();
        dtA._setFromYearMonthDay(iyr, 1, 1);
        MutableDateTime dtZ = new MutableDateTime(dtA);
        dtZ.rollYears(1);
        dtZ.addDays(-1);
        return new DateTimeRange(dtA,dtZ);
    }


	// -----------------------------------------------------------
	// Conversion methods
	// -----------------------------------------------------------

	/**
	 * Answers an integer equivalent from YMD (March 17th 1964 becomes 19640317).
	 *
	 * @return int equivalent integer
	 */
	 public int toIntYYYYMMDD() {
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
		if (_time > 0) {
	        int hours = _time / MillisInHour;
	        int balanceHours = (_time % MillisInHour) ;
	        int minutes = balanceHours / MillisInMinute;
	        int balanceMinutes = balanceHours % MillisInMinute;
	        int seconds = balanceMinutes / MillisInSecond;
	        int millis = balanceMinutes % MillisInSecond;
	        calendar.set(this.year(), this.month()-1, this.day(),hours,minutes,seconds);
	        calendar.set(java.util.Calendar.MILLISECOND, millis);
		} else {
			calendar.set(this.year(), this.month()-1, this.day());
		}
		return calendar.getTime();
	}

    // -----------------------------------------------------------
	// Format methods
    // -----------------------------------------------------------


	/**
	 * Efficient alternative for toString().
	 */
	public StringBuffer toBuffer(StringBuffer strb) {
        return DefaultParserRenderer.renderToBuffer(this,strb);
	}

	/**
	 * Formatted Date object. Useful for debugging.
	 * See formatting functions for more precise output (NOT YET IMPLEMENTED).
	 * <br><br>
	 * Example: 12 January 2001
	 * <br><br>
	 * @return String formatted date
	 */
	public String toString() {
		StringBuffer strb = new StringBuffer();
		toBuffer(strb);
		return strb.toString();
	}

}