package com.mindup.mindup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mindup.mindup.data.repository.GoalRepository

class GoalViewModelFactory(
    private val repository: GoalRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(GoalViewModel::class.java)) {
            return GoalViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}