package com.weqa.ui;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weqa.R;
import com.weqa.model.AuthInput;
import com.weqa.model.CodeConstants;
import com.weqa.service.InstanceIdService;
import com.weqa.service.RetrofitBuilder;
import com.weqa.util.AuthAsyncTask;
import com.weqa.util.AuthWithCodeAsyncTask;
import com.weqa.util.GlobalExceptionHandler;
import com.weqa.util.SharedPreferencesUtil;
import com.weqa.util.ui.KeyboardUtil;

import retrofit2.Retrofit;

public class SplashScreenActivity extends AppCompatActivity implements AuthAsyncTask.UpdateUI, View.OnTouchListener {

    private static final String LOG_TAG = "WEQA-LOG";

    private Button register, connectCode;
    private TextView orText;
    private EditText code;
    private LinearLayout codeContainer;
    private SharedPreferencesUtil util;
    private ProgressBar progressBar;

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

            util = new SharedPreferencesUtil(this);

            register = (Button) findViewById(R.id.register);
            orText = (TextView) findViewById(R.id.ortext);
            connectCode = (Button) findViewById(R.id.connectcode);
            code = (EditText) findViewById(R.id.activationCode);
            codeContainer = (LinearLayout) findViewById(R.id.codeContainer);

            register.setTypeface(tf);
            orText.setTypeface(tf);
            connectCode.setTypeface(tf);

            RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

            connectCode.setOnTouchListener(this);
            container.setOnTouchListener(this);
            code.setOnTouchListener(this);

            progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //TextView appslogan = (TextView) findViewById(R.id.appslogan);
        //appslogan.setTypeface(tf);

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

        //It should show approx. 909 Km
        //Toast.makeText(getApplicationContext(), "Distance: " + LocationUtil.getDistance(50.3, -5.1, 58.4, -3.2), Toast.LENGTH_LONG).show();

        authenticate();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.connectcode) {
            Button b = (Button) v;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                b.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorTABtextSelected));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                b.setBackgroundColor(ContextCompat.getColor(v.getContext(), android.R.color.transparent));
            }
            KeyboardUtil.hideSoftKeyboard(this);
        }
        else if (v.getId() == R.id.activationCode) {
            // TODO
        }
        else if (v.getId() != R.id.register){
            KeyboardUtil.hideSoftKeyboard(this);
        }
        return false;
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

    public void updateUI() {

        register.setVisibility(View.VISIBLE);
        orText.setVisibility(View.VISIBLE);
        codeContainer.setVisibility(View.VISIBLE);

        connectCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // InputDialogUtil.showCodeInputDialog(SplashScreenActivity.this);
                sendCode();
            }
        });
    }

    public void sendCode() {

        String activationCode = code.getText().toString();

        if (isCodeValid(activationCode)) {

            register.setVisibility(View.GONE);
            orText.setVisibility(View.GONE);
            codeContainer.setVisibility(View.GONE);

            progressBar.setVisibility(View.VISIBLE);

            Retrofit retrofit = RetrofitBuilder.getRetrofit();

            Log.d(LOG_TAG, "Calling the API to authenticate...");
            AuthWithCodeAsyncTask runner = new AuthWithCodeAsyncTask(retrofit, LOG_TAG, this);
            AuthInput input = new AuthInput();
            input.setActivationCode(activationCode);
            input.setAuthenticationCode(CodeConstants.AC10);
            input.setAuthorizationCode(CodeConstants.AC20);
            input.setConfigurationCode(CodeConstants.AC30);
            input.setUuid(InstanceIdService.getAppInstanceId(this));
            runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input);
            Log.d(LOG_TAG, "Waiting for response...");
        }
        else {
            Toast.makeText(this, "Invalid Code", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCodeValid(String activationCode) {
        String[] codes = util.getActivationCodes();
        for (String c : codes) {
            Log.d(LOG_TAG, "Code: " + c);
        }
        boolean valid = false;
        for (String c : codes) {
            if (c.equalsIgnoreCase(activationCode)) {
                valid = true;
                break;
            }
        }
        return valid;
    }
}
