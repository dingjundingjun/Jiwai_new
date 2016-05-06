package com.sounuo.jiwai.utils;

import android.util.Log;

/**
 * Created by dj on 2015/7/18.
 * email:dingjun0225@gmail.com
 */
public class Debug {
    private static String TAG = "JIWAI";
    private static final boolean DEBUG = true;

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }
}
