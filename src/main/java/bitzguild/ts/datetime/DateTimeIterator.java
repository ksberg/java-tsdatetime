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

import java.util.Iterator;

/**
 * This class adheres to the Java Iteration pattern. The inner implementation uses
 * DateTimeIncrementer, which operates on mutable data types. Use this class when
 * the standard iteration is desired. Use DateTimeIncrementer when top performance
 * is required (less object creation).
 */
public class DateTimeIterator implements Iterator<DateTime> {

	// ---------------------------------------------------------------------------
	// Incrementer Functions
	// ---------------------------------------------------------------------------

    /**
     * Answers new incrementer which will iterate by n yearsTo. Minimum is 1 _year.
     *
     * @return DateTimeIncrementer
     */
    public static DateTimeIncrementer years(int n) { return new DateTimeIncrementer(n) {
            public void increment(MutableDateTime dt) { dt.addYears(_length); }
        };
    }

    /**
     * Answers new incrementer which will iterate by n months. Minimum is one month.
     *
     * @return DateTimeIncrementer
     */
    public static DateTimeIncrementer months(int n) { return new DateTimeIncrementer(n) {
            public void increment(MutableDateTime dt) { dt.addMonths(_length); }
        };
    }

    /**
     * Answers new incrementer which will iterate by n weeks. Minimum is 1 week.
     *
     * @return DateTimeIncrementer
     */
    public static DateTimeIncrementer weeks(int n) { return new DateTimeIncrementer(n) {
            public void increment(MutableDateTime dt) { dt.addWeeks(_length); }
        };
    }

    /**
     * Answers new incrementer which will iterate by n days. Minimum is 1 _dayOfYear.
     *
     * @return DateTimeIncrementer
     */
    public static DateTimeIncrementer days(int n) { return new DateTimeIncrementer(n) {
            public void increment(MutableDateTime dt) { dt.addDays(_length); }
        };
    }

    /**
     * Answers new incrementer which will iterate by n hours. Minimum is 1 hour.
     *
     * @return DateTimeIncrementer
     */
    public static DateTimeIncrementer hours(int n) { return new DateTimeIncrementer(n) {
            public void increment(MutableDateTime dt) { dt.addHours(_length); }
        };
    }

    /**
     * Answers new incrementer which will iterate by n minutes. Minimum is 1 minute.
     *
     * @return DateTimeIncrementer
     */
    public static DateTimeIncrementer minutes(int n) { return new DateTimeIncrementer(n) {
            public void increment(MutableDateTime dt) { dt.addMinutes(_length); }
        };
    }

    /**
     * Answers new incrementer which will iterate by n seconds. Minimum is 1 second.
     *
     * @return DateTimeIncrementer
     */
    public static DateTimeIncrementer seconds(int n) { return new DateTimeIncrementer(n) {
            public void increment(MutableDateTime dt) { dt.addSeconds(_length); }
        };
    }


    /**
     * Answers new incrementer which will iterate by business days.
     *
     * @return DateTimeIncrementer
     */
    public static DateTimeIncrementer businessDays(final DateTimePredicate holidays) { return new DateTimeIncrementer() {
            public void increment(MutableDateTime dt) {
                dt.nextBusinessDay(holidays);
            }
        };
    }


	// ---------------------------------------------------------------------------
	// Attributes
	// ---------------------------------------------------------------------------
	
	
	protected MutableDateTime       _current;
	protected DateTimeIncrementer	_incrementer;
	protected boolean 				_active;

	// ---------------------------------------------------------------------------
	// Existence
	// ---------------------------------------------------------------------------
	
	
	/**
	 * Default Constructor. Always assumes
     * to start from current _date and _time
     * and increment at daily interval.
	 */
	public DateTimeIterator() {
		_active = true;
		_current = MutableDateTime.now();
		_incrementer = days(1);
	}


	/**
	 * ImmutableDateTimeScrap & Incrementer Constructor
	 * 
	 * @param start MutableDateTime
	 * @param inc incrementer function
	 */
	public DateTimeIterator(DateTime start, DateTimeIncrementer inc) {
		_active = true;
        _current = new MutableDateTime(start);
		_incrementer = inc;
	}

	// ---------------------------------------------------------------------------
	// Iterator Interface
	// ---------------------------------------------------------------------------
	
	/**
	 * Terminate the iteration
	 */
	public void terminate() {
		_active = false;
	}
	
	
	
	// ---------------------------------------------------------------------------
	// Iterator Interface
	// ---------------------------------------------------------------------------
	
	
	@Override
	public boolean hasNext() { return _active; }

	@Override
	public DateTime next() {
        ImmutableDateTime result = new ImmutableDateTime(_current);
		_incrementer.increment(_current);
		return result;
	}

	@Override
	public void remove() {}

}
