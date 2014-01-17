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


/**
 * Non-mutable representation of _date _time range, having lower
 * and upper bounds. Includes static creation methods and set
 * arithmetic on ranges.
 * 
 * There is mutable output side-effect in the 'intersection' method.
 * 
 * @author ksvenberg
 */
public class DateTimeRange {
	
	/**
	 * Create new range from _date, up to but not including end _date
	 * 
	 * @param dtLower MutableDateTime
	 * @param dtUpper MutableDateTime
	 * @return DateTimeRange
	 */
	public static DateTimeRange fromTo(IDateTime dtLower, IDateTime dtUpper)    {
		return new DateTimeRange(dtLower,dtUpper);
	}

	/**
	 * Create new range from _date, up to but not including end _date
	 * 
	 * @param lower long _date _time representation
	 * @param upper long _date _time representation
	 * @return DateTimeRange
	 */
	public static DateTimeRange fromTo(long lower, long upper)    {
		return new DateTimeRange(lower,upper);
	}
	
	/**
	 * Create new range from _date, up to and including end _date
	 * 
	 * @param dtLower MutableDateTime
	 * @param dtUpper MutableDateTime
	 * @return DateTimeRange
	 */
	public static DateTimeRange fromUpTo(IDateTime dtLower, IDateTime dtUpper)  {
        MutableDateTime dtTmp = new MutableDateTime(dtUpper);
        dtTmp.addMillis(1);
		return new DateTimeRange(dtLower,dtTmp);
	}
	
	/**
	 * Create new range from _date, up to and including end _date
	 * 
	 * @param lower long _date _time representation
	 * @param upper long _date _time representation
	 * @return DateTimeRange
	 */
	public static DateTimeRange fromUpTo(long lower, long upper)  {
        MutableDateTime dtTmp = new MutableDateTime(upper);
        dtTmp.addMillis(1);
		return new DateTimeRange(new MutableDateTime(lower),dtTmp);
	}
	
	
	
	protected long	_lowerBound;
	protected long	_upperBound;

	/**
	 * Default Constructor
	 * 
	 * Creates a zero-length range timestamped NOW
	 */
	public DateTimeRange() {
		super();
        MutableDateTime dt = MutableDateTime.now();
		long rep = dt.rep();
		_lowerBound = rep;
		_upperBound = rep;
	}
	
	/**
	 * Constructor that takes datetime reps
	 * 
	 * @param lower long _date _time representation
	 * @param upper long _date _time representation
	 */
	public DateTimeRange(long lower, long upper) {
		super();
		_lowerBound = lower;
		_upperBound = upper;
	}
	
	/**
	 * Constructor that takes MutableDateTime parameters
	 * 
	 * @param dtLower MutableDateTime
	 * @param dtUpper MutableDateTime
	 */
	public DateTimeRange(MutableDateTime dtLower, MutableDateTime dtUpper) {
		super();
		_lowerBound = dtLower.rep();
		_upperBound = dtUpper.rep();
	}
	
	/**
	 * Constructor that takes MutableDateTime parameters
	 * 
	 * @param dtLower MutableDateTime
	 * @param dtUpper MutableDateTime
	 */
	public DateTimeRange(IDateTime dtLower, IDateTime dtUpper) {
		super();
		_lowerBound = dtLower.rep();
		_upperBound = dtUpper.rep();
	}
	
	
	
	public MutableDateTime lower() { return new MutableDateTime(_lowerBound); }
	public MutableDateTime upper() { return new MutableDateTime(_upperBound); }
	
	public long lowerRep() { return _lowerBound; }
	public long upperRep() { return _upperBound; }

	/**
	 * Answer whether given range falls within our range.
	 * Sets may be the same size, with matching end points.
	 * 
	 * @param that DateTimeRange
	 * @return boolean
	 */
	public boolean contains(DateTimeRange that) {
		return (that._lowerBound >= this._lowerBound && that._upperBound <= this._upperBound);
	}

	/**
	 * Answer whether given range overlaps at least part
	 * of our range.
	 * 
	 * @param that DateTimeRange
	 * @return boolean
	 */
	public boolean intersects(DateTimeRange that) {
		return (that._lowerBound >= this._lowerBound || that._upperBound <= this._upperBound);
	}

	/**
	 * Answer new DateTimeRange that spans both the
	 * src and parameter ranges.
	 * 
	 * @param that DateTimeRange
	 * @return DateTimeRange
	 */
	public DateTimeRange union(DateTimeRange that) {
		return new DateTimeRange(Math.min(this._lowerBound,that._lowerBound), Math.max(this._upperBound, that._upperBound));
	}
	
	/**
	 * Answer new DateTimeRange partition represents the lower part
	 * of the combined range.
	 * 
	 * @param that DateTimeRange
	 * @return DateTimeRange
	 */
	public DateTimeRange left(DateTimeRange that) {
		return new DateTimeRange(Math.min(this._lowerBound,that._lowerBound), Math.min(this._upperBound, that._upperBound));
	}
	
	/**
	 * Answer new DateTimeRange partition represents the lower part
	 * of the combined range.
	 * 
	 * @param that DateTimeRange
	 * @return DateTimeRange
	 */
	public DateTimeRange right(DateTimeRange that) {
		return new DateTimeRange(Math.max(this._lowerBound,that._lowerBound), Math.max(this._upperBound, that._upperBound));
	}
	
	
	
	/**
	 * Answer whether given range overlaps at least in part and
	 * return any overlap as a side effect to the output parameter.
	 * 
	 * @param that DateTimeRange
	 * @param output DateTimeRange
	 * @return boolean
	 */
	public boolean insersection(DateTimeRange that, DateTimeRange output) {
		boolean overlap = this.intersects(that);
		if (overlap) {
			DateTimeRange secondLowest = this._lowerBound < that._lowerBound ? that : this;
			DateTimeRange secondHighest = this._upperBound > that._upperBound ? that : this;
			output._lowerBound = secondLowest._lowerBound;
			output._upperBound = secondHighest._upperBound;
		}
		return overlap;
	}
	
	
	/**
	 * Answer new DateTimeRange that (may) extend the upper
	 * bound and keep the existing lower bound.
	 * 
	 * @param that DateTimeRange
	 * @return DateTimeRange
	 */
	public DateTimeRange higher(DateTimeRange that) {
		return new DateTimeRange(this._lowerBound, Math.max(this._upperBound, that._upperBound));
	}
	
	/**
	 * Answer new DateTimeRange that (may) extend the lower
	 * bound and keep the existing upper bound.
	 * 
	 * @param that DateTimeRange
	 * @return DateTimeRange
	 */
	public DateTimeRange lower(DateTimeRange that) {
		return new DateTimeRange(Math.min(this._lowerBound,that._lowerBound), _upperBound);
	}



    /**
     * Answer whether given datetime rep lies within our range
     *
     * @param rep datetime long rep
     * @return
     */
    public boolean within(long rep) {
        return (rep >= this._lowerBound && rep <= this._upperBound);
    }


    /**
	 * Answer whether given _date _time lies within our range
	 * 
	 * @param test MutableDateTime
	 * @return boolean
	 */
	public boolean within(MutableDateTime test) {
        return within(test.rep());
	}

    /**
	 * Answer whether given datetime lies above our range
	 * 
	 * @param test
	 * @return boolean
	 */
	public boolean above(MutableDateTime test) {
		long rep = test.rep();
		return (rep > this._upperBound);
	}
	
	/**
	 * Answer whether given datetime lies below our range
	 * 
	 * @param test
	 * @return boolean
	 */
	public boolean below(MutableDateTime test) {
		long rep = test.rep();
		return (rep < this._lowerBound);
	}
	
    /**
	 * Answer whether given _date _time lies within our range
	 * 
	 * @param test MutableDateTime
	 * @return boolean
	 */
	public boolean within(IDateTime test) {
        return within(test.rep());
	}

    /**
	 * Answer whether given datetime lies above our range
	 * 
	 * @param test
	 * @return boolean
	 */
	public boolean above(IDateTime test) {
		long rep = test.rep();
		return (rep > this._upperBound);
	}
	
	/**
	 * Answer whether given datetime lies below our range
	 * 
	 * @param test
	 * @return boolean
	 */
	public boolean below(IDateTime test) {
		long rep = test.rep();
		return (rep < this._lowerBound);
	}
	
    // -----------------------------------------------------------
	// Range Modifiers
    // -----------------------------------------------------------

	/**
	 * 
	 * @param amount
     * @return DateTimeRange
	 */
    public DateTimeRange addMillis(int amount) {
    	MutableDateTime dtA = new MutableDateTime(this._lowerBound);
    	MutableDateTime dtZ = new MutableDateTime(this._upperBound);
    	dtA.addMillis(amount);
    	dtZ.addMillis(amount);
    	return new DateTimeRange(dtA,dtZ);
    }

    /**
     *
     * @param amount
     * @return DateTimeRange
     */
    public DateTimeRange addSeconds(int amount) {
    	MutableDateTime dtA = new MutableDateTime(this._lowerBound);
    	MutableDateTime dtZ = new MutableDateTime(this._upperBound);
    	dtA.addSeconds(amount);
    	dtZ.addSeconds(amount);
    	return new DateTimeRange(dtA,dtZ);
    }

    /**
     *
     * @param amount
     * @return DateTimeRange
     */
    public DateTimeRange addMinutes(int amount) {
    	MutableDateTime dtA = new MutableDateTime(this._lowerBound);
    	MutableDateTime dtZ = new MutableDateTime(this._upperBound);
    	dtA.addMinutes(amount);
    	dtZ.addMinutes(amount);
    	return new DateTimeRange(dtA,dtZ);
    }

    /**
     *
     * @param amount
     * @return DateTimeRange
     */
    public DateTimeRange addHours(int amount) {
    	MutableDateTime dtA = new MutableDateTime(this._lowerBound);
    	MutableDateTime dtZ = new MutableDateTime(this._upperBound);
    	dtA.addHours(amount);
    	dtZ.addHours(amount);
    	return new DateTimeRange(dtA,dtZ);
    }


    /**
     *
     * @param amount
     * @return DateTimeRange
     */
    public DateTimeRange addDays(int amount) {
    	MutableDateTime dtA = new MutableDateTime(this._lowerBound);
    	MutableDateTime dtZ = new MutableDateTime(this._upperBound);
    	dtA.addDays(amount);
    	dtZ.addDays(amount);
    	return new DateTimeRange(dtA,dtZ);
    }


    /**
     *
     * @param amount
     * @return DateTimeRange
     */
    public DateTimeRange addBusinessDays(int amount) {
    	MutableDateTime dtA = new MutableDateTime(this._lowerBound);
    	MutableDateTime dtZ = new MutableDateTime(this._upperBound);
    	dtA.addBusinessDays(amount);
    	dtZ.addBusinessDays(amount);
    	return new DateTimeRange(dtA,dtZ);
    }
    
    // -----------------------------------------------------------
	// Format methods
    // -----------------------------------------------------------

	/**
	 * Readable representation
	 * 
	 * @param strb StringBuffer
	 * @return StringBuffer
	 */
	public StringBuffer toBuffer(StringBuffer strb) {
		lower().toBuffer(strb);
		strb.append(" to ");
		upper().toBuffer(strb);
		return strb;
	}
	
	/**
	 * Answer string representation (debugging, etc)
	 */
	public String toString() { return toBuffer(new StringBuffer()).toString(); }

}
