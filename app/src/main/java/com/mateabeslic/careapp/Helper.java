package com.mateabeslic.careapp;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class Helper {

    public static Date generateDate(int year, int month, int day) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay().toInstant(ZoneOffset.UTC));
    }

}
