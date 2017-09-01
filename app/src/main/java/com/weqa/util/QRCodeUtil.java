package com.weqa.util;

import android.content.Context;
import android.util.Log;

import com.weqa.model.Authorization;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Manish Ballav on 8/31/2017.
 */

public class QRCodeUtil {

    private static String LOG_TAG = "WEQA-LOG";

    private static SimpleDateFormat QR_DATE_FORMAT = new SimpleDateFormat("yyyyMMddhh:mm:ss");

    private static double BUILDING_RADIUS = 100;

    private SharedPreferencesUtil util;
    private Context context;
    private double latitude, longitude;

    public QRCodeUtil(SharedPreferencesUtil util, Context context) {
        this.util = util;
        this.context = context;
    }

    public boolean isQRCodeValid(String qrCode) {
        String[] tokens = qrCode.split(",");

        if (tokens.length < 4)
            return false;

        String codeType = tokens[0];
        long buildingId = Long.parseLong(tokens[1]);
        long floorplanId = Long.parseLong(tokens[2]);

        Date qrDate = null;
        try {
            qrDate = QR_DATE_FORMAT.parse(tokens[3]);
        }
        catch (ParseException pe) {
            Log.e(LOG_TAG, "Invalid QR Code. Error parsing.", pe);
            return false;
        }

        if (!isBuildingFloorValid(buildingId, floorplanId))
            return false;
        if (!inVicinityOfBuilding(buildingId, floorplanId))
            return false;

        return true;
    }

    private boolean isBuildingFloorValid(long buildingId, long floorplanId) {
        List<Authorization> authList = this.util.getAuthorizationInfo(buildingId);
        boolean floorFound = false;
        for (Authorization a : authList) {
            if (Long.parseLong(a.getFloorPlanId()) == floorplanId) {
                floorFound = true;
                latitude = Double.parseDouble(a.getLatitude());
                longitude = Double.parseDouble(a.getLongitude());
                break;
            }
        }
        return floorFound;
    }

    private boolean inVicinityOfBuilding(long buildingId, long floorplanId) {

        LocationTracker tracker = new LocationTracker(context);
        // check if location is available
        if (tracker.isLocationEnabled()) {
            double lat1 = tracker.getLatitude();
            double lon1 = tracker.getLongitude();

            double distance = LocationUtil.getDistance(lat1, lon1, latitude, longitude);
            Log.d(LOG_TAG, "Current Location(" + lat1 + "," + lon1 + "), Building (" + latitude + "," + longitude + ")");
            Log.d(LOG_TAG, "DISTANCE: " + distance);
            if (distance <= BUILDING_RADIUS) {
                return true;
            }
        }
        return false;
    }
}
