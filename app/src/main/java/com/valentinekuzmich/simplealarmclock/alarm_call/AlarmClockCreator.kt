package com.valentinekuzmich.simplealarmclock.alarm_call

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

/**
 * Created by Valentine on 22.12.2017.
 */
class AlarmClockCreator : JobCreator {
    override fun create(tag: String): Job? {
        when{
            tag.split(";")[0].equals(AlarmClockJob.TAG)->{
                return AlarmClockJob()
            }
            else ->{
                return null
            }
        }

    }
}