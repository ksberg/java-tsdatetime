package bitzguild.ts.datetime;

import bitzguild.ts.datetime.DateTime;

/**
 * DateTimeSetters provide a modification API that can be used by both
 * mutable and immutable implementations, but with different effect.
 */
public interface DateTimeSetters {

    public DateTime withYear(int year);
    public DateTime withYearMonthDay(int year, int month, int day);
    public DateTime withHoursMinutesSeconds(int hours, int minutes, int seconds);
    public DateTime withHoursMinutesSecondsMillis(int hours, int minutes, int seconds, int millis);
    public DateTime withMillisSinceMidnight(int millis);

    public DateTime withRep(long reo);
    public DateTime withJavaDate(java.util.Date jDate);
    public DateTime withYYYYMMDD(int yyyymmdd);

}
