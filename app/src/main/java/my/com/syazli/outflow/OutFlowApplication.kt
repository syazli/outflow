package my.com.syazli.outflow

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import my.com.syazli.outflow.data.local.database.OutFlowDatabase
import my.com.syazli.outflow.data.local.entity.CategoryEntity

@HiltAndroidApp
class OutFlowApplication: Application() {

    private val PREFERENCE_NAME = "outFlowPreference"

    companion object {
        @JvmStatic
        @Volatile
        lateinit var instance: OutFlowApplication
            private set

        @JvmStatic
        @Synchronized
        fun getAppInstance(): OutFlowApplication {
            return instance
        }

        const val PREFERENCE_APP_AUTH = "APP_AUTH"
        const val PREFERENCE_CATEGORY_LOADED = "CATEGORY_LOADED"

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        preloadCategories()
    }

    private fun preloadCategories() {
        val isLoaded = getPreference(PREFERENCE_CATEGORY_LOADED, false)
        if (isLoaded) return

        CoroutineScope(Dispatchers.IO).launch {
            val dao = OutFlowDatabase.getInstance(this@OutFlowApplication).categoryDao()
            val expenseCategories = listOf("Food", "Transport", "Shopping", "Bills", "Entertainment")
            val incomeCategories = listOf("Salary", "Bonus", "Gift")

            expenseCategories.forEach { name ->
                dao.insertCategory(CategoryEntity(name = name, type = "expense"))
            }

            incomeCategories.forEach { name ->
                dao.insertCategory(CategoryEntity(name = name, type = "income"))
            }

            setPreference(PREFERENCE_CATEGORY_LOADED, true)
        }

    }
    fun setPreference(key: String, value: Boolean) {
        getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(key, value)
            .apply()
    }

    fun getPreference(key: String, defaultValue: Boolean): Boolean {
        return getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getBoolean(key, defaultValue)
    }

    fun setPreference(key: String, value: Int) {
        getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            .putInt(key, value)
            .apply()
    }

    fun getPreference(key: String, defaultValue: Int): Int {
        return getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getInt(key, defaultValue)
    }

    fun setLongPreference(key: String, value: Long) {
        getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            .putLong(key, value)
            .apply()
    }

    fun getLongPreference(key: String, defaultValue: Long): Long {
        return getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getLong(key, defaultValue)
    }

    fun setPreference(key: String, value: String) {
        getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            .putString(key, value)
            .apply()
    }

    fun getPreference(key: String, defaultValue: String): String? {
        return getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getString(key, defaultValue)
    }

    fun setPreference(key: String, value: Float) {
        getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            .putFloat(key, value)
            .apply()
    }

    fun getPreference(key: String, defaultValue: Float): Float {
        return getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getFloat(key, defaultValue)
    }

    fun clearPreference(key: String) {
        getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            .remove(key)
            .apply()
    }
}