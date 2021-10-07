package com.co2nsensus.co2mission.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtil {

    public static int getCurrentYear() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getYear();
    }

    public static int getCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getMonthValue();
    }

    public static int getYearOfLocalDate(LocalDateTime localDate) {
        return localDate.getYear();
    }

    public static int getMonthOfLocalDate(LocalDateTime localDate) {
        return localDate.getMonthValue();
    }
}
