package com.weqa.service;

import android.content.Context;

import com.google.android.gms.iid.InstanceID;

/**
 * Created by pc on 8/4/2017.
 */

public class InstanceIdService {

    public static String getAppInstanceId(Context context) {
        return InstanceID.getInstance(context).getId();
    }
}
