package com.weqa.util;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.weqa.model.FloorplanImage;
import com.weqa.model.FloorplanImageInput;
import com.weqa.service.AuthService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Manish Ballav on 8/21/2017.
 */

public class FloorplanImageAsyncTask extends AsyncTask<Object, String, String> {

    public static interface UpdateImage {
        public void updateUI(String base64Image);
    }

    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAILURE = "not-ok";

    private Retrofit retrofit;
    private String logTag;
    private Activity activity;
    private FloorplanImage image;

    public FloorplanImageAsyncTask(Retrofit retrofit, String logTAG, Activity activity) {
        this.retrofit = retrofit;
        this.logTag = logTAG;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            getFloorplanImage((FloorplanImageInput) params[0]);
        } catch (Exception e) {
            Log.e(logTag, "Error in async task", e);
            return STATUS_FAILURE;
        }
        return STATUS_OK;
    }

    private void getFloorplanImage(FloorplanImageInput input) {
        AuthService service = retrofit.create(AuthService.class);
        Call<FloorplanImage> call1 = service.floorplanImage(input);
        try {
            image = call1.execute().body();
            Log.d(logTag, "Base64 Image String size: " + image.getX().length());
            Log.d(logTag, "Availability response received!");
        }
        catch (IOException ioe) {
            Log.e(logTag, "Error in retrofit call", ioe);
        }
    }

    @Override
    protected void onPreExecute() {
    }

    protected void onPostExecute(String status) {
        if (status.equals(STATUS_OK)) {
            final UpdateImage u = (UpdateImage) this.activity;
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // This code will always run on the UI thread, therefore is safe to modify UI elements.
                    u.updateUI(image.getX());
                }
            });
        }
    }
}
