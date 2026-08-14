package com.mindup.mindup.viewmodel

import androidx.lifecycle.ViewModel
import com.mindup.mindup.DBHelper
import com.mindup.mindup.model.Dass21Data
import com.mindup.mindup.model.Dass21Evaluation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Dass21UiState(
    val answers: Map<Int, Int> = emptyMap(),
    val showInstructionDetails: Boolean = false,
    val evaluation: Dass21Evaluation? = null,
    val showResultDialog: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
) {
    val totalCount: Int = Dass21Data.questions.size
    val answeredCount: Int = answers.size
    val isComplete: Boolean = answeredCount == totalCount
    val progress: Float = if (totalCount > 0) answeredCount.toFloat() / totalCount.toFloat() else 0f
}

class Dass21ViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(Dass21UiState())
    val uiState: StateFlow<Dass21UiState> = _uiState.asStateFlow()

    fun onAnswerSelected(questionId: Int, optionValue: Int) {
        _uiState.update { current ->
            val updatedAnswers = current.answers.toMutableMap().apply {
                put(questionId, optionValue)
            }
            current.copy(answers = updatedAnswers)
        }
    }

    fun toggleInstructions() {
        _uiState.update { current ->
            current.copy(showInstructionDetails = !current.showInstructionDetails)
        }
    }

    fun submitQuestionnaire(dbHelper: DBHelper? = null) {
        val currentAnswers = _uiState.value.answers
        if (currentAnswers.size < Dass21Data.questions.size) {
            _uiState.update { current ->
                current.copy(errorMessage = "Por favor, responda a todas as 21 perguntas antes de finalizar.")
            }
            return
        }

        val eval = Dass21Data.calculateEvaluation(currentAnswers)

        var saved = false
        if (dbHelper != null) {
            saved = dbHelper.salvarResultadoDass21(
                depressaoScore = eval.depression.finalScore,
                depressaoClassificacao = eval.depression.severity.label,
                ansiedadeScore = eval.anxiety.finalScore,
                ansiedadeClassificacao = eval.anxiety.severity.label,
                estresseScore = eval.stress.finalScore,
                estresseClassificacao = eval.stress.severity.label
            )
        }

        _uiState.update { current ->
            current.copy(
                evaluation = eval,
                showResultDialog = true,
                isSaved = saved,
                errorMessage = null
            )
        }
    }

    fun dismissResultDialog() {
        _uiState.update { current ->
            current.copy(showResultDialog = false)
        }
    }

    fun resetQuestionnaire() {
        _uiState.value = Dass21UiState()
    }
}
