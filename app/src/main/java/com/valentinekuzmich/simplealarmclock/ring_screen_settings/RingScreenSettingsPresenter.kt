package com.valentinekuzmich.simplealarmclock.ring_screen_settings

import com.valentinekuzmich.simplealarmclock.model.UIParamsRepository

/**
 * Created by Valentine on 16.01.2018.
 */
class RingScreenSettingsPresenter(var mView: IRingScreenSettings.View):IRingScreenSettings.Presenter {
    override fun getPulsatorColor(): Int =
        UIParamsRepository.getPulsatorColor(mView.getContext())

    override fun getButtonColor(): Int =
            UIParamsRepository.getButtonColor(mView.getContext())

    override fun setCount(count: Int) {
        UIParamsRepository.setPulsatorCount(count,mView.getContext())
    }

    override fun setDuration(duration: Int) {
        UIParamsRepository.setPulsatorDuration(duration,mView.getContext())
    }

    override fun setInterpolator(interpolator: String) {
        UIParamsRepository.setPulsatorInterpolator(interpolator,mView.getContext())
    }

    override fun setPulsatorColor(color:Int) {
        UIParamsRepository.setPulsatorColor(color,mView.getContext())
    }

    override fun setButtonColor(color:Int) {
        UIParamsRepository.setButtonColor(color,mView.getContext())
    }

    override fun getDuration(): Int  = UIParamsRepository.getPulsatorDuration(mView.getContext())

    override fun getInterpolator(): String  = UIParamsRepository.getPulsatorInterpolator(mView.getContext())

    override fun getCount(): Int  = UIParamsRepository.getPulsatorCount(mView.getContext())


}