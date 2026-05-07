package my.com.syazli.outflow.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import my.com.syazli.outflow.data.local.dao.CategoryDao
import my.com.syazli.outflow.data.mapper.toDomain
import my.com.syazli.outflow.data.mapper.toEntity
import my.com.syazli.outflow.domain.model.Category
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val dao: CategoryDao) {

    fun getCategoriesByType(type: String): Flow<List<Category>> {
        return dao.getCategoriesByType(type).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertCategory(category: Category) {
        dao.insertCategory(category.toEntity())
    }

    suspend fun deleteCategory(category: Category)  {
        dao.deleteCategory(category.toEntity())
    }
}