package com.weqa.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.weqa.model.AuthInput;
import com.weqa.model.AuthResponse;
import com.weqa.service.AuthService;
import com.weqa.ui.BuildingSelectionActivity;

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
    private Context context;

    public AuthAsyncTask(Retrofit retrofit, String logTAG, Context context) {
        this.retrofit = retrofit;
        this.logTag = logTAG;
        this.context = context;
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
        SharedPreferencesUtil sharedPrefUtil = new SharedPreferencesUtil(this.context);
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

            Intent i = new Intent(this.context, BuildingSelectionActivity.class);
            this.context.startActivity(i);

        }
        catch (IOException ioe) {
            Log.e(logTag, "Error in retrofit call" + ioe.getMessage());
        }
    }

    @Override
    protected void onPreExecute() {
    }

}
