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
import com.weqa.ui.LandingScreenActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Manish Ballav on 9/17/2017.
 */

public class AuthWithCodeAsyncTask extends AsyncTask<Object, String, String> {
    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAILURE = "not-ok";

    private Retrofit retrofit;
    private String logTag;
    private Activity activity;
    private AuthResponse response;
    private SharedPreferencesUtil sharedPrefUtil;

    public AuthWithCodeAsyncTask(Retrofit retrofit, String logTAG, Activity activity) {
        this.retrofit = retrofit;
        this.logTag = logTAG;
        this.activity = activity;
        sharedPrefUtil = new SharedPreferencesUtil(this.activity.getApplicationContext());
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            auth((AuthInput) params[0]);
        } catch (Exception e) {
            Log.d(logTag, "Error in async task " + e.getMessage());
            final Context context = this.activity.getApplication();
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Please try again later!", Toast.LENGTH_LONG).show();
                }
            });
            return STATUS_FAILURE;
        }
        return STATUS_OK;
    }

    private void auth(AuthInput input) {
        AuthService service = retrofit.create(AuthService.class);
        Call<AuthResponse> call1 = service.auth(input);
        try {
            Log.e(logTag, "Retrofit call now...");
            Gson gson = new Gson();
            String inputJson = gson.toJson(input);
            Log.d(logTag, "Input: " + inputJson);

            response = call1.execute().body();
        }
        catch (IOException ioe) {
            Log.d(logTag, "Error in retrofit call" + ioe.getMessage());
            final Context context = this.activity.getApplication();
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Connectivity Problem. Please try again later!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    protected void onPostExecute(String status) {

        final Activity a = this.activity;
        final Context context = this.activity.getApplication();

        if (status.equals(STATUS_OK)) {

            sharedPrefUtil.addAuthTokens(response);

            Gson gson = new Gson();
            String json = gson.toJson(response);
            Log.d(logTag, "Output: " + json);
            Log.d(logTag, "Auth response received!");

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
