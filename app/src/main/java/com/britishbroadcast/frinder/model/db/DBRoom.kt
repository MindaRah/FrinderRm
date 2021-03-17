package com.britishbroadcast.frinder.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.britishbroadcast.frinder.model.data.HangoutPlace

@Database(entities = [HangoutPlace::class], version = 1, exportSchema = false)
abstract class DBRoom: RoomDatabase() {
    abstract fun hangoutPlaceDao(): HangoutPlaceDao
}