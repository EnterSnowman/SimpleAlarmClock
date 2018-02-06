package com.valentinekuzmich.simplealarmclock.ring

import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.valentinekuzmich.simplealarmclock.R
import com.valentinekuzmich.simplealarmclock.Utils.RingPlayer
import kotlinx.android.synthetic.main.activity_ring.*
import android.media.AudioManager
import android.util.Log
import android.view.View
import com.pacific.timer.Rx2Timer
import com.valentinekuzmich.simplealarmclock.Utils.TextUtils
import com.valentinekuzmich.simplealarmclock.model.UIParamsRepository
import kotlinx.android.synthetic.main.activity_ring_screen_settings.*
import java.util.*


class RingActivity : AppCompatActivity(), IRing.View {
    lateinit var mTimer : Rx2Timer
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        wakeUpScrreen()
        setContentView(R.layout.activity_ring)
        setCurrentTime()
        off.setOnClickListener{ _-> finish()}
        pulsator.color = UIParamsRepository.getPulsatorColor(this)
        setOffButtonBackgroundColor(UIParamsRepository.getButtonColor(this))
        pulsator.visibility = View.INVISIBLE
        //blurBackground()
        pulsator.visibility = View.VISIBLE
        pulsator.start()
        volumeControlStream = AudioManager.STREAM_ALARM
        RingPlayer.playAsRingtone(this)
        startShutdownTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer.stop()
        RingPlayer.stopAsRingtone()
    }

    fun setOffButtonBackgroundColor(color:Int){
        (off.background as GradientDrawable).setColor(color)
    }

    fun setCurrentTime(){
        var c = Calendar.getInstance()
        timeTextView.text = TextUtils.getTimeString(c.get(Calendar.HOUR_OF_DAY).toLong(),c.get(Calendar.MINUTE).toLong())
    }



    fun wakeUpScrreen(){
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }

    fun startShutdownTimer(){
        mTimer = Rx2Timer.builder().initialDelay(0)
                .period(1)
                .take(3*60)
                .onEmit { count -> Log.d("TIMER",count.toString()) }
                .onComplete {
                    Log.d("TIMER","Completed")
                    finish()
                }
                .build()
        mTimer.start()
    }

    /*fun blurBackground(){
        var v  = this.window.decorView
        v.setDrawingCacheEnabled(true)
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        Log.d("Blur"," ${ v.getMeasuredWidth()} ${v.getMeasuredHeight()}")
        v.layout(0, 0, rootView.getMeasuredWidth(), rootView.getMeasuredHeight())
        v.buildDrawingCache()
        val bm =v.drawingCache
        Log.d("Blur"," ${bm.width} ${bm.height}")
        Blurry.with(this)
                .radius(25)
                .sampling(2)
                .from(bm)
                .into(blur)
        rootView.isDrawingCacheEnabled = false
    }*/

}
