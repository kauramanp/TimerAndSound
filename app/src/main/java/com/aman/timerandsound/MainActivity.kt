package com.aman.timerandsound

import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.aman.timerandsound.databinding.ActivityMainBinding
import com.aman.timerandsound.databinding.LayoutDialogBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var timerTime = 0L
    lateinit var timer: CountDownTimer
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(this, R.raw.tick)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnTimer.setOnClickListener {
            var layout = LayoutDialogBinding.inflate(layoutInflater)
            var dialogs = Dialog(this)
            dialogs.setContentView(layout.root)
            dialogs.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialogs.show()
            layout.btnSet.setOnClickListener {
                if (layout.etTime.text.isNullOrEmpty()) {
                    layout.etTime.error = resources.getString(R.string.enter_time)
                    layout.etTime.requestFocus()
                } else {
                    timerTime = layout.etTime.text.toString().toLong()
                    timerTime = timerTime*1000
                    updateTimer()
                    dialogs.dismiss()
                }
            }
            layout.btnCancel.setOnClickListener {
                dialogs.dismiss()
            }
        }

        binding.ibPlay.setOnClickListener {
            if (this::timer.isInitialized) {
                timer.start()
            }else{
                Toast.makeText(this, resources.getString(R.string.enter_time), Toast.LENGTH_LONG).show()
            }
        }
        binding.ibStop.setOnClickListener {
            if (this::timer.isInitialized) {
                binding.tvTime.setText("0")
                timer.cancel()
            }else{
                Toast.makeText(this, resources.getString(R.string.enter_time), Toast.LENGTH_LONG).show()
            }
        }


    }

    fun updateTimer() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        timer = object : CountDownTimer(timerTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("millis", millisUntilFinished.toString())
                binding.tvTime.setText((millisUntilFinished / 1000).toString())
                mediaPlayer.start()
            }

            override fun onFinish() {
                mediaPlayer.stop()

            }
        }
    }

}