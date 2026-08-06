package com.mindup.mindup.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val emoji: String,

    val title: String,

    val description: String,

    val progress: Float = 0f,

    val progressText: String = "0 de 0 dias"
)