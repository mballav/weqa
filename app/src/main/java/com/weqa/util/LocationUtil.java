package com.weqa.util;

import android.location.Location;

/**
 * Created by Manish Ballav on 8/7/2017.
 */

public class LocationUtil {

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {

        Location startPoint = new Location("locationA");
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(lon1);

        Location endPoint = new Location("locationB");
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(lon2);

        return startPoint.distanceTo(endPoint);
    }

    public static void main(String[] args) {
        System.out.println(getDistance(50.3, -5.1, 58.4, -3.2));
    }
}
