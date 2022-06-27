package com.aminsoheyli.androidtutorial.component

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

const val TAG = "MyJobServiceTag"

class MyJobService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(TAG, "Job is started successfully")
        jobFinished(params, false)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i(TAG, "Job is stopped successfully")
        return false
    }
}