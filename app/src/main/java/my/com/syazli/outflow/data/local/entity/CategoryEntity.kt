package my.com.syazli.outflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import my.com.syazli.outflow.data.utility.Constants.CATEGORY_TABLE

@Entity(tableName = CATEGORY_TABLE)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String
)