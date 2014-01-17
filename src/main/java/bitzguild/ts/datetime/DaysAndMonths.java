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

public interface DaysAndMonths {

    public static final int JANUARY     = 1;
    public static final int FEBRUARY    = 2;
    public static final int MARCH       = 3;
    public static final int APRIL       = 4;
    public static final int MAY         = 5;
    public static final int JUNE        = 6;
    public static final int JULY        = 7;
    public static final int AUGUST      = 8;
    public static final int SEPTEMBER   = 9;
    public static final int OCTOBER     = 10;
    public static final int NOVEMBER    = 11;
    public static final int DECEMBER    = 12;

    public static final int MONDAY      = 0;
    public static final int TUESDAY     = 1;
    public static final int WEDNESDAY   = 2;
    public static final int THURSDAY    = 3;
    public static final int FRIDAY      = 4;
    public static final int SATURDAY    = 5;
    public static final int SUNDAY      = 6;


    /*
    * Answer the index in a week, 0-6, of the _dayOfYear named dayName.
    * Returns -1 if no such _dayOfYear exists.
    * @param String name of _dayOfYear (full name in English)
    */
    public int dayIndexForName(String dayName);

    public String dayName(int dayIndex);

    public String dayAbbreviation(int dayIndex);

    /**
     * Answer the common month index (1-12) of the month matching the given name.
     * Return -1 if name not found.
     * @param monthName name of month
     * @return int common-use month index (1=January, 12=December)
     */
    public int monthIndexForName(String monthName);

    /**
     * The name of month for the common-use month index (1-12).
     *
     * @param commonMonthIndex (1=January, 12=December)
     * @param doFull or abbreviated
     * @return String name of month
     */
    public String monthName(int commonMonthIndex, boolean doFull);

    public String monthAbbreviation(int commonMonthIndex);

}
