package com.valentinekuzmich.simplealarmclock.settings

import android.net.Uri
import com.valentinekuzmich.simplealarmclock.model.RingRepository

/**
 * Created by Valentine on 16.01.2018.
 */
class SettingsPresenter(var  mView:ISettings.View):ISettings.Presenter {
    override fun getRingtoneName(): String  = RingRepository.getNameOfAudioByUri(mView.getContext())

    override fun getTypeOfRing(): Int  = RingRepository.getTypeOfRing(mView.getContext())

    override fun setRingtoneUri(uri: Uri,type:Int) {
        RingRepository.setRingtoneUri(uri,mView.getContext(),type)
    }

    override fun setRingVolume(volume: Int) {
        RingRepository.setRingVolume(mView.getContext(),volume)
    }

    override fun getRingVolume(): Int = RingRepository.getRingVolume(mView.getContext())


}