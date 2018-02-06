package com.valentinekuzmich.simplealarmclock.main

import android.net.Uri
import android.util.Log
import com.valentinekuzmich.simplealarmclock.model.Clock
import com.valentinekuzmich.simplealarmclock.model.ClockRepository
import com.valentinekuzmich.simplealarmclock.model.RingRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by Valentine on 07.12.2017.
 */
class MainPresenter(var mView: IMain.View): IMain.Presenter {
    override fun setRingVolume(volume: Int) {
        RingRepository.setRingVolume(mView.getContext(),volume)
    }

    override fun enableClock(hour: Long, minute: Long) {
        if (!ClockRepository.isClockExist(hour, minute))
        ClockRepository.addClock(hour, minute,mView.getContext())
    }

    override fun disableClock(hour: Long, minute: Long) {

        ClockRepository.disableClock(hour,minute,mView.getContext())
    }

    override fun deleteClock(hour: Long, minute: Long) {
        ClockRepository.deleteClock(hour,minute,mView.getContext())
    }

    override fun addClock(hour: Long, minute: Long) {
            ClockRepository.addClock(hour, minute,mView.getContext()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{clock ->
                    Log.d("Rx","addClock Clock Received ${clock.hour} ${clock.minute}")
                    mView.showClock(clock) }
    }

    override fun showClock() {
        ClockRepository.getListOfClocks(mView.getContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { clock ->
                    Log.d("Rx","showClock Clock Received ${clock.hour} ${clock.minute}")
                    mView.showClock(clock)
                }

    }



}