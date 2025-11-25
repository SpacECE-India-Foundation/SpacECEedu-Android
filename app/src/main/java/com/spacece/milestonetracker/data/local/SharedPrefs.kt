package com.spacece.milestonetracker.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPrefs constructor(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    fun clearAllPrefs() {
        prefs.edit { clear() }
    }

    fun setUserType(userType: String) = prefs.edit { putString(USER_TYPE, userType) }
    fun getUserType(): String = prefs.getString(USER_TYPE, "") ?: ""

    fun setUserLoggedIn(isLoggedIn: Boolean) = prefs.edit { putBoolean(IS_USER_LOG_IN, isLoggedIn) }
    fun isUserLoggedIn(): Boolean = prefs.getBoolean(IS_USER_LOG_IN, false)

    fun setGuestLoggedIn(isLoggedIn: Boolean) = prefs.edit { putBoolean(IS_GUEST_LOG_IN, isLoggedIn) }
    fun isGuestLoggedIn(): Boolean = prefs.getBoolean(IS_GUEST_LOG_IN, false)


    fun saveSelectedChildId(childId: String) {
        prefs.edit().putString("SELECTED_CHILD_ID", childId).apply()
    }

    fun getSelectedChildId(): String {
        return prefs.getString("SELECTED_CHILD_ID", "") ?: ""
    }

    fun saveUserId(userId: String) {
        prefs.edit().putString("USER_ID", userId).apply()
    }

    fun getUserId(): String {
        return prefs.getString("USER_ID", "") ?: ""
    }
}

