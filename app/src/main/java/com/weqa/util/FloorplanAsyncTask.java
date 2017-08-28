package com.weqa.util;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.weqa.model.FloorPlanDetail;
import com.weqa.model.FloorplanInput;
import com.weqa.model.FloorplanResponse;
import com.weqa.service.AuthService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Manish Ballav on 8/22/2017.
 */

public class FloorplanAsyncTask extends AsyncTask<Object, String, String> {

    public static interface UpdateFloorplan {
        public void updateFloorplan(FloorplanResponse fr);
    }

    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAILURE = "not-ok";

    private Retrofit retrofit;
    private String logTag;
    private Activity activity;
    private FloorplanResponse response;
    private SharedPreferencesUtil util;

//    private ProgressDialog dialog;
    public FloorplanAsyncTask(Retrofit retrofit, String logTAG, Activity activity) {
        this.retrofit = retrofit;
        this.logTag = logTAG;
        this.activity = activity;
        this.util = new SharedPreferencesUtil(activity);
//        this.dialog = new ProgressDialog(activity);
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            getFloorplanDetails((FloorplanInput) params[0], (Integer) params[1]);
        } catch (Exception e) {
            Log.e(logTag, "Error in async task", e);
            return STATUS_FAILURE;
        }
        return STATUS_OK;
    }

    private void getFloorplanDetails(FloorplanInput input, Integer buildingId) {
        AuthService service = retrofit.create(AuthService.class);
        Gson gson = new Gson();
        String json = gson.toJson(input); // myObject - instance of MyObject
        Log.d(logTag, "INPUT: " + json);
        Call<FloorplanResponse> call1 = service.floorplan(input);
        try {
            response = call1.execute().body();
            json = gson.toJson(response); // myObject - instance of MyObject
            Log.d(logTag, "OUTPUT: " + json);
            Log.d(logTag, "Base64 Image String size: " + response.getFloorPlanDetails().get(0).getFloorImage().length());
            Log.d(logTag, "Floorplan detail response received!");
            if (!(util.getNoSpaceOnDevice())) {
                saveFloorplans(response, buildingId);
            }
        }
        catch (IOException ioe) {
            Log.e(logTag, "Error in retrofit call", ioe);
        }
    }

    private void saveFloorplans(FloorplanResponse r, Integer buildingId) {
        long extMemSize = MemoryUtil.getExternalStorageSpace();
        for (FloorPlanDetail f : r.getFloorPlanDetails()) {
            if (f.getFloorImage() != null && f.getFloorImage().length() > 0) {
                byte[] decodedString = Base64.decode(f.getFloorImage(), Base64.DEFAULT);

                Log.d(logTag, "GOING GOING GOING GOING to SAVE IMAGE DOWNLOADED-------------------------------------------");
                Log.d(logTag, "Saving floorplan for floorplan ID: " + f.getFloorPlanId());
                if (decodedString.length > extMemSize) {
                    util.setNoSpaceOnDevice(true);
                    return;
                }

                try {
                    File floorplanFile = new File(Environment.getExternalStorageDirectory(),
                            "Pictures/floorplan_" + buildingId.intValue() + "_" + f.getFloorPlanId().intValue());
                    Log.d(logTag, "Saving to file: " + "Pictures/floorplan_" + buildingId.intValue() + "_" + f.getFloorPlanId().intValue());

                    if (!floorplanFile.mkdirs()) {
                        Log.e(logTag, "Directory not created");
                    }

                    FileOutputStream fos = new FileOutputStream(floorplanFile);

                    fos.write(decodedString);

                    fos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
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
            final FloorplanAsyncTask.UpdateFloorplan u = (FloorplanAsyncTask.UpdateFloorplan) this.activity;
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
