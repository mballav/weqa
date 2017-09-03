package com.weqa.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Manish Ballav on 9/3/2017.
 */

public class DatetimeUtil {

    private static final String LOG_TAG = "WEQA-LOG";

    public static String getLocalDateTime(String GMTString) {
        try {
            String firstPart = GMTString.substring(0, 19);
            Log.d(LOG_TAG, "First part of the date STRING is " + firstPart);
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(firstPart);

            // Potentially use the default locale. This will use the local time zone already.
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            outputFormat.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
            String outputText = outputFormat.format(date);
            Log.d(LOG_TAG, "FORMATTED DATE-------------------------------- is " + outputText);
            return outputText;
        }
        catch (ParseException pe) {
            Log.d(LOG_TAG, "Error parsing GMT Date");
        }
        return "";
    }
}
