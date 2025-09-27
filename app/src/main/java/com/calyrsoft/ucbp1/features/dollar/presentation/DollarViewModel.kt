package com.calyrsoft.ucbp1.features.dollar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.dollar.data.datasource.DollarLocalDataSource
import com.calyrsoft.ucbp1.features.dollar.domain.model.DollarModel
import com.calyrsoft.ucbp1.features.dollar.domain.usecase.FetchDollarUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DollarViewModel(
    val fetchDollarUseCase: FetchDollarUseCase,
    private val localDataSource: DollarLocalDataSource
) : ViewModel() {

    sealed class DollarUIState {
        object Loading : DollarUIState()
        class Error(val message: String) : DollarUIState()
        class Success(val data: DollarModel, val history: List<DollarModel>) : DollarUIState()
    }

    private val _uiState = MutableStateFlow<DollarUIState>(DollarUIState.Loading)
    val uiState: StateFlow<DollarUIState> = _uiState.asStateFlow()

    init {
        getDollar()
        loadHistory()
    }

    fun getDollar() {
        viewModelScope.launch {
            fetchDollarUseCase()
                .catch { e ->
                    android.util.Log.e("DOLLAR_VM", "Error recibiendo RTDB", e)
                    _uiState.value = DollarUIState.Error("Error RTDB: ${e.message}")
                }
                .collect { dollar ->
                    // Guarda el nuevo registro en Room (histórico) con seguridad
                    val history = try {
                        withContext(Dispatchers.IO) {
                            localDataSource.insert(dollar)   // asegúrate de que este método trabaje en IO
                            localDataSource.getAllOrderedByDate()
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("DOLLAR_VM", "Error Room insert/getAll", e)
                        emptyList()
                    }

                    _uiState.value = DollarUIState.Success(dollar, history)
                }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            try {
                val history = withContext(Dispatchers.IO) {
                    localDataSource.getAllOrderedByDate()
                }
                val currentData = (_uiState.value as? DollarUIState.Success)?.data
                    ?: DollarModel("0", "0", "0", "0")
                _uiState.value = DollarUIState.Success(currentData, history)
            } catch (e: Exception) {
                android.util.Log.e("DOLLAR_VM", "Error Room getAll", e)
                // Mantener estado anterior si falla
                val currentData = (_uiState.value as? DollarUIState.Success)?.data
                    ?: DollarModel("0", "0", "0", "0")
                _uiState.value = DollarUIState.Success(currentData, emptyList())
            }
        }
    }
}
