package com.mindup.mindup.model

data class Goal(

    val emoji: String,

    val title: String,

    val description: String,

    val progress: Float = 0f,

    val progressText: String = "0 de 0 dias"

)