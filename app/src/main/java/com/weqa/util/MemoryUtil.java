package com.weqa.util;

import android.os.Environment;
import android.os.StatFs;

/**
 * Created by Manish Ballav on 8/24/2017.
 */

public class MemoryUtil {

    public static long getExternalStorageSpace() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSizeLong() * (long)stat.getAvailableBlocksLong();
        return bytesAvailable;
    }

    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
}
