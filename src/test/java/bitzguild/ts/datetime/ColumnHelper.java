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

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;

public class ColumnHelper {

    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;
    public static final int DEFAULT_ALIGNMENT = ALIGN_RIGHT;

    protected static final String DefaultLabel = "Label";
    public static DecimalFormat DefaultFormat = new DecimalFormat("#.000");
    private static FieldPosition _FieldPos = new FieldPosition(NumberFormat.FRACTION_FIELD);

    protected String        _name;
    protected int           _columnWidth;
    protected int           _columnAlignment;

    private DecimalFormat   _numFormat;



    /**
     * Default Constructor
     */
    public ColumnHelper() {
        super();
        _columnWidth = 20;
        _columnAlignment = DEFAULT_ALIGNMENT;
        _name = DefaultLabel;
        setDecimalFormat("#.000");
    }

    /**
     * Constructor with label name
     */
    public ColumnHelper(String name, int width) {
        super();
        _columnWidth = Math.max(5, Math.abs(width));
        _columnAlignment = DEFAULT_ALIGNMENT;
        _name = name;
        setDecimalFormat("#.000");
    }

    public void setColumnWidth(int w) { _columnWidth = Math.abs(w); }

    public void setDecimalFormat(String strFormat) {
        _numFormat =  new DecimalFormat(strFormat);
    }


    /**
     * Access for setting content alignment, which defaults
     * to right-aligned to support numeric rendering. Input
     * is pinned to permissible values.
     *
     * @param a alignment
     */
    public void setAlignment(int a) {
        switch(a) {
            case ALIGN_LEFT:
                _columnAlignment = ALIGN_LEFT;
                break;
            case ALIGN_RIGHT:
                _columnAlignment = ALIGN_RIGHT;
                break;
            case ALIGN_CENTER:
                _columnAlignment = ALIGN_CENTER;
                break;
            default:
                _columnAlignment = ALIGN_RIGHT;
        }
    }

    /**
     *
     * @param strb
     * @return
     */
    public StringBuffer renderLabel(StringBuffer strb) {
        return renderString(strb, _name);
    }

    /**
     *
     * @param strb
     * @param str
     * @return
     */
    public StringBuffer renderString(StringBuffer strb, String str) {
        int iStart = strb.length();
        strb.append(str);
        return alignValueInColumn(strb, iStart);
    }

    /**
     *
     * @param strb
     * @param value
     * @return
     */
    public StringBuffer renderDouble(StringBuffer strb, double value) {
        int iStart = strb.length();
        _numFormat.format(value, strb, _FieldPos);
        return alignValueInColumn(strb, iStart);
    }

    /**
     *
     * @param strb
     * @param value
     * @return
     */
    public StringBuffer renderLong(StringBuffer strb, long value) {
        int iStart = strb.length();
        strb.append(value);
        return alignValueInColumn(strb, iStart);
    }

    /**
     *
     * @param strb
     * @param iStart
     * @return
     */
    private StringBuffer alignValueInColumn(StringBuffer strb, int iStart) {
        int iEnd = strb.length();
        int width = iEnd - iStart;
        int ipad = _columnWidth -(iEnd - iStart);
        if (width <= _columnWidth) {
            pad(ipad, strb, iStart,iEnd, _columnAlignment);
        } else {
            strb.setLength(iStart + _columnWidth - 3);
            strb.append("...");
        }
        return strb;
    }

    /**
     * <p>
     * The generic finishing function for apply() methods. Invokes wrap
     * and pads the results. Will do left, right, and center alignments.
     * </p>
     *
     * @param ipad int number of spaces to pad
     * @param strb StringBuffer output buffer
     * @param iStart int starting buffer index of element to pad
     * @param iEnd int ending buffer index of element to pad
     * @param alignment
     */
    private void pad(int ipad, StringBuffer strb, int iStart, int iEnd, int alignment) {

        int iEndP = iEnd ;
        int iPadP = ipad;

        if (iPadP < 1) return;

        if (alignment != ALIGN_CENTER) {

            int location = (alignment > ALIGN_LEFT) ? iStart : iEndP;

            char[] chrs = new char[iPadP];
            for(int ich=0; ich<iPadP; ich++) chrs[ich] = ' ';
            strb.insert(location,chrs);

        } else {

            int ich;
            char[] chrs;
            int ipadR = iPadP / 2;
            int ipadL = iPadP - ipadR;

            chrs = new char[ipadL];
            for(ich=0; ich<ipadL; ich++) chrs[ich] = ' ';
            strb.insert(iEndP,chrs);

            chrs = new char[ipadR];
            for(ich=0; ich<ipadR; ich++) chrs[ich] = ' ';
            strb.insert(iStart,chrs);

        }
    }


}
