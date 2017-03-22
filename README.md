java-tsdatetime
===============

Performant Date replacement for Java with added manipulation and query methods


BENCHMARK RESULTS

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
