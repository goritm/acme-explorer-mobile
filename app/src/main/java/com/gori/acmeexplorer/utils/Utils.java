package com.gori.acmeexplorer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static final String LOGGER_NAME = "acme-explorer-logs";

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat().format(date);
    }

    public static String twoDigits(int n) {
        return (n < 10) ? ("0" + n) : String.valueOf(n);
    }
}
