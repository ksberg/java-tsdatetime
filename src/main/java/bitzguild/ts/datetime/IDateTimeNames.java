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
 * The IDateTimeNames interface provides the String output for day and month names
 * and abbreviations.
 */
public interface IDateTimeNames {

    // ------------------------------------------------------------------------------
    // Names
    // ------------------------------------------------------------------------------


    /**
     * Answer the name for day of the week (e.g. Monday, Tuesday, Wednesday ...).
     * Month and Day names can be configured using custom DaysAndMonths class
     * and matching Parser/Renderer.
     *
     * @return String
     */
    public String dayName();


    /**
     * Answer the abbreviation day of the week (e.g. Mon, Tue, Wed ...).
     * Month and Day names can be configured using custom DaysAndMonths class
     * and matching Parser/Renderer.
     *
     * @return String
     */
    public String dayAbbreviation();


    /**
     * Answer the month name (e.g. January, February, March ...).
     * Month and Day names can be configured using custom DaysAndMonths class
     * and matching Parser/Renderer.
     *
     * @return String
     */
    public String monthName();

    /**
     * Answer the month abbreviation (e.g. Jan, Feb, Mar ...).
     * Month and Day names can be configured using custom DaysAndMonths class
     * and matching Parser/Renderer.
     *
     * @return String
     */
    public String monthAbbreviation();

	
}
