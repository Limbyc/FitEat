package com.valance.fiteat.db.sharedPreferences

import android.content.Context

object PermissionManager {

    private const val PREF_NAME = "Permissions"
    private const val KEY_PERMISSION_GRANTED = "permissionGranted"

    fun savePermissionGranted(context: Context, isGranted: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_PERMISSION_GRANTED, isGranted).apply()
    }

    fun isPermissionGranted(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_PERMISSION_GRANTED, false)
    }
}
