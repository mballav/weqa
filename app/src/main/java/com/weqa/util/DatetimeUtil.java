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

    public static Date getDateFromGMT(String GMTString) {
        try {
            String firstPart = GMTString.substring(0, 19);
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(firstPart);
            return date;
        }
        catch (ParseException pe) {
            Log.d(LOG_TAG, "Error parsing GMT Date");
        }
        return null;
    }

    public static String getLocalDate(String GMTString) {
        try {
            String firstPart = GMTString.substring(0, 19);
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(firstPart);

            // Potentially use the default locale. This will use the local time zone already.
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            outputFormat.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
            String outputText = outputFormat.format(date);
            return outputText;
        }
        catch (ParseException pe) {
            Log.d(LOG_TAG, "Error parsing GMT Date");
        }
        return "";
    }

    public static String getLocalDateTime(String GMTString) {
        try {
            String firstPart = GMTString.substring(0, 19);
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(firstPart);

            // Potentially use the default locale. This will use the local time zone already.
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            outputFormat.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
            String outputText = outputFormat.format(date);
            return outputText;
        }
        catch (ParseException pe) {
            Log.d(LOG_TAG, "Error parsing GMT Date");
        }
        return "";
    }

    public static boolean isDateExpired(String GMTString) {
        try {
            String firstPart = GMTString.substring(0, 19);
            Log.d(LOG_TAG, "First part of the date STRING is " + firstPart);
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(firstPart);

            Date currentDate = new Date();

            if (currentDate.before(date)) return false;
        }
        catch (ParseException pe) {
            Log.d(LOG_TAG, "Error parsing GMT Date");
        }
        return true;
    }

    public static String getTimeDifference(String GMTString) {
        try {
            String firstPart = GMTString.substring(0, 19);
            Log.d(LOG_TAG, "First part of the date STRING is " + firstPart);
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(firstPart);

            Date currentDate = new Date();

            long diff = date.getTime() - currentDate.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);

            String outputText = diffHours + " hours";
            if (diffMinutes >= 1) {
                outputText += " and " + diffMinutes + " minutes";
            }

            return outputText;
        }
        catch (ParseException pe) {
            Log.d(LOG_TAG, "Error parsing GMT Date");
        }
        return "";
    }
}
