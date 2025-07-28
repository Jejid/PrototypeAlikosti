package com.example.utility;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    public static boolean isValidFormat(String dateStr) {
        try {
            YearMonth.parse(dateStr, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isExpired(String dateStr) {
        try {
            YearMonth input = YearMonth.parse(dateStr, FORMATTER);
            YearMonth current = YearMonth.now();
            return input.isBefore(current);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

