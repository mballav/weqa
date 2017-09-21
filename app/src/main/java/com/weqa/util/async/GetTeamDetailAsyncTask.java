package com.weqa.util.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.weqa.model.CollaborationInput;
import com.weqa.model.CollaborationResponse;
import com.weqa.model.json.TeamDetailInput;
import com.weqa.model.json.TeamDetailResponse;
import com.weqa.service.AuthService;
import com.weqa.util.CollaborationAsyncTask;
import com.weqa.util.SharedPreferencesUtil;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Manish Ballav on 9/21/2017.
 */

public class GetTeamDetailAsyncTask extends AsyncTask<Object, String, String> {

    public static interface UpdateUI {
        public void updateUI(List<TeamDetailResponse> responses);
    }

    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAILURE = "not-ok";

    private Retrofit retrofit;
    private String logTag;
    private Activity activity;
    private List<TeamDetailResponse> responseList;
    private SharedPreferencesUtil sharedPrefUtil;

    public GetTeamDetailAsyncTask(Retrofit retrofit, String logTAG, Activity activity) {
        this.retrofit = retrofit;
        this.logTag = logTAG;
        this.activity = activity;
        sharedPrefUtil = new SharedPreferencesUtil(this.activity.getApplicationContext());
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            getTeamDetail((TeamDetailInput) params[0]);
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

    private void getTeamDetail(TeamDetailInput input) {
        AuthService service = retrofit.create(AuthService.class);
        Call<List<TeamDetailResponse>> call1 = service.teamDetail(input);
        try {
            Log.e(logTag, "Retrofit call now...");
            Gson gson = new Gson();
            String inputJson = gson.toJson(input);
            Log.d(logTag, "Input: " + inputJson);

            responseList = call1.execute().body();
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

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String status) {

        final Activity a = this.activity;
        final Context context = this.activity.getApplication();

        if (status.equals(STATUS_OK)) {

            Gson gson = new Gson();
            String json = gson.toJson(responseList);
            Log.d(logTag, "Output: " + json);
            Log.d(logTag, "Get team detail response received!");

            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((GetTeamDetailAsyncTask.UpdateUI) activity).updateUI(responseList);
                }
            });
        }
        else {
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Problem with connectivity... exiting!", Toast.LENGTH_LONG).show();
                    activity.finish();
                }
            });
        }
    }

}
