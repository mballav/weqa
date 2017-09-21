package com.weqa.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.weqa.ui.ErrorHandlerActivity;

/**
 * Created by Manish Ballav on 9/4/2017.
 */

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String LOG_TAG = "WEQA-LOG";

    private Context context;

    public GlobalExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        Log.e(LOG_TAG, "Uncaught exception", throwable);

        Intent intent = new Intent(context, ErrorHandlerActivity.class);
        context.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
