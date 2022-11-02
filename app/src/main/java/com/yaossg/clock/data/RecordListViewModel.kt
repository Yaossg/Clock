package com.yaossg.clock.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class RecordListViewModel(val dataSource: DataSource): ViewModel()

class RecordListViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordListViewModel::class.java)) {
            return RecordListViewModel(DataSource.getDataSource(context.resources)) as T
        }
        error("Unknown ViewModel class")
    }
}