package my.com.syazli.outflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import my.com.syazli.outflow.data.local.entity.TransactionEntity
import my.com.syazli.outflow.data.utility.Constants.TRANSACTION_TABLE


@Dao
interface TransactionDao {

    @Query("SELECT * FROM $TRANSACTION_TABLE ORDER BY date DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT SUM(amount) FROM $TRANSACTION_TABLE WHERE type ='income'")
    fun getTotalIncome(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM $TRANSACTION_TABLE WHERE type ='expense'")
    fun getTotalExpense(): Flow<Double?>

    @Query("SELECT * FROM $TRANSACTION_TABLE WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

}