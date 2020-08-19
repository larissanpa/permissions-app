package com.example.permissions.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_CODE = 642;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scheduleAlarm();
        scheduleJob();
    }

    public void requestMicrophonePermission(View view) {
        requestPermission(Manifest.permission.RECORD_AUDIO);
    }

    public void requestLocationPermission(View view) {
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public void requestCameraPermission(View view) {
        requestPermission(Manifest.permission.CAMERA);
    }

    private boolean checkPermissionStatus(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(final String permission) {
        if (checkPermissionStatus(permission)) {
            // permission already granted
            return;
        }
        // Permission is not granted
        ActivityCompat.requestPermissions(this,
                                          new String[]{permission},
                                          PERMISSIONS_REQUEST_CODE);
    }

    private void scheduleAlarm() {
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            Log.i(TAG, "Scheduling alarm");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                        SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(5),
                                        getPendingIntent());
            } else {
                am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(5), getPendingIntent());
            }
            Log.i(TAG, "Alarm Scheduled");
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(getApplicationContext(), TestBroadcastReceiver.class);
        intent.setAction("com.example.permissions.app.alarm");
        intent.setPackage(getPackageName());

        return PendingIntent.getBroadcast(this, 34761, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @SuppressLint("NewApi")
    private void scheduleJob() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                ComponentName serviceName = new ComponentName(this, TestJobService.class);
                JobInfo jobInfo = new JobInfo.Builder(32234, serviceName)
                        .setMinimumLatency(TimeUnit.MINUTES.toMillis(5))
                        .setOverrideDeadline(TimeUnit.MINUTES.toMillis(6))
                        .build();
                int result = jobScheduler.schedule(jobInfo);
                Log.i(TAG, "Job scheduling result: " + (result == JobScheduler.RESULT_SUCCESS ? "success" : "failure"));
            }
        }
    }
}