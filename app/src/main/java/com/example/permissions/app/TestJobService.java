package com.example.permissions.app;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class TestJobService extends JobService {

    private static final String TAG = "TestJobService";

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.i(TAG, "Job ran successfully");
        return false;
    }

    @Override
    public boolean onStopJob(final JobParameters jobParameters) {
        Log.w(TAG, "Job stopped!");
        return false;
    }
}
