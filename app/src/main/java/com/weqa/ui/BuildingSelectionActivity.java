package com.weqa.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weqa.R;
import com.weqa.model.Authorization;
import com.weqa.model.Availability;
import com.weqa.model.AvailabilityInput;
import com.weqa.service.InstanceIdService;
import com.weqa.service.RetrofitBuilder;
import com.weqa.util.AvailabilityAsyncTask;
import com.weqa.util.LocationTracker;
import com.weqa.util.LocationUtil;
import com.weqa.util.SharedPreferencesUtil;
import com.weqa.widget.SearchableSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;

public class BuildingSelectionActivity extends AppCompatActivity implements AvailabilityAsyncTask.UpdateAvailability, View.OnClickListener {


    SharedPreferencesUtil util;
    TextView work, focus, meeting, group;
    ImageView workImage, focusImage, meetingImage, groupImage;

    private static final String LOG_TAG = "WEQA-LOG";
    private int selectedBuildingId;

    Map<String, Integer> itemTypeIdMap = new HashMap<String, Integer>();

    Authorization selectedBuilding;
    SearchableSpinner spinner;
    List items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_selection);

        work = (TextView) findViewById(R.id.work);
        focus = (TextView) findViewById(R.id.focus);
        meeting = (TextView) findViewById(R.id.meeting);
        group = (TextView) findViewById(R.id.group);

        workImage = (ImageView) findViewById(R.id.workImage);
        focusImage = (ImageView) findViewById(R.id.focusImage);
        meetingImage = (ImageView) findViewById(R.id.meetingImage);
        groupImage = (ImageView) findViewById(R.id.groupImage);

        work.setOnClickListener(this);
        focus.setOnClickListener(this);
        meeting.setOnClickListener(this);
        group.setOnClickListener(this);

        workImage.setOnClickListener(this);
        focusImage.setOnClickListener(this);
        meetingImage.setOnClickListener(this);
        groupImage.setOnClickListener(this);

        spinner = (SearchableSpinner) findViewById(R.id.spinner);

        spinner.setPositiveButton("OK");
        spinner.setTitle("Buildings");

        util = new SharedPreferencesUtil(this);
        List<Authorization> authListOriginal = util.getAuthorizationInfo();
        final List<Authorization> authList = removeDupliateBuildings(authListOriginal);

        Log.d(LOG_TAG, "Inside onCreate(): Authorization LIST SIZE: " + authList.size());

        items = new ArrayList();
        for (Authorization a : authList) {
            String bName = getBuildingDisplayName(a);
            items.add(bName);
            Log.d(LOG_TAG, "Inside onCreate(): " + a);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, items);
        spinner.setAdapter(arrayAdapter);

        selectedBuilding = getBuildingForSearchBar(authList);
        selectedBuildingId = Integer.parseInt(selectedBuilding.getBuildingId());

        getAvailability();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Inside onItemSelected: NEW POSITION =========== " + position);
                Authorization authSelected = authList.get(position);
                selectedBuildingId = Integer.parseInt(authSelected.getBuildingId());
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(LOG_TAG, "NOTHING SELECTED");
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, FloorPlanActivity.class);
        i.putExtra("buildingId", selectedBuildingId);
        switch (v.getId()) {
            case R.id.work:
            case R.id.workImage:
                i.putExtra("itemTypeId", itemTypeIdMap.get("Work").intValue());
                break;
            case R.id.focus:
            case R.id.focusImage:
                i.putExtra("itemTypeId", itemTypeIdMap.get("Focus").intValue());
                break;
            case R.id.meeting:
            case R.id.meetingImage:
                i.putExtra("itemTypeId", itemTypeIdMap.get("Meeting").intValue());
                break;
            case R.id.group:
            case R.id.groupImage:
                i.putExtra("itemTypeId", itemTypeIdMap.get("Group").intValue());
                break;
        }
        this.startActivity(i);
    }

    private List<Authorization> removeDupliateBuildings(List<Authorization> aList) {
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

    private String getBuildingDisplayName(Authorization a) {
        String bName = a.getBuildingName() + ", " + a.getAddress();
        if (bName.length() > 30) bName = bName.substring(0, 30) + "...";
        return bName;
    }

    public void updateUI() {
        if (itemTypeIdMap.isEmpty()) {

            String bName = getBuildingDisplayName(selectedBuilding);
            int index = items.indexOf(bName);
            spinner.setSelection(index);

            List<Availability> allList = util.getAvailability();
            for (Availability a : allList) {
                itemTypeIdMap.put(a.getItemType(), a.getItemTypeId());
            }
        }
        List<Availability> aList = util.getAvailability(selectedBuildingId);
        for (Availability a : aList) {
            Log.d(LOG_TAG, "Item TYPE: " + a.getItemType() + ", Item TYPE ID: " + a.getItemTypeId());
            switch (a.getItemType()) {

                case "Work":
                    if (a.getItemCount() > 0)
                        work.setText(a.getItemCount() + " Available");
                    else
                        work.setText("Not Available");
                    break;

                case "Focus":
                    if (a.getItemCount() > 0)
                        focus.setText(a.getItemCount() + " Available");
                    else
                        focus.setText("Not Available");
                    break;

                case "Meeting":
                    if (a.getItemCount() > 0)
                        meeting.setText(a.getItemCount() + " Available");
                    else
                        meeting.setText("Not Available");
                    break;

                case "Group":
                    if (a.getItemCount() > 0)
                        group.setText(a.getItemCount() + " Available");
                    else
                        group.setText("Not Available");
                    break;
            }
        }
    }

    private void getAvailability() {
        Retrofit retrofit = RetrofitBuilder.getRetrofit();

        AvailabilityAsyncTask runner = new AvailabilityAsyncTask(retrofit, LOG_TAG, this);
        AvailabilityInput input = new AvailabilityInput();
        input.setBuilding("1");
        input.setUuid(InstanceIdService.getAppInstanceId(BuildingSelectionActivity.this));
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input);
    }

    private Authorization getBuildingForSearchBar(List<Authorization> authList) {
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
        LocationTracker tracker = new LocationTracker(this);
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
