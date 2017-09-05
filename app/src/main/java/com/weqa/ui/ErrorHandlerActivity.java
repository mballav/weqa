package com.weqa.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.weqa.R;
import com.weqa.util.GlobalExceptionHandler;

public class ErrorHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler(this));
        setContentView(R.layout.activity_error_handler);
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "font/HelveticaNeueMed.ttf");

        TextView appslogan = (TextView) findViewById(R.id.appslogan);

        appslogan.setTypeface(tf);

        Button restartButton = (Button) findViewById(R.id.restartButton);
        Button exitButton = (Button) findViewById(R.id.exitButton);

        final Context context = this.getApplicationContext();
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SplashScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(WeqaApplication.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager mgr = (AlarmManager) WeqaApplication.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
                ErrorHandlerActivity.this.finishAffinity();
                System.exit(2);            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ErrorHandlerActivity.this.finishAffinity();
            }
        });
    }
}
