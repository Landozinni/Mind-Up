package com.mindup.mindup.viewmodel

import androidx.lifecycle.ViewModel
import com.mindup.mindup.DBHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DiaryUiState(
    val selectedMood: String? = null,
    val notes: String = "",
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false
)

class DiaryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    fun onMoodSelected(mood: String) {
        _uiState.update { it.copy(selectedMood = mood, errorMessage = null) }
    }

    fun onNotesChanged(newNotes: String) {
        _uiState.update { it.copy(notes = newNotes, errorMessage = null) }
    }

    fun saveDiaryEntry(dbHelper: DBHelper, onSavedSuccessfully: () -> Unit) {
        val current = _uiState.value

        if (current.selectedMood == null) {
            _uiState.update { it.copy(errorMessage = "Por favor, selecione como você se sente hoje.") }
            return
        }

        if (current.notes.trim().isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Preencha a descrição do seu dia.") }
            return
        }

        _uiState.update { it.copy(isSaving = true, errorMessage = null) }

        val sucesso = dbHelper.salvarEntradaDiario(current.selectedMood, current.notes)

        if (sucesso) {
            _uiState.update {
                it.copy(
                    isSaving = false,
                    selectedMood = null,
                    notes = "",
                    saveSuccess = true,
                    errorMessage = null
                )
            }
            onSavedSuccessfully()
        } else {
            _uiState.update {
                it.copy(
                    isSaving = false,
                    errorMessage = "Erro ao salvar a entrada do diário. Tente novamente."
                )
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun resetSaveStatus() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}
