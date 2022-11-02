package com.yaossg.clock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.yaossg.clock.data.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var clockTextView: TextView
    lateinit var updateButton: Button
    lateinit var recycler: RecyclerView
    lateinit var recordButton: Button

    val recordListViewModel by lazy { RecordListViewModelFactory(this).create(RecordListViewModel::class.java) }

    val db by lazy { RecordDB(this, 1) }

    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clockTextView = findViewById(R.id.clockTextView)
        updateButton = findViewById(R.id.updateButton)
        recycler = findViewById(R.id.recycler)
        recordButton = findViewById(R.id.recordButton)

        updateButton.setOnClickListener {
            clockTextView.text = SimpleDateFormat("hh:mm:ss", Locale.US).format(Date())
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
    }
}