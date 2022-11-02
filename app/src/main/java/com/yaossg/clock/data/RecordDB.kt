package com.yaossg.clock.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RecordDB(context: Context, version: Int): SQLiteOpenHelper(context, "record.db", null, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table Records(time text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun add(record: Record) {
        val contentValues = ContentValues()
        contentValues.put("time", record.time)
        writableDatabase.insert("Records", null, contentValues)
    }

    fun set(records: List<Record>) {
        writableDatabase.delete("Records", null, null)
        records.forEach { add(it) }
    }

    fun get(): List<Record> {
        return readableDatabase.query(
            "Records",
            null,
            null,
            null,
            null,
            null,
            null).use { cursor->
            val ret = mutableListOf<Record>()
            while (cursor.moveToNext()) {
                ret += Record(cursor.getString(cursor.getColumnIndex("time")))
            }
            ret
        }
    }
}