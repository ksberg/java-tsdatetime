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
 * DateTimeBusiness interface provides date calculations based on
 * work-week as defined by a holiday function (DateTimePredicate).
 * DateTime implementation supports a static default Holidays
 * predicate which can be replaced by a custom DateTimePredicate.
 * The default addresses many regular trading market holidays.
 */
public interface DateTimeBusiness {

    /**
     * Answer sequential business day of month starting at 1.
     * This answer depends on holiday configuration.
     *
     * @return int
     */
    public int businessDayOfMonth(DateTimePredicate holidays);

    public DateTime addBusinessDays(int numDays, DateTimePredicate holidays);
    public DateTime nextBusinessDay(DateTimePredicate holidays);
    public DateTime priorBusinessDay(DateTimePredicate holidays);

    public DateTimeIterator businessDaysInMonth(DateTimePredicate holidays);
    public DateTimeIterator businessDaysInMonthBefore(DateTime before, DateTimePredicate holidays);
    public DateTimeIterator businessDaysBefore(DateTime before, DateTimePredicate holidays);
    public DateTimeIterator businessDaysBeforeNthWeekday(DateTime before, int nth, int weekdayIndex, DateTimePredicate holidays);
    
    public DateTime nthBusinessDayOfMonth(int n, DateTimePredicate holidays);
    public DateTime nthWeekdayOfMonth(int nth, int dayOfWeek, DateTimePredicate holidays);
    public DateTime nthBusinessWeekdayOfMonth(int nth, int dayOfWeek, DateTimePredicate holidays);
    public DateTime lastNthBusinessDayOfMonth(int nth, DateTimePredicate holidays);
    public DateTime lastNthBusinessDayOfMonthBefore(int nth, DateTime before, DateTimePredicate holidays);
    public DateTime lastNthBusinessDayOfMonthBeforeIthWeekday(int nth, int ith, int wkday, DateTimePredicate holidays);
    
}
