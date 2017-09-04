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

    private static SimpleDateFormat QR_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private static double BUILDING_RADIUS = 500;

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
        long orgId = Long.parseLong(tokens[1]);
        long buildingId = Long.parseLong(tokens[2]);

        Date qrDate = null;
        try {
            qrDate = QR_DATE_FORMAT.parse(tokens[3]);
        }
        catch (ParseException pe) {
            Log.e(LOG_TAG, "Invalid QR Code. Error parsing.", pe);
            return false;
        }

        if (!isBuildingFloorValid(orgId, buildingId)) {
            Log.d(LOG_TAG, "---------------------------Failed the authorization test!");
            return false;
        }
        Log.d(LOG_TAG, "---------------------------Passed the authorization test!");
        if (!inVicinityOfBuilding(buildingId)) {
            Log.d(LOG_TAG, "---------------------------Failed the latitude longitude test!");
            return false;
        }
        Log.d(LOG_TAG, "---------------------------Passed the latitude longitude test!");

        return true;
    }

    /**
     * Validates if orgId and buildingId are part of the authentication information
     * for the user.
     *
     * @param orgId
     * @param buildingId
     * @return
     */
    private boolean isBuildingFloorValid(long orgId, long buildingId) {

        List<Long> orgIdList = this.util.getOrganizationIdList();
        if (!orgIdList.contains(orgId))
            return false;

        List<Authorization> authList = this.util.getAuthorizationInfo(buildingId);
        if (authList.size() == 0)
            return false;
        else {
            latitude = Double.parseDouble(authList.get(0).getLatitude());
            longitude = Double.parseDouble(authList.get(0).getLongitude());
            return true;
        }
    }

    private boolean inVicinityOfBuilding(long buildingId) {

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
