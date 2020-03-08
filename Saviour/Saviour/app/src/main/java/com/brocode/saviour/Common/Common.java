package com.brocode.saviour.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    public static final String APP_ID = "060a8fcd59fc8e8002e17bc9968bfebb";
    public static Location current_location = null;

    public static String convertUnixToDate(long unixSeconds) {
        Date date = new java.util.Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm EEE dd-MM-YYYY");
//        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        return sdf.format(date);
    }

    public static String convertUnixToHours(long sunrise) {
        Date date = new java.util.Date(sunrise*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
//        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        return sdf.format(date);
    }
}
