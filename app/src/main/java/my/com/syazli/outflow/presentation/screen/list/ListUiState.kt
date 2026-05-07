package my.com.syazli.outflow.presentation.screen.list

import my.com.syazli.outflow.domain.model.Transactions

data class ListUiState(
    val transaction: List<Transactions> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val totalBalance: Double = 0.0,
    val isLoading: Boolean = true,
    val isDoneLoading: Boolean = false,
    val hasLaunched: Boolean = false
)
