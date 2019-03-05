package com.example.admin.autosilentmode.fireBaseJob;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MyJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
        Log.e("Call-", "Start");

        return true; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.e("Call-", "stop");
        return false; // Answers the question: "Should this job be retried?"
    }
}