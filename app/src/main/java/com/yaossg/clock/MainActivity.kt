package com.yaossg.clock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var clockTextView: TextView
    lateinit var updateButton: Button

    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clockTextView = findViewById(R.id.clockTextView)
        updateButton = findViewById(R.id.updateButton)

        updateButton.setOnClickListener {
            clockTextView.text = SimpleDateFormat("hh:mm:ss", Locale.US).format(Date())
        }

        handler.post(object : Runnable {
            override fun run() {
                updateButton.callOnClick()
                handler.postDelayed(this, 1000)
            }
        })

    }
}