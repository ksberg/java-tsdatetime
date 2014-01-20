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

/**
 * <p>
 * The DateTimeFormat interface provides DateTime parsing and rendering API.
 * It also includes a format descriptor and DaysAndMonths object that matches
 * the input/output.
 * </p>
 * <p>
 * The parsing is designed to work with MutableDateTime as a base type, to
 * enable minimal object creation. The decision to propagate or encapsulate
 * the mutable state is up to the consumer.
 * </p>
 * <p>
 * The format is designed to work with StringBuffer to avoid String creation
 * where possible. StringBuffer is the only Java buffer to support number output
 * rendering (e.g. DecimalFormat), and as of Java 1.7, the  StringBuffer/Format
 * combination is significantly faster than StringBuilder/String format.
 * </p>
 */
public interface DateTimeFormat {

    /**
     *
     * @param dt receiving datetime
     * @param dtString string to parse
     */
    DateTime parseToDateTime(MutableDateTime dt, String dtString) throws ParseException;

    /**
     *
     * @param date
     * @param sb
     */
    StringBuffer renderToBuffer(DateTime date, StringBuffer sb);

    /**
     *
     * @return
     */
    String format();


    /**
     *
     * @return
     */
    DaysAndMonths daysAndMonths();
}
