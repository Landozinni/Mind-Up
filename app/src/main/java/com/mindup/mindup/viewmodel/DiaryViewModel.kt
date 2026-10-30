package com.mindup.mindup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindup.mindup.DBHelper
import com.mindup.mindup.DiarySaveResult
import com.mindup.mindup.model.DiaryEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DiaryUiState(
    val selectedMood: String? = null,
    val notes: String = "",
    val isSaving: Boolean = false,
    val isLoading: Boolean = true,
    val diaryAlreadyExists: Boolean = false,
    val todayEntry: DiaryEntry? = null,
    val history: List<DiaryEntry> = emptyList(),
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false
)

class DiaryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    fun loadDiaryStatus(dbHelper: DBHelper) {
        viewModelScope.launch {
            val diaryData = withContext(Dispatchers.IO) {
                runCatching {
                    dbHelper.obterEntradaDiarioHoje() to dbHelper.obterHistoricoDiario()
                }.getOrNull()
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    diaryAlreadyExists = diaryData?.first != null,
                    todayEntry = diaryData?.first,
                    history = diaryData?.second.orEmpty(),
                    selectedMood = diaryData?.first?.mood,
                    notes = diaryData?.first?.description.orEmpty(),
                    errorMessage = if (diaryData == null) "Não foi possível carregar seu diário." else null
                )
            }
        }
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

        viewModelScope.launch {
            val resultado = withContext(Dispatchers.IO) {
                dbHelper.salvarEntradaDiario(
                    humor = current.selectedMood,
                    descricao = current.notes
                )
            }

            if (resultado == DiarySaveResult.SUCCESS) {
                val diaryData = withContext(Dispatchers.IO) {
                    runCatching {
                        dbHelper.obterEntradaDiarioHoje() to dbHelper.obterHistoricoDiario()
                    }.getOrNull()
                }
                val todayEntry = diaryData?.first ?: DiaryEntry(
                    id = -1,
                    mood = current.selectedMood.orEmpty(),
                    description = current.notes.trim(),
                    createdAt = ""
                )
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        diaryAlreadyExists = true,
                        todayEntry = todayEntry,
                        history = diaryData?.second.orEmpty(),
                        selectedMood = todayEntry?.mood ?: current.selectedMood,
                        notes = todayEntry?.description ?: current.notes.trim(),
                        saveSuccess = true,
                        errorMessage = null
                    )
                }
                onSavedSuccessfully()
            } else if (resultado == DiarySaveResult.ALREADY_EXISTS) {
                loadDiaryStatus(dbHelper)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        diaryAlreadyExists = true,
                        saveSuccess = false,
                        errorMessage = "Você já registrou seu diário de hoje."
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveSuccess = false,
                        errorMessage = "Não foi possível salvar o diário. Tente novamente."
                    )
                }
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
