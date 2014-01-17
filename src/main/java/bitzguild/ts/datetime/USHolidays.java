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
 * <p>
 * This holiday implementation is currently hardcoded. It does
 * not currently encompass the necessary days afforded to all honest,
 * hard working, dog-fearing American citizens. Ideally
 * an implementation would be table-driven so it can adapt
 * to the _holidays du jour.
 * </p>
 * <p>
 * Thanksgiving, Memorial, and Labor _dayOfYear calculations still
 * need to be finished. Thanksgiving requires new implementation
 * of Date.weekOfMonth().
 * </p>
 * Creation _date: (7/19/2001 3:18:45 PM)
 * @author: Kevin Sven Berg
 */
public class USHolidays implements IDateTimePredicate, java.io.Serializable {

	public static final long serialVersionUID = 1L;
	
	/**
	 * DateUSHolidays constructor comment.
	 */
	public USHolidays() {
		super();
	}

	public boolean apply(IDateTime theDate) {

		int month 	= theDate.month();
		int day 	= theDate.day();

		switch(month) {
			case DaysAndMonths.MAY:
				return isMemorialDay(theDate, month, day);

			case DaysAndMonths.JULY:
				return (day == 4);

			case DaysAndMonths.SEPTEMBER:
				return isLaborDay(theDate, month, day);

			case DaysAndMonths.NOVEMBER:
				return isThanksGiving(theDate, month, day);

			case DaysAndMonths.DECEMBER:
				switch(day) {
					case 24:
						return true;
					case 25:
						return true;
					case 31:
						return true;
				}
		}
		return false;
	}

	public boolean isMemorialDay(IDateTime theDate, int imo, int idy) {
		if (idy > 24) return (theDate.dayOfWeek() == DaysAndMonths.MONDAY);
		return false;
	}

	public boolean isLaborDay(IDateTime theDate, int imo, int idy) {
		if (idy < 8) return (theDate.dayOfWeek() == DaysAndMonths.MONDAY);
		return false;
	}

	/**
	* Thanksgiving always falls within the last 5 weekdays of November.
	*
	* @param theDate the given _date
	* @return boolean whether _date is in Thanksgiving holiday
	*/
	public boolean isThanksGiving(IDateTime theDate, int imo, int idy) {

		int weekInMonth = theDate.weekOfMonth(); // must be in 4th or 5th week
		if(weekInMonth < 4) return false;

		if (idy >= 20 && idy <= 27) return (theDate.dayOfWeek() == DaysAndMonths.THURSDAY);
		return false;
	}
}
