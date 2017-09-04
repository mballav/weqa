package com.weqa.ui;

/**
 * Created by Manish Ballav on 9/4/2017.
 */
import android.app.Application;
import android.content.Context;

public class WeqaApplication extends Application {

    public static WeqaApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static WeqaApplication getInstance() {
        return instance;
    }
}