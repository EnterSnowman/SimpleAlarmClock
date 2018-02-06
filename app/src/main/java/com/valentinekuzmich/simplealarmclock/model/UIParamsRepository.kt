package com.valentinekuzmich.simplealarmclock.model

import android.content.Context
import android.graphics.Color

/**
 * Created by Valentine on 16.01.2018.
 */
object UIParamsRepository {
    const val UI_PARAMS = "UI_PARAMS"
    const val PULSATOR_DURATION  = "PULSATOR_DURATION"
    const val PULSATOR_COUNT = "PULSATOR_COUNT"
    const val PULSATOR_INTERPOLATOR = "PULSATOR_INTERPOLATOR"
    const val PULSATOR_COLOR = "PULSATOR_COLOR"
    const val BUTTON_COLOR = "BUTTON_COLOR"

    object Interpolators{
        const val LINEAR = "Linear"
        const val ACCELERATE = "Accelerate"
        const val DECELERATE = "Decelerate"
        const val ACCELERATE_DECELERATE = "AccelerateDecelerate"
    }

    fun setPulsatorDuration(duration:Int,context: Context){
        context.getSharedPreferences(UI_PARAMS,Context.MODE_PRIVATE).edit()
                .putInt(PULSATOR_DURATION,duration)
                .apply()
    }

    fun setPulsatorCount(count:Int,context: Context){
        context.getSharedPreferences(UI_PARAMS,Context.MODE_PRIVATE).edit()
                .putInt(PULSATOR_COUNT,count)
                .apply()
    }

    fun setPulsatorInterpolator(interpolator:String,context: Context){
        context.getSharedPreferences(UI_PARAMS,Context.MODE_PRIVATE).edit()
                .putString(PULSATOR_INTERPOLATOR,interpolator)
                .apply()
    }

    fun getPulsatorDuration(context: Context):Int =
            context.getSharedPreferences(UI_PARAMS,Context.MODE_PRIVATE).getInt(PULSATOR_DURATION,1000)

    fun getPulsatorCount(context: Context):Int =
            context.getSharedPreferences(UI_PARAMS,Context.MODE_PRIVATE).getInt(PULSATOR_COUNT,0)

    fun getPulsatorInterpolator(context: Context):String =
            context.getSharedPreferences(UI_PARAMS,Context.MODE_PRIVATE).getString(PULSATOR_INTERPOLATOR,Interpolators.LINEAR)

    fun setPulsatorColor(color:Int,context: Context){
        context.getSharedPreferences(UI_PARAMS,Context.MODE_PRIVATE).edit()
                .putInt(PULSATOR_COLOR,color)
                .apply()
    }

    fun setButtonColor(color:Int,context: Context){
        context.getSharedPreferences(UI_PARAMS,Context.MODE_PRIVATE).edit()
                .putInt(BUTTON_COLOR,color)
                .apply()
    }

    fun getButtonColor(context: Context) = context.getSharedPreferences(UI_PARAMS,Context.MODE_PRIVATE).getInt(BUTTON_COLOR, Color.RED)

    fun getPulsatorColor(context: Context) = context.getSharedPreferences(UI_PARAMS,Context.MODE_PRIVATE).getInt(PULSATOR_COLOR,Color.GREEN)
}