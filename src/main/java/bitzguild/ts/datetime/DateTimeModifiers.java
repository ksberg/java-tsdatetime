package bitzguild.ts.datetime;

/**
 * Created by ksvenberg on 1/23/14.
 */
public interface DateTimeModifiers {

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addMillis(int amount);

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addSeconds(int amount);

    /**
     *
     * @param amount
     *
     * @return
     */
    public DateTime addMinutes(int amount);

    /**
     *
     * @param amount
     * @return
     */
    public DateTime addHours(int amount);


    /**
     *
     * @param numDays
     * @return
     */
    public DateTime addDays(int numDays);


    /**
     *
     * @param numDays
     * @return
     */
    public DateTime addBusinessDays(int numDays, DateTimePredicate holidays);


    /**
     *
     * @param numWeeks
     * @return
     */
    public DateTime addWeeks(int numWeeks);


    /**
     *
     * @param numberOfYears
     * @return
     */
    public DateTime addYears(int numberOfYears);


    /**
     *
     * @param dayOfWeek
     * @return
     */
    public DateTime rollbackToDayOfWeek(int dayOfWeek);


    /**
     *
     * @param num
     * @return
     */
    public DateTime rollMonths(int num);


    /**
     *
     * @param dayOfWeek
     * @return
     */
    public DateTime rollToDayOfWeek(int dayOfWeek);


    /**
     *
     * @return
     */
    public DateTime nextBusinessDay(DateTimePredicate holidays);


    /**
     *
     * @return
     */
    public DateTime nextWeekday();


    /**
     *
     * @return
     */
    public DateTime nextWeek();


    /**
     *
     * @return
     */
    public DateTime nextMonth();


    /**
     *
     * @return
     */
    public DateTime nextQuarter();


    /**
     *
     * @return
     */
    public DateTime nextYear();


    /**
     *
     * @param numYears
     * @return
     */
    public DateTime rollYears(int numYears);


    /**
     *
     * @return
     */
    public DateTime priorWeekday();


    /**
     *
     * @return
     */
    public DateTime priorBusinessDay(DateTimePredicate holidays);

}
