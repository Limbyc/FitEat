package com.valance.fiteat.db.sharedPreferences

import android.content.Context
import com.valance.fiteat.db.sharedPreferences.User

class UserSharedPreferences(private val context: Context) {

    private val USER_PREFERENCES = "user_preferences"
    private val KEY_USER_NAME = "user_name"
    private val KEY_USER_HEIGHT = "user_height"
    private val KEY_USER_WEIGHT = "user_weight"
    private val KEY_USER_EATING_TIME = "user_eating_time"

    private val sharedPreferences = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_NAME, user.name)
        editor.putInt(KEY_USER_HEIGHT, user.height)
        editor.putInt(KEY_USER_WEIGHT, user.weight)
        editor.putString(KEY_USER_EATING_TIME, user.time)
        editor.apply()
    }

    fun getUser(): User {
        val userName = sharedPreferences.getString(KEY_USER_NAME, "") ?: ""
        val userHeight = sharedPreferences.getInt(KEY_USER_HEIGHT, -1)
        val userWeight = sharedPreferences.getInt(KEY_USER_WEIGHT, -1)
        val userEatingTime = sharedPreferences.getString(KEY_USER_EATING_TIME, "") ?: ""

        return User(name = userName, height = userHeight, weight = userWeight, time = userEatingTime)
    }

    fun saveWeight(weight: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_USER_WEIGHT, weight)
        editor.apply()
    }
    fun getWeight(): Int {
        return sharedPreferences.getInt(KEY_USER_WEIGHT, -1)
    }

    fun getHeight(): Int {
        return sharedPreferences.getInt(KEY_USER_HEIGHT, -1)
    }

    fun clearUser() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_USER_NAME)
        editor.remove(KEY_USER_HEIGHT)
        editor.remove(KEY_USER_WEIGHT)
        editor.remove(KEY_USER_EATING_TIME)
        editor.apply()
    }
}
