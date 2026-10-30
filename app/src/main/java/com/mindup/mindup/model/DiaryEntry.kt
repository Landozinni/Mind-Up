package com.mindup.mindup.model

data class DiaryEntry(
    val id: Long,
    val mood: String,
    val description: String,
    val createdAt: String
)
