package com.weqa.util;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.weqa.model.Availability;
import com.weqa.model.AvailabilityInput;
import com.weqa.service.AuthService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Manish Ballav on 8/18/2017.
 */

public class AvailabilityAsyncTask extends AsyncTask<Object, String, String> {

    public static interface UpdateAvailability {
        public void updateUI();
    }

    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAILURE = "not-ok";

    private Retrofit retrofit;
    private String logTag;
    private Activity activity;

    public AvailabilityAsyncTask(Retrofit retrofit, String logTAG, Activity activity) {
        this.retrofit = retrofit;
        this.logTag = logTAG;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            getAvailability((AvailabilityInput) params[0]);
        } catch (Exception e) {
            Log.e(logTag, "Error in async task", e);
            return STATUS_FAILURE;
        }
        return STATUS_OK;
    }

    private void getAvailability(AvailabilityInput input) {
        SharedPreferencesUtil s = new SharedPreferencesUtil(this.activity);
        AuthService service = retrofit.create(AuthService.class);
        Call<List<Availability>> call1 = service.availability(input);
        try {
            List<Availability> availabilityList = call1.execute().body();
            s.addAvailability(availabilityList);

            Gson gson = new Gson();
            String inputJson = gson.toJson(input);
            String json = gson.toJson(availabilityList);
            Log.d(logTag, inputJson);
            Log.d(logTag, json);
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
            final UpdateAvailability u = (UpdateAvailability) this.activity;
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // This code will always run on the UI thread, therefore is safe to modify UI elements.
                    u.updateUI();
                }
            });
        }
    }
}
