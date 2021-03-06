package it.fmistri.dontdieplease.util;

import android.util.Pair;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Util static methods for date manipulations.
 */
public class DateUtil {
    /**
     * Calculate the {@link Date} range for a specific day of the month. This range will be used
     * by the DAO to select all the reports in that specific range of milliseconds.
     * @param year The gregorian year.
     * @param month The gregorian month(0 based).
     * @param day The gregorian day of the month.
     * @return A pair containing the start date and the end date defining a day-long range.
     */
    public static Pair<Date, Date> getDayRange(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
        Date start = calendar.getTime();

        // Calculate the end date by adding one day and subtracting one millisecond
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        Date end = calendar.getTime();

        return new Pair<>(start, end);
    }

    /**
     * Retrieve the {@link Date} range for the last week before the given day.
     * @param year The gregorian year.
     * @param month The gregorian month.
     * @param day The gregorian day.
     * @return A pair containing the start date and the end date defining a week-long range.
     */
    public static Pair<Date, Date> getWeekRange(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date end = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();

        return new Pair<>(start, end);
    }
}
