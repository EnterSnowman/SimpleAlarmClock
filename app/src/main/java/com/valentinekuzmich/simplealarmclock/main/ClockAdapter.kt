package com.valentinekuzmich.simplealarmclock.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.valentinekuzmich.simplealarmclock.R
import com.valentinekuzmich.simplealarmclock.Utils.TextUtils
import com.valentinekuzmich.simplealarmclock.model.Clock


/**
 * Created by Valentine on 07.12.2017.
 */
class ClockAdapter(var mListener: ((View?, Int) -> Unit),var mEnableListener: (Int,Boolean)-> Unit): RecyclerView.Adapter<ClockAdapter.ClockHolder>() {
    var mClocks: ArrayList<Clock>

    init {
        mClocks = ArrayList<Clock>()
    }

    override fun onBindViewHolder(holder: ClockHolder?, position: Int) {
        holder?.timeOfClock?.text =TextUtils.getTimeString(mClocks[position].hour.toLong(), mClocks[position].minute.toLong())
        holder?.clockEnabledCheckBox?.isChecked = mClocks[position].isEnable
    }

    override fun getItemCount(): Int  = mClocks.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ClockHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.clock_item, parent, false)
        return ClockHolder(v,mListener!!,mEnableListener)
    }

    class ClockHolder(itemView: View?,var mListener: (View?, Int) -> Unit,var mEnableListener: (Int,Boolean)-> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            mEnableListener(adapterPosition,isChecked)
        }

        var timeOfClock : TextView
        var deleteButton : ImageView
        var clockEnabledCheckBox: CheckBox
        init {
            timeOfClock = itemView!!.findViewById(R.id.timeOfClock) as TextView
            deleteButton = itemView!!.findViewById(R.id.deleteAlarm) as ImageView
            deleteButton.setOnClickListener(this)
            clockEnabledCheckBox = itemView!!.findViewById(R.id.clockEnabled) as CheckBox
            clockEnabledCheckBox.setOnCheckedChangeListener(this)
        }

        override fun onClick(v: View?) {
            mListener(v,adapterPosition)
        }
    }
}