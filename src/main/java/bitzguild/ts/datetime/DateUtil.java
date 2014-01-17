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

public class DateUtil {

    protected static final int[]      DaysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    protected static final int[]      FirstDayOfMonth = { 1, 32, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335 };

    /**
     * Answer the number of days in the month named monthName in the year yearInteger.
     * Month index is common use index (1-12). Year can be positive or negative, with
     * negative yearsTo treated as B.C.
     *
     * @param commonMonthIndex (1=January, 12=December)
     * @param yearInteger plus=A.D., minus=B.C.
     */
    public static int daysInMonthForYear(int commonMonthIndex, int yearInteger) {
        int monthIndex = commonMonthIndex-1;
        return DaysInMonth[monthIndex] + ((monthIndex == 1) ? leapYearBalance(yearInteger) : 0);
    }

    /**
     * Answer number of days in given year, accounting for leap yearsTo.
     *
     * @param yearInteger int year
     * @return int
     */
    public static int daysInYear(int yearInteger) {
        return 365 + leapYearBalance(yearInteger);
    }

    public static int firstDayOfMonth(int commonMonthIndx, int iyear) {
        return ((commonMonthIndx > 2) ? leapYearBalance(iyear) : 0) + FirstDayOfMonth[commonMonthIndx-1];
    }


    /**
     * Answer 1 if the year yearInteger is a leap year or 0 if it is not
     *
     * @param yearInteger int year
     */
    public static int leapYearBalance(int yearInteger) {
        int adjustedYear = (yearInteger > 0) ? yearInteger : -(yearInteger + 1);
        if(((adjustedYear % 4) != 0) || (((adjustedYear % 100) == 0) && ((adjustedYear % 400) != 0)))
            return 0;
        return 1;
    }


    /**
     * Answer the nth week of the given month
     *
     * @param dayCount days from January 1
     * @param commonMonthIndex month 1-12
     * @return int
     */
    public static int weekOfMonth(int dayCount, int commonMonthIndex) {
        int daysIntoMonth = dayCount - FirstDayOfMonth[commonMonthIndex-1] + 1;
        return (daysIntoMonth / 7) + (((daysIntoMonth % 7) > 0) ? 1 : 0);
    }


    /**
     * Computes the number of days from (or until) January 1 of the year 1 A.D.
     * upto (or since) January 1 of a given year. Useful for comparison functions.
     *
     * @param gregorianYear gregorianYear
     */
    public static int absoluteDaysToYear(int gregorianYear) {
        int absDays = 0;
        int yearDelta, quadCenturies, centuries, quadYears, years;
        boolean isInADEra = (gregorianYear > 0);

        if(gregorianYear == 0)
            gregorianYear = -1; // no year zero
        if(isInADEra) {
            yearDelta = gregorianYear - 1;
        } else {
            yearDelta = - (gregorianYear + 1);
        }
        quadCenturies = yearDelta / 400;
        yearDelta = yearDelta % 400;
        centuries = yearDelta / 100;
        yearDelta = yearDelta % 100;
        quadYears = yearDelta / 4;
        years = yearDelta % 4;

        absDays = (quadCenturies * 146097) + 	// days per quad century
                (centuries * 36524) + 	// days per century
                (quadYears * 1461) +	// days per quad year
                (years * 365);			// days per year

        if(!isInADEra)	{
            absDays = absDays + 366;
            absDays = -absDays;
        }
        return absDays;
    }
}
