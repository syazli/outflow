package my.com.syazli.outflow.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import my.com.syazli.outflow.data.local.dao.TransactionDao
import my.com.syazli.outflow.data.mapper.toDomain
import my.com.syazli.outflow.data.mapper.toEntity
import my.com.syazli.outflow.domain.model.Transactions
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val dao: TransactionDao) {

    fun getAllTransactions(): Flow<List<Transactions>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getTotalIncome(): Flow<Double?> = dao.getTotalIncome()

    fun getTotalExpense(): Flow<Double?> = dao.getTotalExpense()

    suspend fun insertTransaction(transaction: Transactions) {
        dao.insertTransaction(transaction.toEntity())
    }

    suspend fun getTransactionById(id: Int): Transactions? {
        return dao.getTransactionById(id)?.toDomain()
    }

    suspend fun updateTransaction(transaction: Transactions) {
        dao.updateTransaction(transaction.toEntity())
    }

    suspend fun deleteTransaction(transaction: Transactions) {
        dao.deleteTransaction(transaction.toEntity())
    }
}