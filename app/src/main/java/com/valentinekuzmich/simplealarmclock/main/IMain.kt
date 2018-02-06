package com.valentinekuzmich.simplealarmclock.main

import android.content.Context
import android.net.Uri
import com.valentinekuzmich.simplealarmclock.model.Clock


/**
 * Created by Valentine on 07.12.2017.
 */
interface IMain {
    interface View{
        fun showClock(clock: Clock)

        fun getContext(): Context
    }

    interface Presenter{
        fun showClock()

        fun addClock(hour: Long,minute:Long)

        fun deleteClock(hour: Long,minute:Long)

        fun enableClock(hour: Long,minute:Long)

        fun disableClock(hour: Long,minute:Long)

        fun setRingVolume(volume:Int)
    }
}