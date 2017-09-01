package com.weqa.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weqa.model.AuthResponse;
import com.weqa.model.Authentication;
import com.weqa.model.Authorization;
import com.weqa.model.Availability;
import com.weqa.model.Configuration;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 8/1/2017.
 */

public class SharedPreferencesUtil {

    SharedPreferences spAuthentication;
    SharedPreferences spAuthorization;
    SharedPreferences spConfig;
    SharedPreferences spHistory;
    SharedPreferences spAvail;

    final static String AUTHENTICATION_FILENAME = "AuthenticationInfo";
    final static String AUTHORIZATION_FILENAME = "AuthorizationInfo";
    final static String CONFIG_FILENAME = "Configuration";
    final static String HISTORY_FILENAME = "History";
    final static String AVAILABILITY_FILENAME = "Availability";

    final static String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

    final static String NO_SPACE_ON_DEVICE = "NoSpaceOnDevice";

    private Context context;
    private String logTag;

    public SharedPreferencesUtil(Context context, String logTag) {
        this.context = context;
        this.logTag = logTag;
    }

    public SharedPreferencesUtil(Context context) {
        this.context = context;
        this.logTag = "YEZLO-LOG";
    }

    public Authorization getLatestBookedBuilding() {
        // TODO
        return null;
    }

    private void putString(SharedPreferences sp, String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void removeKey(SharedPreferences sp, String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public double getGeofenceRadius() {
        spConfig = context.getSharedPreferences(CONFIG_FILENAME, Context.MODE_PRIVATE);
        return (double) spConfig.getInt(Configuration.GEO_FENCE, 0);
    }

    public void setNoSpaceOnDevice(boolean noSpaceOnDevice) {
        spConfig = context.getSharedPreferences(CONFIG_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spConfig.edit();
        editor.putBoolean(NO_SPACE_ON_DEVICE, noSpaceOnDevice);
        editor.commit();
    }

    public boolean getNoSpaceOnDevice() {
        spConfig = context.getSharedPreferences(CONFIG_FILENAME, Context.MODE_PRIVATE);
        return spConfig.getBoolean(NO_SPACE_ON_DEVICE, false);
    }

    public void addAvailability(List<Availability> availList) {
        spAvail = context.getSharedPreferences(AVAILABILITY_FILENAME, Context.MODE_PRIVATE);
        spAvail.edit().clear().commit();
        Log.d(logTag, "CLEARED FILE: " + AVAILABILITY_FILENAME);
        Log.d(logTag, "NOW GOING to add availability information....");
        SharedPreferences.Editor editor = spAvail.edit();
        for (Availability a : availList) {
            editor.putInt(a.getBuildingId() + "-" + a.getItemType() + "-" + a.getItemTypeId(), a.getItemCount());
        }
        editor.commit();
        Log.d(logTag, "                                         .... availability information added");
    }

    public List<Availability> getAvailability(int buildingId) {
        spAvail = context.getSharedPreferences(AVAILABILITY_FILENAME, Context.MODE_PRIVATE);
        String key = "" + buildingId;
        Map<String, ?> keyValueMap = spAvail.getAll();
        List<Availability> aList = new ArrayList<Availability>();
        for (String k : keyValueMap.keySet()) {
            if (k.contains(key)) {
                Availability a = new Availability();
                String[] tokens = k.split("-");
                a.setItemType(tokens[1]);
                a.setItemTypeId(Integer.parseInt(tokens[2]));
                a.setItemCount((Integer)keyValueMap.get(k));
                aList.add(a);
            }
        }
        return aList;
    }

    public List<Availability> getAvailability() {
        spAvail = context.getSharedPreferences(AVAILABILITY_FILENAME, Context.MODE_PRIVATE);
        Map<String, ?> keyValueMap = spAvail.getAll();
        List<Availability> aList = new ArrayList<Availability>();
        for (String k : keyValueMap.keySet()) {
            Availability a = new Availability();
            String[] tokens = k.split("-");
            a.setItemType(tokens[1]);
            a.setItemTypeId(Integer.parseInt(tokens[2]));
            a.setItemCount((Integer)keyValueMap.get(k));
            aList.add(a);
        }
        return aList;
    }

    public void addAuthTokens(AuthResponse token) {
        spAuthentication = context.getSharedPreferences(AUTHENTICATION_FILENAME, Context.MODE_PRIVATE);
        spAuthorization = context.getSharedPreferences(AUTHORIZATION_FILENAME, Context.MODE_PRIVATE);
        spConfig = context.getSharedPreferences(CONFIG_FILENAME, Context.MODE_PRIVATE);
        addAuthenticationInfo(token.getAuthentication());
        addAuthorizationInfo(token.getAuthorization());
        addConfigurationInfo(token.getConfiguration());
    }

    private void addAuthenticationInfo(Authentication authentication) {
        if (authentication != null) {
            SharedPreferences.Editor editor = spAuthentication.edit();
            editor.putString(Authentication.EMPLOYEE_NAME, authentication.getEmployeeName());
            editor.putString(Authentication.EMPLOYEE_MOBILE, authentication.getMobileNo());
            List<String> orgList = authentication.getOrganization();
            editor.putString(Authentication.ORG_INFO, orgList.toString());
            editor.putString(Authentication.AUTH_TIME, getCurrentDate());
            Log.d("SPLASH", "User: " + authentication.getEmployeeName() + " authenticated!");
            editor.commit();
        }
    }

    private void addAuthorizationInfo(List<Authorization> authList) {
        if (authList == null || authList.size() == 0) return;
        for (Authorization auth : authList)
            Log.d("SPLASH", "Building: " + auth);

        SharedPreferences.Editor editor = spAuthorization.edit();
        Gson gson = new Gson();
        String json = gson.toJson(authList); // myObject - instance of MyObject
        editor.putString(Authorization.BUILDING_INFO, json);
        editor.putString(Authorization.AUTH_TIME, getCurrentDate());
        editor.commit();
    }

    private void addConfigurationInfo(Configuration c) {
        if (c != null) {
            SharedPreferences.Editor editor = spConfig.edit();
            editor.putInt(Configuration.GEO_FENCE, c.getGeoFence());
            editor.putInt(Configuration.UPDATE_CHECK_LIMIT, c.getUpdateCheckLimit());
            Log.d("SPLASH", "Geo Fence: " + c.getGeoFence());
            editor.putString(Configuration.AUTH_TIME, getCurrentDate());
            editor.commit();
        }
    }

    public List<Authorization> getAuthorizationInfo() {
        spAuthorization = context.getSharedPreferences(AUTHORIZATION_FILENAME, Context.MODE_PRIVATE);
        String authInfoJson = spAuthorization.getString(Authorization.BUILDING_INFO, null);
        if (authInfoJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(authInfoJson,
                    new TypeToken<List<Authorization>>() {
                    }.getType()); // myObject - instance of MyObject
        }
        return new ArrayList<Authorization>();
    }

    public String getFloorLevel(long buildingId, long floorplanId) {
        List<Authorization> authList = getAuthorizationInfo(buildingId);
        for (Authorization a : authList) {
            if (Long.parseLong(a.getFloorPlanId()) == floorplanId) {
                return a.getFloorLevel();
            }
        }
        return "0";
    }

    public List<Authorization> getAuthorizationInfo(long buildingId) {
        List<Authorization> authList = getAuthorizationInfo();
        List<Authorization> buildingAuthList = new ArrayList<Authorization>();
        for (Authorization a : authList) {
            if (Long.parseLong(a.getBuildingId()) == buildingId) {
                buildingAuthList.add(a);
            }
        }
        return buildingAuthList;
    }

    private String getCurrentDate() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }

    private String encryptPasswordMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

/*    public boolean isUpdateNeeded(AuthUpdateInput input) {
        int updateCheckLimit = spConfig.getInt(Configuration.UPDATE_CHECK_LIMIT, 0);
        if (updateCheckLimit == 0) return false;

        boolean updateNeeded = false;
        String timeString = spAuthentication.getString(Authentication.AUTH_TIME, null);
        if (isUpdateNeeded(spAuthentication, timeString, updateCheckLimit, Authentication.AUTH_TIME)) {
            input.setAuthenticationCode(CodeConstants.AC20);
            input.setAuthenticationExpiryDateTime(timeString);
            updateNeeded = true;
        }

        timeString = spAuthorization.getString(Authorization.AUTH_TIME, null);
        if (isUpdateNeeded(spAuthorization, timeString, updateCheckLimit, Authorization.AUTH_TIME)) {
            input.setAuthorizationCode(CodeConstants.AC20);
            input.setAuthorizationExpiryDateTime(timeString);
            updateNeeded = true;
        }

        timeString = spConfig.getString(Configuration.AUTH_TIME, null);
        if (isUpdateNeeded(spConfig, timeString, updateCheckLimit, Configuration.AUTH_TIME)) {
            input.setConfigurationCode(CodeConstants.AC20);
            input.setConfigurationExpiryDateTime(timeString);
            updateNeeded = true;
        }

        return updateNeeded;
    }*/

    private boolean isUpdateNeeded(SharedPreferences sp, String timeString, int updateCheckLimit, String authTimeKey) {
        if (timeString == null)
            return false;
        try {
            Date tokenDate = (new SimpleDateFormat(DATE_FORMAT)).parse(timeString);
            Date currentDate = Calendar.getInstance().getTime();
            long diff = currentDate.getTime() - tokenDate.getTime();
            long diffSeconds = diff/1000;
            if (diffSeconds > (updateCheckLimit*24*60*60)) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(authTimeKey, getCurrentDate());
                editor.commit();
                return false;
            } else
                return true;
        }
        catch (ParseException pe) {
            return false;
        }
    }

}
