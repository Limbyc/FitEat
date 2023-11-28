package com.valance.fiteat.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {
    @Insert
    fun insertUser(user: User)
    @Query("SELECT * FROM UserInfo")
    fun getAllUser(): Flow<List<User>>
}