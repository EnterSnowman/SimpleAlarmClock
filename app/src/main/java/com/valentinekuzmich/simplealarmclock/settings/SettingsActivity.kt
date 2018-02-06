package com.valentinekuzmich.simplealarmclock.settings

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.MenuItem
import android.widget.RadioGroup
import android.widget.SeekBar
import com.valentinekuzmich.simplealarmclock.R
import com.valentinekuzmich.simplealarmclock.Utils.RingPlayer
import com.valentinekuzmich.simplealarmclock.Utils.TextUtils
import com.valentinekuzmich.simplealarmclock.Utils.TextUtils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
import com.valentinekuzmich.simplealarmclock.main.MainActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(),ISettings.View {
    lateinit var mPresenter:SettingsPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mPresenter = SettingsPresenter(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showPermissionRequest()
        //setupUI()

    }

    override fun getContext(): Context  = this
    fun showPermissionRequest(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)

            }
        }
        else{
            setupUI()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == TextUtils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupUI()
            } else {
                // User refused to grant permission.
            }
        }
    }
    fun setupUI(){
        ringtoneName.text = mPresenter.getRingtoneName()
        volumeBar?.max = RingPlayer.getMaxVolume(this)
        volumeBar?.progress = mPresenter.getRingVolume()
        volumeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mPresenter.setRingVolume(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        selectRingtone.setOnClickListener{v ->
            var chooserDialog = AlertDialog.Builder(this)
                    .setTitle(R.string.choose_ringtone_type)
                    .setView(R.layout.ringtone_chooser_layout)
                    .setPositiveButton(android.R.string.ok){dialog, which ->
                        var rg = (dialog as AlertDialog).findViewById<RadioGroup>(R.id.chooser)
                        when(rg!!.checkedRadioButtonId){
                            R.id.system ->{
                                Log.d("RINGTONE PICKER", " system")
                                showSystemRingtoneChooser()
                            }
                            R.id.user ->{
                                Log.d("RINGTONE PICKER", " user")
                                showUserRingtoneChooser()
                            }
                        }
                    }
                    .setNegativeButton(android.R.string.cancel){dialog, which ->  }
                    .create()
            chooserDialog.show()
            chooserDialog.findViewById<RadioGroup>(R.id.chooser)!!.check(when(mPresenter.getTypeOfRing()){
                USER_RING -> R.id.user
                SYSTEM_RING -> R.id.system
                else -> -1
            })


        }

        testPlayButton.setOnClickListener { v ->
            if (!RingPlayer.isPlaying()){
                RingPlayer.playAsRingtone(this)
                testPlayButton.setImageResource(R.drawable.stop)
            }
            else{
                RingPlayer.stopAsRingtone()
                testPlayButton.setImageResource(R.drawable.play)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        RingPlayer.stopAsRingtone()
        testPlayButton.setImageResource(R.drawable.play)
    }

    fun showSystemRingtoneChooser(){
        var i = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        i.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,getString(R.string.choose_ringtone))
        i.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
        startActivityForResult(i, SYSTEM_RINGTONE_PCKR)
    }

    fun showUserRingtoneChooser(){
        val intent = Intent()
        intent.type = "audio/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_ringtone)), USER_RINGTONE_PCKR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("RINGTONE PICKER", "Req code $requestCode, res code $resultCode")
        if (requestCode== SYSTEM_RINGTONE_PCKR && resultCode == Activity.RESULT_OK){
            var ringtoneUri : Uri? = data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            Log.d("RINGTONE PICKER",ringtoneUri?.toString()?: "lul")
            ringtoneUri?.let { mPresenter?.setRingtoneUri(it, SYSTEM_RING) }
            ringtoneName.text = mPresenter.getRingtoneName()
        }
        if (requestCode== USER_RINGTONE_PCKR && resultCode == Activity.RESULT_OK){
            var ringtoneUri : Uri? = data?.data
            Log.d("RINGTONE PICKER",ringtoneUri?.toString()?: "lul")
            ringtoneUri?.let { mPresenter?.setRingtoneUri(it, USER_RING) }
            ringtoneName.text = mPresenter.getRingtoneName()
        }
        //super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home->{
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        const val SYSTEM_RINGTONE_PCKR = 1
        const val USER_RINGTONE_PCKR = 2

        const val SYSTEM_RING =  10
        const val USER_RING = 20
    }
}
