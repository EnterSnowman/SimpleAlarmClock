package com.valentinekuzmich.simplealarmclock

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.evernote.android.job.JobManager
import com.valentinekuzmich.simplealarmclock.Utils.RingPlayer
import com.valentinekuzmich.simplealarmclock.alarm_call.AlarmClockCreator


/**
 * Created by Valentine on 22.12.2017.
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(AlarmClockCreator())
        //android.support.multidex.MultiDex.install(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }


}