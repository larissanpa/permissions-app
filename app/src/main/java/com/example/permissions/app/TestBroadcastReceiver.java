package com.example.permissions.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TestBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "TestBroadcastReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.i(TAG, "Alarm triggered successfully");
    }
}
