package com.afba.imageplus.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.afba.imageplus.constants.ApplicationConstants;

@Component
public class DateHelper {
    public String getCurrentJulianDate() {
        return String.format("%ty", LocalDate.now()).concat(String.format("%03d", LocalDate.now().getDayOfYear()));
    }

    public String getCurrentJulianYearJulianDay() {
        String dayOfYear = String.format("%03d", LocalDate.now().getDayOfYear());
        return String.valueOf(LocalDate.now().getYear()) + dayOfYear;
    }

    public LocalTime getLocalTimeFromQuePriVal(Date timeLocal, String format) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(format);
        String time = timeFormat.format(timeLocal);
        return LocalTime.parse(time);
    }

    public LocalDate getLocalDateFromQuePriVal(Date dateLocal, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String date = dateFormat.format(dateLocal);
        return LocalDate.parse(date);
    }

    public Date getDateTimeFormatFromStringCombination(String datetime, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(datetime);
        } catch (ParseException e) {
            return null;
        }

    }

    public String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT);
        Date date = new Date();
        return formatter.format(date);
    }

    public boolean isDateValid(String date, String formate) {
        try {
            DateFormat df = new SimpleDateFormat(formate);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String addDaysInCurrentDate(int add, String unit) {
        if (unit.equals("D")) {
            return LocalDate.now().plusDays(add).toString();
        } else if (unit.equals("M")) {
            return LocalDate.now().plusMonths(add).toString();
        } else if (unit.equals("Y")) {
            return LocalDate.now().plusYears(add).toString();
        } else {
            return LocalDate.now().toString();
        }
    }

    public String subtractDaysInCurrentDate(int minus, String unit) {
        if (unit.equals("D")) {
            return LocalDate.now().minusDays(minus).toString();
        } else if (unit.equals("M")) {
            return LocalDate.now().minusMonths(minus).toString();
        } else if (unit.equals("Y")) {
            return LocalDate.now().minusYears(minus).toString();
        } else {
            return LocalDate.now().toString();
        }
    }

    public static Integer[] getAge(LocalDate birthDate, LocalDate currentDate) {
        if (birthDate.isBefore(currentDate)) {
            Period period = Period.between(birthDate, currentDate);
            return new Integer[] { period.getYears(), period.getMonths() };
        } else {
            return new Integer[] { 0, 0 };
        }
    }

    public String reformateDate(LocalDate date, String requiredFormate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(requiredFormate);
        return formatter.format(date);
    }

    public String reformateDate(LocalTime date, String requiredFormate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(requiredFormate);
        return formatter.format(date);
    }

    public Long getDuration(LocalDate starthDate, LocalDate endDate, String unit) {
        if (starthDate.isBefore(endDate)) {
            if (unit.equals("D")) {
                return ChronoUnit.DAYS.between(starthDate, endDate);
            } else if (unit.equals("M")) {
                return ChronoUnit.MONTHS.between(starthDate, endDate);
            } else if (unit.equals("Y")) {
                return ChronoUnit.YEARS.between(starthDate, endDate);
            } else {
                return 0L;
            }
        } else {
            return 0L;
        }

    }

    public static LocalDate getLastDateOfMonth(LocalDate date) {
        return date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
    }

    public static String localTimeToProvidedFormat(final String pattern, LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return time.format(formatter);
    }

    public static String localDateToProvidedFormat(DateTimeFormatter formatter, LocalDate date) {
        return date.format(formatter);
    }
}
