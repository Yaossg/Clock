package com.yaossg.clock.data

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yaossg.clock.R

object RecordDiffCallback : DiffUtil.ItemCallback<Record>() {
    override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem == newItem
    }

}

open class RecordAdapter : ListAdapter<Record, RecordAdapter.RecordViewHolder>(RecordDiffCallback) {
    class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recordTextView: TextView = itemView.findViewById(R.id.recyclerText)
        private var currentRecord: Record? = null

        fun bind(record: Record) {
            currentRecord = record

            recordTextView.text = record.time
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = getItem(position)
        holder.bind(record)
    }
}

object RemoteRecordAdapter: RecordAdapter() {
    override fun getItem(position: Int): Record {
        return Record(RemoteClock.now.get()?.timelist?.get(position) ?: "Waiting for server")
    }

    override fun getItemCount(): Int {
        return RemoteClock.now.get()?.timelist?.size ?: 0
    }
}