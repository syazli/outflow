package my.com.syazli.outflow.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import my.com.syazli.outflow.data.local.entity.TransactionEntity
import my.com.syazli.outflow.data.repository.TransactionRepository
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val transactionRepository: TransactionRepository) : ViewModel() {
    private val _state = MutableStateFlow(ListUiState())

    val uiState: StateFlow<ListUiState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine (
                transactionRepository.getAllTransactions(),
                transactionRepository.getTotalIncome(),
                transactionRepository.getTotalExpense()
            ) { transaction, income, expense ->
                val totalIncome = income ?: 0.0
                val totalExpense = expense?: 0.0
                ListUiState(transaction, totalIncome, totalExpense, totalIncome - totalExpense, false, !_state.value.hasLaunched, true)
            }.collect { state ->
                _state.update { state }
            }
        }
    }
    fun onLoadingDismiss() {
        _state.update { it.copy(isDoneLoading = false) }
    }
     fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
        }

    }
}