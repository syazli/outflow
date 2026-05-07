package my.com.syazli.outflow.data.repository

import kotlinx.coroutines.flow.Flow
import my.com.syazli.outflow.data.local.dao.CategoryDao
import my.com.syazli.outflow.data.local.entity.CategoryEntity
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val dao: CategoryDao) {

    fun getCategoriesByType(type: String): Flow<List<CategoryEntity>> = dao.getCategoriesByType(type)

    suspend fun insertCategory(category: CategoryEntity) = dao.insertCategory(category)

    suspend fun deleteCategory(category: CategoryEntity) = dao.deleteCategory(category)
}