package com.valentinekuzmich.simplealarmclock.ring_screen_settings

import android.content.Context

/**
 * Created by Valentine on 16.01.2018.
 */
interface IRingScreenSettings {
    interface View{
        fun getContext():Context
    }
    interface Presenter{
        fun getCount():Int

        fun getDuration():Int

        fun getInterpolator():String

        fun getPulsatorColor():Int

        fun getButtonColor():Int

        fun setCount(count:Int)

        fun setDuration(duration:Int)

        fun setInterpolator(interpolator:String)

        fun setPulsatorColor(color:Int)

        fun setButtonColor(color:Int)
    }
}