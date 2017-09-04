package com.weqa.ui;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.weqa.R;
import com.weqa.model.AuthInput;
import com.weqa.model.CodeConstants;
import com.weqa.service.InstanceIdService;
import com.weqa.service.RetrofitBuilder;
import com.weqa.util.AuthAsyncTask;
import com.weqa.util.GlobalExceptionHandler;

import retrofit2.Retrofit;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String LOG_TAG = "WEQA-LOG";

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(3500); // As I am using LENGTH_LONG in Toast
                SplashScreenActivity.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler(this));
        setContentView(R.layout.activity_splash_screen);

        if (android.os.Build.VERSION.SDK_INT < 21) {
            Toast.makeText(this, "This application requires newer version of OS than your android phone provides!", Toast.LENGTH_LONG).show();
            thread.start();
        }
        else {

            Typeface tf = Typeface.createFromAsset(this.getAssets(), "font/HelveticaNeueMed.ttf");

            TextView appname = (TextView) findViewById(R.id.appname);
            TextView appslogan = (TextView) findViewById(R.id.appslogan);

            appname.setTypeface(tf);
            appslogan.setTypeface(tf);

        /*
        LocationTracker tracker = new LocationTracker(this);
        // check if location is available
        if (tracker.isLocationEnabled()) {
            double lat1 = tracker.getLatitude();
            double lon1 = tracker.getLongitude();
            Toast.makeText(getApplicationContext(), "Lat: " + lat1 + ", Lon: " + lon1, Toast.LENGTH_LONG).show();
        } else {
            // show dialog box to user to enable location
            tracker.askToOnLocation();
        }
        */

//        It should show approx. 909 Km
//        Toast.makeText(getApplicationContext(), "Distance: " + LocationUtil.getDistance(50.3, -5.1, 58.4, -3.2), Toast.LENGTH_LONG).show();

            authenticate();
        }
    }

    private void authenticate() {
        Retrofit retrofit = RetrofitBuilder.getRetrofit();

        Log.d(LOG_TAG, "Calling the API to authenticate...");
        AuthAsyncTask runner = new AuthAsyncTask(retrofit, LOG_TAG, this);
        AuthInput input = new AuthInput();
        input.setAuthenticationCode(CodeConstants.AC10);
        input.setAuthorizationCode(CodeConstants.AC20);
        input.setConfigurationCode(CodeConstants.AC30);
        input.setUuid(InstanceIdService.getAppInstanceId(SplashScreenActivity.this));
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input);
        Log.d(LOG_TAG, "Waiting for response...");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
