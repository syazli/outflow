package my.com.syazli.outflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import my.com.syazli.outflow.data.local.entity.CategoryEntity
import my.com.syazli.outflow.data.utility.Constants.CATEGORY_TABLE

@Dao
interface CategoryDao {

    @Query("SELECT * FROM $CATEGORY_TABLE WHERE type = :type ORDER BY name ASC")
    fun getCategoriesByType(type: String): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT COUNT(*) FROM $CATEGORY_TABLE")
    suspend fun getCategoryCount(): Int
}