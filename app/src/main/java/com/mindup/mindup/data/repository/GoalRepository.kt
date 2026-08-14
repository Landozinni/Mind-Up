package com.mindup.mindup.data.repository

import com.mindup.mindup.data.dao.GoalDao
import com.mindup.mindup.model.Goal
import kotlinx.coroutines.flow.Flow

class GoalRepository(
    private val goalDao: GoalDao
) {

    val allGoals: Flow<List<Goal>> = goalDao.getAllGoals()

    suspend fun insert(goal: Goal) {
        goalDao.insertGoal(goal)
    }

    suspend fun update(goal: Goal) {
        goalDao.updateGoal(goal)
    }

    suspend fun delete(goal: Goal) {
        goalDao.deleteGoal(goal)
    }
}