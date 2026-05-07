package my.com.syazli.outflow.presentation.screen.add

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import my.com.syazli.outflow.OutFlowApplication
import my.com.syazli.outflow.R
import my.com.syazli.outflow.data.repository.CategoryRepository
import my.com.syazli.outflow.data.repository.TransactionRepository
import my.com.syazli.outflow.domain.model.Category
import my.com.syazli.outflow.domain.model.Transactions
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(private val repository: TransactionRepository, private val categoryRepository: CategoryRepository) : ViewModel() {

    private val _state = MutableStateFlow(AddUIState())
    val uiState: StateFlow<AddUIState> = _state.asStateFlow()

    fun setType(type: String) {
        _state.update { it.copy(type = type, selectedCategory = null) }
        loadCategories(type)
    }

    fun loadTransactionEdit(id: Int) {
        viewModelScope.launch {
            val transaction = repository.getTransactionById(id) ?: return@launch
            loadCategories(transaction.type)

            categoryRepository.getCategoriesByType(transaction.type).collect { categories ->
                val matchedCategory = categories.find { it.name == transaction.title }
                _state.update {
                    it.copy(
                        isEditMode = true,
                        editTransactionId = transaction.id,
                        type = transaction.type,
                        categories = categories,
                        selectedCategory = matchedCategory,
                        amount = transaction.amount.toString(),
                        date = transaction.date,
                        note = transaction.note

                    )
                }
                return@collect
            }
        }
    }
    private fun loadCategories(type: String) {
        viewModelScope.launch {
            categoryRepository.getCategoriesByType(type).collect { categories ->
                _state.update { it.copy(categories = categories) }
            }
        }
    }

    fun setCategory(category: Category) {
        _state.update { it.copy(selectedCategory = category, error = null) }
    }

    fun setAmount(amount: String) {
        _state.update { it.copy(amount = amount, error = null) }
    }

    fun setDate(date: Long) {
        _state.update { it.copy(date = date) }
    }

    fun setNote(note: String) {
        _state.update { it.copy(note = note) }
    }

    fun showAddCategoryDialog() {
        _state.update { it.copy(showAddCategoryDialog = true, newCategoryName = "") }
    }

    fun dismissCategoryDialog() {
        _state.update { it.copy(showAddCategoryDialog = false, newCategoryName = "") }
    }

    fun setNewCategoryName(name: String) {
        _state.update { it.copy(newCategoryName = name) }
    }

    fun saveNewCategory() {
        val state = _state.value
        if (state.newCategoryName.isBlank()) return

        viewModelScope.launch {
            val newCategory = Category(
                name = state.newCategoryName.trim(),
                type = state.type
            )
            categoryRepository.insertCategory(newCategory)
            _state.update {
                it.copy(
                    showAddCategoryDialog = false,
                    newCategoryName = ""
                )
            }
        }
    }
    fun save() {
        val state = _state.value

        if (state.selectedCategory == null) {
            _state.update { it.copy(error = OutFlowApplication.getAppInstance().getString(R.string.add_category)) }
            return
        }

        if (state.amount.isBlank()) {
            _state.update { it.copy(error = OutFlowApplication.getAppInstance().getString(R.string.add_amount)) }
            return
        }

        val amountDouble = state.amount.toDoubleOrNull()
        if (amountDouble == null || amountDouble <= 0) {
            _state.update { it.copy(error = OutFlowApplication.getAppInstance().getString(R.string.add_amount_valid)) }
            return
        }


        viewModelScope.launch {
            if (state.isEditMode && state.editTransactionId != null) {
                repository.updateTransaction(
                    Transactions(
                        id = state.editTransactionId,
                        title =  state.selectedCategory.name,
                        amount = amountDouble,
                        type = state.type,
                        date = state.date,
                        note = state.note

                    )
                )
            } else {
                repository.insertTransaction(
                    Transactions(
                        title =  state.selectedCategory.name,
                        amount = amountDouble,
                        type = state.type,
                        date = state.date,
                        note = state.note

                    )
                )
            }

            _state.update { it.copy(isSaved = true) }
        }
    }
}