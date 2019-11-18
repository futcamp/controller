///////////////////////////////////////////////////////////////////////
//
// Future Camp Project
//
// Copyright(C) 2019 Sergey Denisov.
//
// Written by Sergey Denisov aka LittleBuster(DenisovS21@gmail.com)
// Github:  https://github.com/LittleBuster
//          https://github.com/futcamp
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public Licence 3
// as published by the Free Software Foundation; either version 3
// of the Licence, or(at your option) any later version.
//
///////////////////////////////////////////////////////////////////////

package ru.futcamp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Time control functions
 */
public class TimeControl {
    /**
     * Set default timezone
     * @param zone Timezone
     */
    public static void setTimeZone(String zone) {
        TimeZone.setDefault(TimeZone.getTimeZone(zone));
    }

    /**
     * Get current hour
     * @return Current hour
     */
    public static int getCurHour() {
        Date date = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat("HH");
        return Integer.parseInt(dtf.format(date));
    }

    /**
     * Get current date as string
     * @return Date
     */
    public static String getCurDate() {
        Date date = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat("dd.MM.20YY");
        return dtf.format(date);
    }

    /**
     * Get previous date as string
     * @return Previous date
     */
    public static String getPrevDate() {
        Date date = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat("dd.MM.20YY");

        String strDate = dtf.format(date);
        String[] parts = strDate.split("\\.");
        int day = Integer.parseInt(parts[0]);

        if (day != 1) {
            day--;
        } else {
            return "";
        }

        String out = day + "." + parts[1] + "." + parts[2];
        if (day < 10)
            out = "0" + out;
        return out;
    }
}
