package my.com.syazli.outflow.presentation.screen.add

import androidx.compose.runtime.saveable.Saver
import my.com.syazli.outflow.data.local.entity.CategoryEntity

data class AddUIState(
    val type: String = "expense",
    val categories: List<CategoryEntity> = emptyList(),
    val selectedCategory: CategoryEntity? = null,
    val amount: String = "",
    val date: Long = System.currentTimeMillis(),
    val note: String = "",
    val isSaved: Boolean = false,
    val error: String? = null,
    val showAddCategoryDialog: Boolean = false,
    val newCategoryName: String = "",
    val isEditMode: Boolean = false,
    val editTransactionId: Int? = null
)