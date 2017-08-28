package com.weqa.model;

import android.graphics.Bitmap;

/**
 * Created by pc on 7/31/2017.
 */

public class HotspotCenter {
    public float x;
    public float y;
    public Bitmap croppedBitmap;

    public HotspotCenter() {
    }

    public HotspotCenter(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!HotspotCenter.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        HotspotCenter c = (HotspotCenter) obj;
        if (c.x != this.x || c.y != this.y) {
            return false;
        }
        return true;
    }
}
