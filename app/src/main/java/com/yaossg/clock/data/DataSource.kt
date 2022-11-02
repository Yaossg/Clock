package com.yaossg.clock.data

import android.content.SharedPreferences
import android.content.res.Resources
import android.database.DatabaseUtils
import androidx.lifecycle.MutableLiveData

class DataSource(resources: Resources) {
    val liveData = MutableLiveData<List<Record>>()

    fun add(record: Record) {
        val currentList = liveData.value
        if (currentList == null) {
            liveData.postValue(listOf(record))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(record)
            liveData.postValue(updatedList)
        }
    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(resources: Resources): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }


}