package com.spacece.milestonetracker.data.local

import android.content.Context
import com.spacece.milestonetracker.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun AppDatabase.initialiseUser(sharedPrefs: SharedPrefs, user: User?) {
    withContext(Dispatchers.IO) {
        if (user != null) {
            userDao().upsertUser(user)
            sharedPrefs.setUserLoggedIn(true)
            sharedPrefs.setGuestLoggedIn(false)
        }
    }
}

suspend fun AppDatabase.getCurrentUser(): User {
    return withContext(Dispatchers.IO) {
        userDao().getUser() ?: User()
    }
}

suspend fun AppDatabase.clearAllData(sharedPrefs: SharedPrefs) {
    withContext(Dispatchers.IO) {
        clearAllTables()
        sharedPrefs.clearAllPrefs()
    }
}

/**
 * Fetches the current user's ID safely from the Room database.
 */
suspend fun getCurrentUserId(context: Context): Int? = withContext(Dispatchers.IO) {
    val db = AppModule.aapDataBase(context)
    return@withContext db.userDao().getUser()?.current_user_id
}
