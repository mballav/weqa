package com.weqa.util;

import com.weqa.model.Authorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Manish Ballav on 8/31/2017.
 */

public class AuthorizationUtil {

    public static List<Authorization> removeDupliateBuildings(List<Authorization> aList) {
        List<Authorization> bList = new ArrayList<Authorization>();
        List<String> dlist = new ArrayList<String>();

        Authorization[] authArray = aList.toArray(new Authorization[aList.size()]);
        Arrays.sort(authArray, Authorization.BuildingFloorNameComparator);

        for (Authorization a : authArray) {
            if (dlist.indexOf(a.getBuildingName() + ", " + a.getAddress()) == -1) {
                bList.add(a);
                dlist.add(a.getBuildingName() + ", " + a.getAddress());
            }
        }
        return bList;
    }

    public static String getBuildingDisplayName(Authorization a) {
        String bName = a.getBuildingName() + ", " + a.getAddress();
        if (bName.length() > 30) bName = bName.substring(0, 30) + "...";
        return bName;
    }
}
