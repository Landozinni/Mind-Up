package com.mindup.mindup

import android.content.Context
import androidx.room.Room
import com.mindup.mindup.data.database.AppDatabase
import com.mindup.mindup.data.repository.GoalRepository

class AppContainer(context: Context) {

    private val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "mindup_database"
    ).build()

    val goalRepository = GoalRepository(database.goalDao())

}