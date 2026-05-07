package my.com.syazli.outflow.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import my.com.syazli.outflow.data.local.dao.CategoryDao
import my.com.syazli.outflow.data.local.dao.TransactionDao
import my.com.syazli.outflow.data.local.database.OutFlowDatabase
import my.com.syazli.outflow.data.repository.CategoryRepository
import my.com.syazli.outflow.data.repository.TransactionRepository
import javax.inject.Singleton
import my.com.syazli.outflow.data.utility.Constants.OUTFLOW_DATABASE

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): OutFlowDatabase {
        return OutFlowDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideTransactionDao(db: OutFlowDatabase) : TransactionDao {
        return db.transactionDao()
    }

    @Provides
    @Singleton
    fun provideTransactionRepo(dao : TransactionDao): TransactionRepository {
        return TransactionRepository(dao)
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: OutFlowDatabase) : CategoryDao {
        return db.categoryDao()
    }

    @Provides
    @Singleton
    fun provideCategoryRepo(dao: CategoryDao) : CategoryRepository {
        return CategoryRepository(dao)
    }
}