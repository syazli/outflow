package my.com.syazli.outflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import my.com.syazli.outflow.data.utility.Constants.TRANSACTION_TABLE

@Entity(tableName = TRANSACTION_TABLE)
data class TransactionEntity(
    @PrimaryKey(true)
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: String,
    val date: Long,
    val note: String
)