package me.lofro.core.paper.utils.date;

import java.time.LocalDate;

public class Date {

    public static String getDateForDayZero() {
        LocalDate nowDay = LocalDate.now();

        int month = nowDay.getMonthValue();
        int day = nowDay.getDayOfMonth();

        String s = nowDay.getYear() + "-";

        if (month < 10) s += "0";
        s += month + "-";

        if (day < 10) s += "0";
        s += day;

        return s;
    }

}
