package com.valentinekuzmich.simplealarmclock.alarm_call

import android.app.Activity
import com.evernote.android.job.Job
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.evernote.android.job.DailyJob
import com.evernote.android.job.JobRequest
import com.valentinekuzmich.simplealarmclock.ring.RingActivity
import java.util.concurrent.TimeUnit

import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.content.Context.POWER_SERVICE




/**
 * Created by Valentine on 22.12.2017.
 */
class AlarmClockJob: DailyJob() {
    override fun onRunDailyJob(params: Job.Params): DailyJob.DailyJobResult {
        var i = Intent(context,RingActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
        return DailyJob.DailyJobResult.SUCCESS
    }



    companion object {
        val TAG = "simple_alarm_clock_job"

        /*fun addAlarmClock(hour: Long, minute: Long){
            DailyJob.schedule( JobRequest.Builder(TAG), TimeUnit.HOURS.toMillis(1), TimeUnit.HOURS.toMillis(6))
        }*/
    }
}