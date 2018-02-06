package com.valentinekuzmich.simplealarmclock.Utils

import android.content.Context
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.util.Log
import com.valentinekuzmich.simplealarmclock.R
import com.valentinekuzmich.simplealarmclock.model.RingRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import io.reactivex.disposables.Disposable
import android.media.AudioAttributes
import android.os.Build





/**
 * Created by Valentine on 02.01.2018.
 */
object RingPlayer {
    var ringtone : Ringtone? = null

    fun isPlaying() = ringtone?.isPlaying ?: false

    fun playAsRingtone(context:Context){
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
                .setStreamVolume(AudioManager.STREAM_ALARM,RingRepository.getRingVolume(context),0)
        ringtone = RingtoneManager.getRingtone(context, RingRepository.getRingtoneUri(context))
        if (Build.VERSION.SDK_INT >= 21) {
            val aa = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            ringtone!!.audioAttributes = aa
        } else {
            ringtone!!.streamType = AudioManager.STREAM_ALARM
        }
        ringtone!!.play()

    }

    fun stopAsRingtone(){
        ringtone?.stop()
    }

    fun getMaxVolume(context: Context):Int =
            (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
                    .getStreamMaxVolume(AudioManager.STREAM_ALARM)

}