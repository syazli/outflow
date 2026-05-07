package my.com.syazli.outflow.data.repository

import kotlinx.coroutines.flow.Flow
import my.com.syazli.outflow.data.local.dao.TransactionDao
import my.com.syazli.outflow.data.local.entity.TransactionEntity
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val dao: TransactionDao) {

    fun getAllTransactions(): Flow<List<TransactionEntity>> = dao.getAll()

    fun getTotalIncome(): Flow<Double?> = dao.getTotalIncome()

    fun getTotalExpense(): Flow<Double?> = dao.getTotalExpense()

    suspend fun insertTransaction(transaction: TransactionEntity) = dao.insertTransaction(transaction)

    suspend fun getTransactionById(id: Int): TransactionEntity? = dao.getTransactionById(id)

    suspend fun updateTransaction(transaction: TransactionEntity) = dao.updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: TransactionEntity) = dao.deleteTransaction(transaction)
}