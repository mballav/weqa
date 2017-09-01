package com.weqa.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.weqa.model.AuthInput;
import com.weqa.model.AuthResponse;
import com.weqa.service.AuthService;
import com.weqa.ui.BuildingSelectionActivity;
import com.weqa.ui.LandingScreenActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Manish Ballav on 8/5/2017.
 */

public class AuthAsyncTask extends AsyncTask<Object, String, String> {

    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAILURE = "not-ok";

    private Retrofit retrofit;
    private String logTag;
    private Activity activity;

    public AuthAsyncTask(Retrofit retrofit, String logTAG, Activity activity) {
        this.retrofit = retrofit;
        this.logTag = logTAG;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            auth((AuthInput) params[0]);
        } catch (Exception e) {
            Log.e(logTag, "Error in async task", e);
            return STATUS_FAILURE;
        }
        return STATUS_OK;
    }

    private void auth(AuthInput input) {
        SharedPreferencesUtil sharedPrefUtil = new SharedPreferencesUtil(this.activity.getApplicationContext());
        AuthService service = retrofit.create(AuthService.class);
        Call<AuthResponse> call1 = service.auth(input);
        try {
            Log.e(logTag, "Retrofit call now...");
            AuthResponse token = call1.execute().body();
            sharedPrefUtil.addAuthTokens(token);

            Gson gson = new Gson();
            String inputJson = gson.toJson(input);
            String json = gson.toJson(token);
            Log.d(logTag, inputJson);
            Log.d(logTag, json);
            Log.d(logTag, "Auth response received!");
        }
        catch (IOException ioe) {
            Log.e(logTag, "Error in retrofit call" + ioe.getMessage());
        }
    }

    @Override
    protected void onPreExecute() {
    }

    protected void onPostExecute(String status) {

        final Activity a = this.activity;
        final Context context = this.activity.getApplication();

        if (status.equals(STATUS_OK)) {

            // This code will always run on the UI thread, therefore is safe to modify UI elements.
            Intent i = new Intent(context, LandingScreenActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else {

            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Problem with connectivity... exiting!", Toast.LENGTH_LONG).show();
                    a.finish();
                }
            });
        }
    }
}
