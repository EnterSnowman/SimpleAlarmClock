package com.valentinekuzmich.simplealarmclock.settings

import android.content.Context
import android.net.Uri

/**
 * Created by Valentine on 16.01.2018.
 */
interface ISettings {
    interface View{
        fun getContext():Context
    }

    interface Presenter{
        fun setRingVolume(volume:Int)

        fun getRingVolume():Int

        fun setRingtoneUri(uri: Uri,type:Int)

        fun getTypeOfRing():Int

        fun getRingtoneName():String
    }
}