package com.valentinekuzmich.simplealarmclock.model

import android.content.Context
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import com.valentinekuzmich.simplealarmclock.R
import java.io.File
import android.content.ContentValues.TAG
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log



/**
 * Created by Valentine on 02.01.2018.
 */
object RingRepository {
    val RINGTONE_URI = "ringtoneUri"
    val URI = "uri"
    val VOLUME = "volume"
    val TYPE = "type"
    fun getRingId(): Int  =  R.raw.alarm

    fun setRingtoneUri(ringtoneUri:Uri,context:Context,type:Int){
        var ringtonePreferences = context.getSharedPreferences(RINGTONE_URI, Context.MODE_PRIVATE)
        ringtonePreferences.edit()
                .putString(URI,ringtoneUri.toString())
                .putInt(TYPE,type)
                .commit()
    }

    fun getRingtoneUri(context: Context):Uri =
            Uri.parse(context.getSharedPreferences(RINGTONE_URI, Context.MODE_PRIVATE)
            .getString(URI,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()))

    fun getRingVolume(context: Context):Int = context
            .getSharedPreferences(RINGTONE_URI, Context.MODE_PRIVATE).getInt(VOLUME,
            (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).getStreamVolume(AudioManager.STREAM_ALARM))

    fun setRingVolume(context: Context,volume:Int){
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
                .setStreamVolume(AudioManager.STREAM_ALARM,volume,0)
        context.getSharedPreferences(RINGTONE_URI, Context.MODE_PRIVATE)
                .edit()
                .putInt(VOLUME,volume)
                .commit()
    }

    fun getDefaultRingtoneUri() = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

    fun getTypeOfRing(context: Context):Int  =context.getSharedPreferences(RINGTONE_URI, Context.MODE_PRIVATE).getInt(TYPE,-1)

    fun getNameOfAudioByUri(context: Context):String{
        var uri = getRingtoneUri(context)
        var fileName = ""
        Log.d(TAG, "uri is " + uri.toString())
        if (!uri.equals(getDefaultRingtoneUri())) {

            if (uri.getScheme().equals("file")) {
                fileName = uri.getLastPathSegment()
            } else {
               var cursor: Cursor? = null
               try {
                    cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.TITLE), null, null, null)
                    if (cursor != null && cursor!!.moveToFirst()) {
                        fileName = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Images.ImageColumns.TITLE))
                        Log.d(TAG, "name is " + fileName)
                 }
              } finally {
                 if (cursor != null) {
                        cursor!!.close()
                 }
            }
        }
        }
        else{
            fileName = context.getString(R.string.default_ring)
        }
        return fileName
    }
}