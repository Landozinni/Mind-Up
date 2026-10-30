package com.mindup.mindup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindup.mindup.data.repository.GoalRepository
import com.mindup.mindup.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch

class GoalViewModel(
    private val repository: GoalRepository
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()
    val goals: Flow<List<Goal>> = repository.allGoals.catch {
        _message.value = "Não foi possível carregar suas metas. Tente novamente."
        emit(emptyList())
    }

    fun clearMessage() { _message.value = null }

    fun insertGoal(goal: Goal) {
        viewModelScope.launch {
            runCatching { repository.insert(goal) }
                .onSuccess { _message.value = "Meta criada com sucesso." }
                .onFailure { _message.value = "Não foi possível salvar. Tente novamente." }
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            runCatching { repository.update(goal) }
                .onSuccess { _message.value = "Meta atualizada com sucesso." }
                .onFailure { _message.value = "Não foi possível atualizar. Tente novamente." }
        }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            runCatching { repository.delete(goal) }
                .onSuccess { _message.value = "Meta excluída." }
                .onFailure { _message.value = "Não foi possível excluir. Tente novamente." }
        }
    }
}
