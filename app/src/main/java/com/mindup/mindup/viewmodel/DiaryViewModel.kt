package com.mindup.mindup.viewmodel

import androidx.lifecycle.ViewModel
import com.mindup.mindup.DBHelper
import com.mindup.mindup.DiarySaveResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DiaryUiState(
    val selectedMood: String? = null,
    val notes: String = "",
    val isSaving: Boolean = false,
    val diaryAlreadyExists: Boolean = false,
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false
)

class DiaryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    fun loadDiaryStatus(dbHelper: DBHelper) {
        val exists = dbHelper.possuiEntradaDiario()
        _uiState.update { it.copy(diaryAlreadyExists = exists) }
    }

    fun onMoodSelected(mood: String) {
        _uiState.update {
            it.copy(
                selectedMood = mood,
                errorMessage = null
            )
        }
    }

    fun onNotesChanged(newNotes: String) {
        _uiState.update {
            it.copy(
                notes = newNotes,
                errorMessage = null
            )
        }
    }

    fun saveDiaryEntry(
        dbHelper: DBHelper,
        onSavedSuccessfully: () -> Unit
    ) {
        val current = _uiState.value

        if (current.isSaving || current.diaryAlreadyExists) return

        if (current.selectedMood == null) {
            _uiState.update {
                it.copy(
                    errorMessage =
                        "Por favor, selecione como você se sente hoje."
                )
            }
            return
        }

        if (current.notes.trim().isEmpty()) {
            _uiState.update {
                it.copy(
                    errorMessage = "Preencha a descrição do seu dia."
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isSaving = true,
                errorMessage = null,
                saveSuccess = false
            )
        }

        val resultado = dbHelper.salvarEntradaDiario(
            humor = current.selectedMood,
            descricao = current.notes
        )

        if (resultado == DiarySaveResult.SUCCESS) {
            _uiState.update {
                it.copy(
                    isSaving = false,
                    diaryAlreadyExists = true,
                    selectedMood = null,
                    notes = "",
                    saveSuccess = true,
                    errorMessage = null
                )
            }

            onSavedSuccessfully()
        } else if (resultado == DiarySaveResult.ALREADY_EXISTS) {
            _uiState.update {
                it.copy(
                    isSaving = false,
                    diaryAlreadyExists = true,
                    saveSuccess = false,
                    errorMessage = "Já existe um diário cadastrado. O limite é de um diário."
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isSaving = false,
                    saveSuccess = false,
                    errorMessage =
                        "Não foi possível salvar o diário. Tente novamente."
                )
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    fun resetSaveStatus() {
        _uiState.update {
            it.copy(saveSuccess = false)
        }
    }
}
