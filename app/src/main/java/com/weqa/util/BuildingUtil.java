package com.weqa.util;

import android.content.Context;
import android.util.Log;

import com.weqa.model.Authorization;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Manish Ballav on 8/31/2017.
 */

public class BuildingUtil {

    private String LOG_TAG;
    private SharedPreferencesUtil util;
    private Context context;

    public BuildingUtil(String logTag, SharedPreferencesUtil util, Context context) {
        this.LOG_TAG = logTag;
        this.util = util;
        this.context = context;
    }

    public Authorization getBuildingForSearchBar(List<Authorization> authList) {
        Authorization nearestBuilding = findNearestBuilding(authList);
        if (nearestBuilding != null) {
            return nearestBuilding;
        }
        else {
            Log.d(LOG_TAG, "Nearest Building Search returned NULL!");
            Authorization latestBookedBuilding = util.getLatestBookedBuilding();
            if (latestBookedBuilding != null) {
                return latestBookedBuilding;
            }
            else {
                Log.d(LOG_TAG, "Latest Booked Building Search returned NULL!");
                Authorization firstBuilding = getFirstSortedBuilding(authList);
                if (firstBuilding != null) {
                    return firstBuilding;
                }
            }
        }
        return null;
    }

    private Authorization getFirstSortedBuilding(List<Authorization> authList) {
        if (authList.size() == 0)
            return null;
        else {
            Authorization[] authArray = authList.toArray(new Authorization[authList.size()]);
            Arrays.sort(authArray, Authorization.BuildingFloorNameComparator);
            Log.d(LOG_TAG, "" + authArray[0]);
            return authArray[0];
        }
    }

    private Authorization findNearestBuilding(List<Authorization> authList) {
        double geofenceRadius = util.getGeofenceRadius();

        Log.d(LOG_TAG, "Geofence Radius: " + geofenceRadius);

        Authorization nearestBuilding = null;
        LocationTracker tracker = new LocationTracker(context);
        // check if location is available
        if (tracker.isLocationEnabled()) {
            double lat1 = tracker.getLatitude();
            double lon1 = tracker.getLongitude();
            double nearestDistance = geofenceRadius + 1;
            for (Authorization authInfo : authList) {
                double lat2 = Double.parseDouble(authInfo.getLatitude());
                double lon2 = Double.parseDouble(authInfo.getLongitude());
                double distance = LocationUtil.getDistance(lat1, lon1, lat2, lon2);
                Log.d(LOG_TAG, "Current Location(" + lat1 + "," + lon1 + "), Building (" + lat2 + "," + lon2 + ")");
                Log.d(LOG_TAG, "DISTANCE: " + distance);
                if (distance <= geofenceRadius) {
                    if (distance < nearestDistance) {
                        nearestBuilding = authInfo;
                        nearestDistance = distance;
                    }
                }
            }
        }
        else {
            // show dialog box to user to enable location
            tracker.askToOnLocation();
        }
        if (nearestBuilding != null)
            return nearestBuilding;
        else
            return null;
    }
}
