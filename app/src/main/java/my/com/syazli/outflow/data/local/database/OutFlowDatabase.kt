package my.com.syazli.outflow.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import my.com.syazli.outflow.data.local.dao.CategoryDao
import my.com.syazli.outflow.data.local.entity.TransactionEntity
import my.com.syazli.outflow.data.local.dao.TransactionDao
import my.com.syazli.outflow.data.local.entity.CategoryEntity
import my.com.syazli.outflow.data.utility.Constants.OUTFLOW_DATABASE
@Database(entities = [TransactionEntity::class, CategoryEntity::class], version = 2, exportSchema = false)
abstract class OutFlowDatabase: RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: OutFlowDatabase? = null
        fun getInstance(context: Context): OutFlowDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    OutFlowDatabase::class.java,
                    OUTFLOW_DATABASE
                )
                .build()
                .also { INSTANCE = it }
            }
        }
    }
}