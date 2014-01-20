package bitzguild.ts.datetime.format;

import bitzguild.ts.datetime.DateTime;
import bitzguild.ts.datetime.DaysAndMonths;
import bitzguild.ts.datetime.DateTimeFormat;
import bitzguild.ts.datetime.MutableDateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * This class enables use of any existing Java Date format to
 * parse or render the underlying PSC Date. Methods do require
 * conversion between
 */
public class JavaDateParserRenderer implements DateTimeFormat {

    protected DaysAndMonths _daysAndMonths;
    protected DateFormat    _javaDateFormat;

    /**
     * Default Constructor
     */
    public JavaDateParserRenderer() {
        _daysAndMonths = new DaysAndMonthsForEnglish();
    }

    /**
     * DateFormat Constructor.
     *
     * @param df Java DateFormat
     */
    public JavaDateParserRenderer(DateFormat df) {
        _javaDateFormat = df;
    }

    public DateTime parseToDateTime(MutableDateTime date, String dateString) throws ParseException {
        Date d = _javaDateFormat.parse(dateString);
        date.setFromJavaDate(d);
        return date;
    }

    public StringBuffer renderToBuffer(DateTime date, StringBuffer strb) {
        Date d = date.toJavaDate();
        strb.append(_javaDateFormat.format(d));
        return strb;
    }

    public String format() {
        return _javaDateFormat.toString();
    }

    public DaysAndMonths daysAndMonths() {
        return _daysAndMonths;
    }

}
