package com.valentinekuzmich.simplealarmclock.Utils

import com.valentinekuzmich.simplealarmclock.alarm_call.AlarmClockJob

/**
 * Created by Valentine on 23.12.2017.
 */
object TextUtils {
    const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 12
    fun getTimeString(hour: Long, minute: Long): String{
        var res = ""
        res = res.plus(hour.toString()).plus(":")
        if (minute<10)
            res = res.plus("0")
        res = res.plus(minute.toString())
        return res
    }

    fun getAlarmTagByTimeInString(timeInString:String) = AlarmClockJob.TAG.plus(";").plus(timeInString)
}