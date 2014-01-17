package bitzguild.ts.datetime.format;


import bitzguild.ts.datetime.DaysAndMonths;

public class DaysAndMonthsForSpanish implements DaysAndMonths {

    public static final String[]   MonthNamesFull  	= { "enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre" };
    public static final String[]   MonthNamesAbbr 	= { "enero", "feb", "marzo", "abr", "mayo", "jun", "jul", "agosto", "set", "oct", "nov", "dic" };

    public static final String[]   DayNamesFull = { "lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo" };
    public static final String[]   DayNamesAbbr = { "lu", "ma", "mi", "ju", "vi", "sa", "do" };

    /*
    * Answer the index in a week, 0-6, of the _dayOfYear named dayName.
    * Returns -1 if no such _dayOfYear exists.
    * @param String name of _dayOfYear (full name in English)
    */
    public int dayIndexForName(String dayName) {
        if(dayName != null) {
            for(int i=0; i<7; i++) {
                if(DayNamesFull[i].equals(dayName))
                    return i;
            }
        }
        return -1;
    }

    public String dayName(int dayIndex) {
        return DayNamesFull[dayIndex];
    }

    /**
     *
     * @param dayIndex
     * @return
     */
    public String dayAbbreviation(int dayIndex) {
        return DayNamesAbbr[dayIndex];
    }


    /**
     * Answer the common month index (1-12) of the month matching the given name.
     * Return -1 if name not found.
     * @param monthName name of month
     * @return int common-use month index (1=January, 12=December)
     */
    public int monthIndexForName(String monthName) {
        if(monthName != null) {
            for(int i=0; i<12; i++) {
                if(MonthNamesFull[i].equals(monthName)) return i+1;
            }
        }
        return -1;
    }

    /**
     * The name of month for the common-use month index (1-12).
     *
     * @param commonMonthIndex (1=January, 12=December)
     * @param doFull or abbreviated
     * @return String name of month
     */
    public String monthName(int commonMonthIndex, boolean doFull) {
        return doFull ? MonthNamesFull[commonMonthIndex-1] : MonthNamesAbbr[commonMonthIndex-1];
    }

    /**
     * The month abbreviation for the common use month index (1-12).
     *
     * @param commonMonthIndex int
     * @return String abbreviation
     */
    public String monthAbbreviation(int commonMonthIndex) {
        return MonthNamesAbbr[commonMonthIndex-1];
    }
}
