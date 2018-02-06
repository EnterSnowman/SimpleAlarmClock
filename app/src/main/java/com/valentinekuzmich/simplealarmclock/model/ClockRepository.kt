package com.valentinekuzmich.simplealarmclock.model

import android.util.Log
import android.content.Context
import com.evernote.android.job.DailyJob
import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.valentinekuzmich.simplealarmclock.Utils.TextUtils
import com.valentinekuzmich.simplealarmclock.alarm_call.AlarmClockJob
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by Valentine on 22.12.2017.
 */
object ClockRepository {
    val mSecondsInterval = 59
    val LIST_OF_CLOCKS = "listOfClocks"
    val LIST_OF_IDS = "listOfIds"
    fun addClock(hour: Long, minute: Long,context: Context):Observable<Clock>{
        var clocksPreferences = context.getSharedPreferences(LIST_OF_CLOCKS, Context.MODE_PRIVATE)
        var idsPreferences = context.getSharedPreferences(LIST_OF_IDS, Context.MODE_PRIVATE)

        var id = DailyJob.schedule( JobRequest.Builder(TextUtils.getAlarmTagByTimeInString(TextUtils.getTimeString(hour, minute)))
                .setUpdateCurrent(false),
                TimeUnit.HOURS.toMillis(hour)+TimeUnit.MINUTES.toMillis(minute),
                TimeUnit.HOURS.toMillis(hour)+TimeUnit.MINUTES.toMillis(minute)+TimeUnit.SECONDS.toMillis(mSecondsInterval.toLong()))
        //Log.d("Clock Scheduler", "New clock ${newJobId}")
        clocksPreferences.edit().putBoolean(TextUtils.getTimeString(hour, minute),true).commit()
        idsPreferences.edit().putInt(TextUtils.getTimeString(hour, minute),id).commit()
        var clockObservable = Observable.just(Clock(true, hour.toInt(), minute.toInt()))
        //Log.d("Clock Scheduler","Millis to minutes ${JobManager.instance().getJobRequest(newJobId).startMs} ${TimeUnit.MILLISECONDS.toMinutes(JobManager.instance().getJobRequest(newJobId).startMs)}")
        Log.d("Clock Scheduler", "Clock added $hour $minute")
        return clockObservable
    }

    fun isClockExist(hour: Long, minute: Long): Boolean = !JobManager.instance().getAllJobsForTag(TextUtils.getAlarmTagByTimeInString(TextUtils.getTimeString(hour, minute))).isEmpty()

    fun getListOfClocks(context: Context): Observable<Clock>{
        //var clockJobs = JobManager.instance().getAllJobRequestsForTag(AlarmClockJob.TAG)
        //Log.d("Clock Scheduler", "Clock size ${clockJobs.size}")
        var clocks = ArrayList<Clock>()
        var tmp = context.getSharedPreferences(LIST_OF_CLOCKS, Context.MODE_PRIVATE).all as(Map<String, Boolean>)
        for (entry in tmp.entries)
            clocks.add(Clock(entry.value,
                    Integer.valueOf(entry.key.split(":".toRegex())[0]),
                    Integer.valueOf(entry.key.split(":".toRegex())[1])))

        return Observable.fromIterable(clocks.sortedWith(compareBy<Clock>{it.hour}.thenBy{it.minute}))
    }

    fun clearAllClocks(context: Context){
        JobManager.instance().cancelAll()
        var clocksPreferences = context.getSharedPreferences(LIST_OF_CLOCKS, Context.MODE_PRIVATE)
        clocksPreferences.edit().clear().commit()

    }

    fun deleteClock(hour: Long, minute: Long,context: Context){
        var clocksPreferences = context.getSharedPreferences(LIST_OF_CLOCKS, Context.MODE_PRIVATE)
        JobManager.instance().cancelAllForTag(TextUtils.getAlarmTagByTimeInString(TextUtils.getTimeString(hour, minute)))
        clocksPreferences.edit().remove(TextUtils.getTimeString(hour, minute)).commit()
        Log.d("Clock Preferences","${TextUtils.getTimeString(hour, minute)} removed")
    }

    fun disableClock(hour: Long, minute: Long,context: Context){
        var clocksPreferences = context.getSharedPreferences(LIST_OF_CLOCKS, Context.MODE_PRIVATE)
        JobManager.instance().cancelAllForTag(TextUtils.getAlarmTagByTimeInString(TextUtils.getTimeString(hour, minute)))
        clocksPreferences.edit().putBoolean(TextUtils.getTimeString(hour, minute),false).commit()
        Log.d("Clock Preferences","${TextUtils.getTimeString(hour, minute)} disabled")
    }
}