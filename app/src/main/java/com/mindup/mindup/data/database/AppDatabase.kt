package com.mindup.mindup.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mindup.mindup.data.dao.GoalDao
import com.mindup.mindup.model.Goal

@Database(
    entities = [Goal::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDao

}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE goals_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, emoji TEXT NOT NULL, title TEXT NOT NULL, description TEXT NOT NULL, progress REAL NOT NULL, progressText TEXT NOT NULL, category TEXT NOT NULL, targetValue INTEGER NOT NULL, currentValue INTEGER NOT NULL, dueDate TEXT NOT NULL, frequency TEXT NOT NULL)")
        database.execSQL("INSERT INTO goals_new (id, emoji, title, description, progress, progressText, category, targetValue, currentValue, dueDate, frequency) SELECT id, emoji, title, description, progress, progressText, 'Pessoal', CASE WHEN progressText LIKE '% de %' AND CAST(substr(progressText, instr(progressText, ' de ') + 4) AS INTEGER) > 0 THEN CAST(substr(progressText, instr(progressText, ' de ') + 4) AS INTEGER) ELSE 1 END, 0, '', '' FROM goals")
        database.execSQL("UPDATE goals_new SET currentValue = min(targetValue, max(0, CAST(round(progress * targetValue) AS INTEGER)))")
        database.execSQL("DROP TABLE goals")
        database.execSQL("ALTER TABLE goals_new RENAME TO goals")
    }
}
