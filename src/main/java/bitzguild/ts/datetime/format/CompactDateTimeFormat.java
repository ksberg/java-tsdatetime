package bitzguild.ts.datetime.format;


import bitzguild.ts.datetime.*;

import java.text.ParseException;

public class CompactDateTimeFormat implements DateTimeFormat {


    protected DaysAndMonths _daysAndMonths;

    /**
     * Default Constructor
     */
    public CompactDateTimeFormat() {
        _daysAndMonths = new DaysAndMonthsForEnglish();
    }

    /**
     * DaysAndMonths Constructor. This class makes indirect use of
     * DaysAndMonths, meaning these are not included as part of the
     * parsing and rendering. However, a specialized DaysAndMonths
     * is useful for DateTime subclasses that print dayName or monthName.
     *
     * @param altDaysAndMonths rendering for days and months
     */
    public CompactDateTimeFormat(DaysAndMonths altDaysAndMonths) {
        _daysAndMonths = altDaysAndMonths;
    }

    
    /**
     * Parse string into MutableDateTime
     * TODO: Parse Time
     * 
     * @param datetime MutableDateTime receiver
     * @param datetimeString String to parse
     */
    public DateTime parseToDateTime(MutableDateTime datetime, String datetimeString) throws ParseException {
    	
    	String dateStr = null;
    	String timeStr = null;
        int idx = datetimeString.indexOf('.');
        if (idx < 0) {
        	dateStr = datetimeString;
        } else {
            dateStr = datetimeString.substring(0, idx);
            timeStr = datetimeString.substring(idx+1, datetimeString.length());
        }
    	
        int yyyymmdd = Integer.parseInt(dateStr);
        int year = yyyymmdd / 10000;
        int yearBalance = yyyymmdd % 10000;
        int month = yearBalance / 100;
        int day = yearBalance % 100;
        datetime.setFromYearMonthDay(year,month,day);
        
        if (timeStr != null) {
            int combo = Integer.parseInt(timeStr);
            int hms = combo / 1000;
            int ms = combo % 1000;
            int hm = hms / 100;
            int sec = hms % 100;
            int h = hm / 100;
            int m = hm % 100;
            datetime.setHoursMinutesSecondsMillis(h,m, sec, ms);
        }
        return datetime;
    }

    /**
     * Format given date time into buffer
     */
    public StringBuffer renderToBuffer(DateTime date, StringBuffer strb) {
        strb.append(date.year()*10000 + date.month()*100 + date.day());
        strb.append(".");
        int before = strb.length();
        strb.append(date.hours()*10000000 + date.minutes()*100000 + date.seconds()*1000 + date.millis());
        int len = strb.length() - before;
        if (len < 9) {
        	int balance = 9 - len;
        	while(balance-- > 0) strb.insert(before, '0');
        }
        return strb;
    }

    public String format() {
        return "YYYYMMDD";
    }

    public DaysAndMonths daysAndMonths() {
        return _daysAndMonths;
    }

}
