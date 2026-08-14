package com.mindup.mindup.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindup.mindup.data.dao.GoalDao
import com.mindup.mindup.model.Goal

@Database(
    entities = [Goal::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDao

}