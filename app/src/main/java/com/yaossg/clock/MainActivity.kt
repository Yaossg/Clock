package com.yaossg.clock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.JsonReader
import android.util.Log
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.yaossg.clock.data.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var clockTextView: TextView
    lateinit var updateButton: Button
    lateinit var recycler: RecyclerView
    lateinit var recordButton: Button
    lateinit var clearButton: Button
    lateinit var remoteSwitch: SwitchCompat

    val recordListViewModel by lazy { RecordListViewModelFactory(this).create(RecordListViewModel::class.java) }

    val db by lazy { RecordDB(this, 1) }

    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RemoteClock.updater.start()

        clockTextView = findViewById(R.id.clockTextView)
        updateButton = findViewById(R.id.updateButton)
        recycler = findViewById(R.id.recycler)
        recordButton = findViewById(R.id.recordButton)
        clearButton = findViewById(R.id.clearButton)
        remoteSwitch = findViewById(R.id.remoteSwitch)

        updateButton.setOnClickListener {
            if (remoteSwitch.isChecked) {
                clockTextView.text = RemoteClock.now.get()?.current_time ?: "Waiting for server"
                RemoteRecordAdapter.notifyDataSetChanged()
            } else {
                clockTextView.text = SimpleDateFormat("hh:mm:ss", Locale.US).format(Date())
            }
        }

        handler.post(object : Runnable {
            override fun run() {
                updateButton.callOnClick()
                handler.postDelayed(this, 1000)
            }
        })
        val adapter = RecordAdapter()
        recycler.adapter = adapter

        val dataSource = recordListViewModel.dataSource
        dataSource.liveData.observe(this) {
            it?.let {
                db.set(it)
                adapter.submitList(it as MutableList<Record>)
            }
        }

        dataSource.liveData.postValue(db.get())

        recordButton.setOnClickListener {
            dataSource.add(Record(clockTextView.text.toString()))
        }

        clearButton.setOnClickListener {
            db.set(listOf())
            dataSource.liveData.postValue(db.get())
        }

        remoteSwitch.setOnClickListener {
            if (remoteSwitch.isChecked) {
                recycler.adapter = RemoteRecordAdapter
            } else {
                recycler.adapter = adapter
            }
        }
    }
}