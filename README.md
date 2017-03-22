java-tsdatetime
===============

Performant Date replacement for Java with added manipulation and query methods


# Benchmark Results

```
                Test   BitzGuild        Joda        Java        Factor

            Creation          29         219         527        18.092
              Format         269         848         188         4.491
               Parse         111        1703        7438        67.006
              Serial          12          24           5         4.465
     Date Comparison          10           7           5         1.754
  Get Year/Month/Day          17          65         315        18.209
   Iterate by Minute          21          40           8         4.776
      Iterate by Day          12         111          13         9.146
     Iterate by Year          10         689         621        63.538
  Iterate by Biz Day          97         137         461         4.750

```

# Overview

Bitzguild DateTime originated in Smalltalk, pre-dating Java and Joda time classes. 
The original intent was to encapsulate the type of calendar manipulations required
to calculate futures expiration and other support for trading time series. The 
Java classes were designed to minimize object overhead by promoting use of the 
the serialized long equivalent. Time and date are treated as independent integers
so that time-of-day comparisons and filtering are consistent and fast. Time is
represented as an offset from midnight, so 8:00 trading day, or trading time
ranges are simple integer math.

# Business Calendar Computations

These are common computations used in the expiration of futures and options contracts.

```
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
```
