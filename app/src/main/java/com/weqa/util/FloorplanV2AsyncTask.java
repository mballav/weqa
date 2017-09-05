package com.weqa.util;

/**
 * Created by Manish Ballav on 8/31/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.weqa.model.FloorPlanDetailV2;
import com.weqa.model.FloorplanInputV2;
import com.weqa.model.FloorplanResponseV2;
import com.weqa.model.ItemTypeDetailV2;
import com.weqa.service.AuthService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Manish Ballav on 8/22/2017.
 */

public class FloorplanV2AsyncTask extends AsyncTask<Object, String, String> {

    public static interface UpdateFloorplan {
        public void updateFloorplan(FloorplanResponseV2 fr);
    }

    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAILURE = "not-ok";

    private Retrofit retrofit;
    private String logTag;
    private Activity activity;
    private FloorplanResponseV2 response;
    private SharedPreferencesUtil util;

    public FloorplanV2AsyncTask(Retrofit retrofit, String logTAG, Activity activity) {
        this.retrofit = retrofit;
        this.logTag = logTAG;
        this.activity = activity;
        this.util = new SharedPreferencesUtil(activity);
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            getFloorplanDetails((FloorplanInputV2) params[0], (Long) params[1]);
        } catch (Exception e) {
            Log.d(logTag, "Error in async task! " + e.getMessage());
            final Context context = this.activity.getApplicationContext();
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // This code will always run on the UI thread, therefore is safe to modify UI elements.
                    Toast.makeText(context, "Please try again later!", Toast.LENGTH_LONG).show();
                }
            });
            return STATUS_FAILURE;
        }
        return STATUS_OK;
    }

    private void getFloorplanDetails(FloorplanInputV2 input, Long buildingId) {
        AuthService service = retrofit.create(AuthService.class);
        Gson gson = new Gson();
        String json = gson.toJson(input); // myObject - instance of MyObject
        Log.d(logTag, "INPUT: " + json);
        Call<FloorplanResponseV2> call1 = service.floorplanV2(input);
        try {
            response = call1.execute().body();

            FloorPlanDetailV2 firstAvailable = response.getFloorPlanDetails().get(0);
            boolean imageReturned = ((firstAvailable.getFloorImage() != null) && (firstAvailable.getFloorImage().length() > 0));

            json = gson.toJson(response); // myObject - instance of MyObject
            Log.d(logTag, "OUTPUT: " + json);
            Log.d(logTag, "Floorplan detail response received!");


            if (imageReturned && !(util.getNoSpaceOnDevice())) {
                saveFloorplans(response, buildingId);
            }
        }
        catch (IOException ioe) {
            Log.d(logTag, "Error in retrofit call" + ioe.getMessage());
            final Context context = this.activity.getApplicationContext();
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // This code will always run on the UI thread, therefore is safe to modify UI elements.
                    Toast.makeText(context, "Connectivity Problem. Please try again later!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void saveFloorplans(FloorplanResponseV2 r, Long buildingId) {
        long extMemSize = MemoryUtil.getExternalStorageSpace();
        FloorPlanDetailV2 f = r.getFloorPlanDetails().get(0);

        if (f.getFloorImage() != null && f.getFloorImage().length() > 0) {
            byte[] decodedString = Base64.decode(f.getFloorImage(), Base64.DEFAULT);

            Log.d(logTag, "Saving floorplan for floorplan ID: " + f.getFloorPlanId());
            if (decodedString.length > extMemSize) {
                util.setNoSpaceOnDevice(true);
                return;
            }

            try {
                File floorplanFile = new File(Environment.getExternalStorageDirectory(),
                        "Pictures/floorplan_" + buildingId.intValue() + "_" + f.getFloorPlanId().intValue());
                Log.d(logTag, "Saving to file: " + "Pictures/floorplan_" + buildingId.intValue() + "_" + f.getFloorPlanId().intValue());

                File picturesDir = new File(Environment.getExternalStorageDirectory(), "Pictures");

                if (!picturesDir.exists()) {
                    if (!picturesDir.mkdirs()) {
                        Log.e(logTag, "Directory not created");
                    }
                }

                FileOutputStream fos = new FileOutputStream(floorplanFile);
                fos.write(decodedString);
                fos.close();
            } catch (IOException ioe) {
                Log.d(logTag, "Problem saving downloaded image! " + ioe.getMessage());
            }
            Log.d(logTag, "Floorplan saved for floorplan ID: " + f.getFloorPlanId());
        }
    }

    @Override
    protected void onPreExecute() {
        //      this.dialog.setMessage("Please wait");
        //      this.dialog.show();
    }

    protected void onPostExecute(String status) {
        //      if (dialog.isShowing()) {
        //          dialog.dismiss();
        //      }
        if (status.equals(STATUS_OK)) {
            final FloorplanV2AsyncTask.UpdateFloorplan u = (FloorplanV2AsyncTask.UpdateFloorplan) this.activity;
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // This code will always run on the UI thread, therefore is safe to modify UI elements.
                    u.updateFloorplan(response);
                }
            });
        }
    }
}
