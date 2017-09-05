package com.weqa.util;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.weqa.model.BookingInput;
import com.weqa.model.BookingReleaseInput;
import com.weqa.model.BookingResponse;
import com.weqa.service.AuthService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Manish Ballav on 9/2/2017.
 */

public class BookingReleaseAsyncTask extends AsyncTask<Object, String, String> {

    public static interface OnShowBookingReleaseResponse {
        public void showBookingReleaseResponse(BookingResponse br, String qrCode,
                                               boolean showConfirmation, boolean removeLocalBooking);
    }

    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAILURE = "not-ok";

    private Retrofit retrofit;
    private String logTag;
    private Activity activity;
    private BookingResponse response;
    private SharedPreferencesUtil util;

    private String qrCode;
    private boolean showConfirmation;
    private boolean removeLocalBooking;

    public BookingReleaseAsyncTask(Retrofit retrofit, String logTAG, Activity activity) {
        this.retrofit = retrofit;
        this.logTag = logTAG;
        this.activity = activity;
        this.util = new SharedPreferencesUtil(activity);
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            this.showConfirmation = (Boolean) params[1];
            this.removeLocalBooking = (Boolean) params[2];
            BookingReleaseInput input = (BookingReleaseInput) params[0];
            qrCode = input.getQrCode();
            book(input);
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

    private void book(BookingReleaseInput input) {
        AuthService service = retrofit.create(AuthService.class);
        Gson gson = new Gson();
        String json = gson.toJson(input); // myObject - instance of MyObject
        Log.d(logTag, "INPUT: " + json);
        Call<BookingResponse> call1 = service.bookRelease(input);
        try {
            response = call1.execute().body();

            json = gson.toJson(response); // myObject - instance of MyObject
            Log.d(logTag, "OUTPUT: " + json);
            Log.d(logTag, "Booking release response received!");
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


    @Override
    protected void onPostExecute(String status) {
        if (status.equals(STATUS_OK)) {
            final BookingReleaseAsyncTask.OnShowBookingReleaseResponse u
                    = (BookingReleaseAsyncTask.OnShowBookingReleaseResponse) this.activity;
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // This code will always run on the UI thread, therefore is safe to modify UI elements.
                    u.showBookingReleaseResponse(response, qrCode, showConfirmation, removeLocalBooking);
                }
            });
        }
    }
}
