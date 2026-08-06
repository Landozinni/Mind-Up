package com.mindup.mindup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindup.mindup.data.repository.GoalRepository
import com.mindup.mindup.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GoalViewModel(
    private val repository: GoalRepository
) : ViewModel() {

    val goals: Flow<List<Goal>> = repository.allGoals

    fun insertGoal(goal: Goal) {
        viewModelScope.launch {
            repository.insert(goal)
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            repository.update(goal)
        }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            repository.delete(goal)
        }
    }
}