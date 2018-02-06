package com.valentinekuzmich.simplealarmclock.ring_screen_settings

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AccelerateInterpolator
import com.valentinekuzmich.simplealarmclock.R
import kotlinx.android.synthetic.main.activity_ring_screen_settings.*
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.SeekBar
import com.mancj.slideup.SlideUp
import com.mancj.slideup.SlideUpBuilder
import com.valentinekuzmich.simplealarmclock.Utils.FixedPulsatorLayout
import com.valentinekuzmich.simplealarmclock.Utils.RingPlayer
import com.valentinekuzmich.simplealarmclock.Utils.TextUtils
import com.valentinekuzmich.simplealarmclock.model.UIParamsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

import java.util.*
import java.util.concurrent.TimeUnit


class RingScreenSettingsActivity : AppCompatActivity(),IRingScreenSettings.View {
    lateinit var colorInterpolator : AccelerateInterpolator
    lateinit var uiInterpolator : AccelerateInterpolator
    lateinit var mPresenter : RingScreenSettingsPresenter
    var shift : Int = 0
    var colorShift : Int = 0
    lateinit var colorSlideUp:SlideUp
    lateinit var slideUp:SlideUp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_ring_screen_settings)
        mPresenter = RingScreenSettingsPresenter(this)
        colorInterpolator = AccelerateInterpolator(1.0f)
        uiInterpolator = AccelerateInterpolator(1.0f)
        setOffButtonBackgroundColor(mPresenter.getButtonColor())
        pulsatorTest.color = mPresenter.getPulsatorColor()
        pulsatorTest.start()
        Log.d("UI","slideView height before build ${settingsView.layoutParams.height.toFloat()}")
        settingsView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                shift = settingsView.height
                Log.d("UI","shift $shift")
                buildSlideView()
                setupSettingsUI()
                settingsView.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })

        colorSettings.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                colorShift = colorSettings.height
                Log.d("UI","colorShift $colorShift")
                buildColorSlideView()
                setupColorPicker()
                colorSettings.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
    }

    fun setCurrentTime(){
        var c = Calendar.getInstance()
        timeTextViewTest.text = TextUtils.getTimeString(c.get(Calendar.HOUR_OF_DAY).toLong(),c.get(Calendar.MINUTE).toLong())
    }

    fun setupSettingsUI(){
        countSeekBar.max = 9
        countSeekBar.progress = mPresenter.getCount()-1
        pulse_count.text = " "+mPresenter.getCount().toString()
        pulsatorTest.count = mPresenter.getCount()
        durationSeekBar.max = 10000
        durationSeekBar.progress = mPresenter.getDuration()
        pulse_duration.text = " "+mPresenter.getDuration().toString()+" ms"
        pulsatorTest.duration = mPresenter.getDuration()
        setCurrentTime()
        interpolator_chooser.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.linear_interpolator ->{
                    mPresenter.setInterpolator(UIParamsRepository.Interpolators.LINEAR)
                    pulsatorTest.interpolator = FixedPulsatorLayout.INTERP_LINEAR
                }
                R.id.accelerate_interpolator ->{
                    mPresenter.setInterpolator(UIParamsRepository.Interpolators.ACCELERATE)
                    pulsatorTest.interpolator = FixedPulsatorLayout.INTERP_ACCELERATE
                }
                R.id.decelerate_interpolator ->{
                    mPresenter.setInterpolator(UIParamsRepository.Interpolators.DECELERATE)
                    pulsatorTest.interpolator = FixedPulsatorLayout.INTERP_DECELERATE
                }
                R.id.accelerateDecelerate_interpolator ->{
                    mPresenter.setInterpolator(UIParamsRepository.Interpolators.ACCELERATE_DECELERATE)
                    pulsatorTest.interpolator = FixedPulsatorLayout.INTERP_ACCELERATE_DECELERATE
                }
            }
        }

        interpolator_chooser.check( when(mPresenter.getInterpolator()){
            UIParamsRepository.Interpolators.LINEAR -> R.id.linear_interpolator
            UIParamsRepository.Interpolators.ACCELERATE -> R.id.accelerate_interpolator
            UIParamsRepository.Interpolators.DECELERATE -> R.id.decelerate_interpolator
            UIParamsRepository.Interpolators.ACCELERATE_DECELERATE -> R.id.accelerateDecelerate_interpolator
            else ->R.id.linear_interpolator
        })

        //set listeners
        countSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pulsatorTest.count = progress + 1
                Log.d("UI","count ${progress+1}")
                mPresenter.setCount(progress+1)
                pulse_count.text = " "+(progress+1).toString()
            }

        })

        durationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pulsatorTest.duration = progress
                Log.d("UI","duration $progress")
                mPresenter.setDuration(progress)
                pulse_duration.text = " "+progress.toString()+" ms"
            }

        })
    }

    fun setupColorPicker(){
        picker.addSVBar(svbar)
        picker.showOldCenterColor = false
        colorSwitcher.setThumbResource(R.drawable.left_thumb)
        picker.color = mPresenter.getPulsatorColor()
        //colorSwitcher.setTrackResource(R.color.color_switcher_track)
        colorSwitcher.setOnCheckedChangeListener{buttonView, isChecked ->
            if (isChecked){
                colorSwitcher.setThumbResource(R.drawable.right_thumb)
                pulsationLabel.setTextColor(resources.getColor(R.color.disabled_text))
                closeButtonLabel.setTextColor(resources.getColor(R.color.black))
                picker.color = mPresenter.getButtonColor()
            }
            else{
                colorSwitcher.setThumbResource(R.drawable.left_thumb)
                pulsationLabel.setTextColor(resources.getColor(R.color.black))
                closeButtonLabel.setTextColor(resources.getColor(R.color.disabled_text))
                picker.color = mPresenter.getPulsatorColor()
            }
        }
        pulsationLabel.setTextColor(resources.getColor(R.color.black))
        closeButtonLabel.setTextColor(resources.getColor(R.color.disabled_text))
        //create observable of color changes
        var colorObservable = PublishSubject.create<Int>()
        colorObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(50, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe { c ->
                    if (!colorSwitcher.isChecked)
                        mPresenter.setPulsatorColor(c)
                    else
                        mPresenter.setButtonColor(c)
                }
        picker.setOnColorChangedListener { color ->
            Log.d("Color","picked color: $color")
            if (!colorSwitcher.isChecked){
                pulsatorTest.color = color
            }
            else
                setOffButtonBackgroundColor(color)
            colorObservable.onNext(color)
        }
    }

    fun setOffButtonBackgroundColor(color:Int){
        (off_test.background as GradientDrawable).setColor(color)
    }
    fun buildSlideView(){
        //pulsation settings
        Log.d("UI","slideView height before build ${settingsView.layoutParams.height.toFloat()}")
        slideUp = SlideUpBuilder(settingsView)
                .withStartGravity(Gravity.TOP)
                .withLoggingEnabled(true)
                //.withInterpolator(uiInterpolator)
                .withListeners(object : SlideUp.Listener.Events {
                    override fun onVisibilityChanged(visibility: Int) {

                    }

                    override fun onSlide(percent: Float) {

                    }

                })
                .build()
        slideFab.setOnClickListener { v ->
            if (!slideUp.isVisible){
                //Log.d("UI","slideView height ${settingsView.layoutParams.height.toFloat()}")
                //Log.d("UI","sliderUp height ${slideUp.getSliderView<LinearLayout>().layoutParams.height.toFloat()}")
                showUISlide()
                colorSlideFab.hide()
            }
            else{
                hideUISlide()
                colorSlideFab.show()
            }
        }


    }

    fun buildColorSlideView(){
        //color settings
        colorSlideUp = SlideUpBuilder(colorSettings)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.TOP)
                .withLoggingEnabled(true)
                .withListeners(object : SlideUp.Listener.Events {
                    override fun onVisibilityChanged(visibility: Int) {

                    }

                    override fun onSlide(percent: Float) {

                    }

                })
                //.withHideSoftInputWhenDisplayed(true)
                .build()
        colorSlideFab.setOnClickListener { v ->
            if (!colorSlideUp.isVisible){
                //Log.d("UI","slideView height ${settingsView.layoutParams.height.toFloat()}")
                //Log.d("UI","sliderUp height ${slideUp.getSliderView<LinearLayout>().layoutParams.height.toFloat()}")
                showColorSlide()
                slideFab.hide()
            }
            else{
                hideColorSlide()
                slideFab.show()
            }
        }
    }

    fun showColorSlide(){
        var i = colorSlideUp.interpolator
        colorSlideUp.show()
        //colorSlideFab.animate().translationY(-colorShift.toFloat()).setInterpolator(i).start()
    }

    fun hideColorSlide(){
        if (colorSlideUp.isVisible) {
            var i = colorSlideUp.interpolator
            colorSlideUp.hide()
            //colorSlideFab.animate().translationY(0f).setInterpolator(i).start()

        }
    }


    fun showUISlide(){
        var i = slideUp.interpolator
        slideUp.show()
        //slideFab.animate().translationY(-shift.toFloat()).setInterpolator(i).start()
    }

    fun hideUISlide(){
        if (slideUp.isVisible) {
            //var i = slideUp.interpolator
            slideUp.hide()
            //slideFab.animate().translationY(0f).setInterpolator(i).start()

        }
    }

    override fun getContext(): Context  = this



}
