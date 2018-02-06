package com.valentinekuzmich.simplealarmclock.main

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatSeekBar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TimePicker
import com.valentinekuzmich.simplealarmclock.R
import com.valentinekuzmich.simplealarmclock.Utils.RingPlayer
import com.valentinekuzmich.simplealarmclock.Utils.TextUtils
import com.valentinekuzmich.simplealarmclock.model.Clock
import com.valentinekuzmich.simplealarmclock.model.ClockRepository
import com.valentinekuzmich.simplealarmclock.ring.RingActivity
import com.valentinekuzmich.simplealarmclock.ring_screen_settings.RingScreenSettingsActivity
import com.valentinekuzmich.simplealarmclock.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),IMain.View {
    var mPresenter: MainPresenter? = null
    var mClockAdapter : ClockAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter = MainPresenter(this)
        mClockAdapter = ClockAdapter({v,position->
            mPresenter!!.deleteClock(mClockAdapter!!.mClocks[position].hour.toLong(), mClockAdapter!!.mClocks[position].minute.toLong())
            mClockAdapter!!.mClocks.removeAt(position)
            mClockAdapter!!.notifyDataSetChanged()
        },{pos,isChecked ->
            mClockAdapter!!.mClocks[pos].isEnable = isChecked
            if (isChecked)
                mPresenter!!.enableClock(mClockAdapter!!.mClocks[pos].hour.toLong(),mClockAdapter!!.mClocks[pos].minute.toLong())
            else
                mPresenter!!.disableClock(mClockAdapter!!.mClocks[pos].hour.toLong(), mClockAdapter!!.mClocks[pos].minute.toLong())
        })
        clockList.adapter = mClockAdapter
        clockList.layoutManager = LinearLayoutManager(this)
        mPresenter!!.showClock()
        var am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        Log.d("VOLUME","${am.getStreamVolume(AudioManager.STREAM_ALARM)}")
        Log.d("VOLUME","max volume ${am.getStreamMaxVolume(AudioManager.STREAM_ALARM)}")
    }

    override fun getContext(): Context {
        return this
    }

    override fun showClock(clock: Clock) {
        mClockAdapter!!.mClocks.add(clock)
        mClockAdapter!!.mClocks = ArrayList<Clock>(mClockAdapter!!.mClocks.sortedWith(compareBy<Clock>{it.hour}.thenBy { it.minute }))
        mClockAdapter!!.notifyDataSetChanged()
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var id = item!!.itemId
        when(id){
            R.id.add_clock ->{
                showAddNewClockDialog()
            }
            R.id.testRing ->{
                var i = Intent(this, RingScreenSettingsActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
            }

            R.id.choose_ringtone ->{
                var i = Intent(this, SettingsActivity::class.java)
                startActivity(i)
                //startRingtonePicker()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun showAddNewClockDialog(){
        val timePickerDialog = TimePickerDialog(this,
                { view: TimePicker?, hourOfDay: Int, minute: Int ->
                    if (view!!.isShown) {
                        Log.d("Clock Scheduler", "clock set $hourOfDay $minute")
                        mPresenter!!.addClock(hourOfDay.toLong(), minute.toLong())
                    }
                },
                Calendar.getInstance().time.hours, Calendar.getInstance().time.minutes, true)

        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel)) { dialogInterface, i ->
            if (i == DialogInterface.BUTTON_NEGATIVE) {
                timePickerDialog.dismiss()
            }
        }
        timePickerDialog.show()
    }


}
