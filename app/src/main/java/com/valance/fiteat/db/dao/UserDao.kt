package com.valance.fiteat.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.valance.fiteat.db.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    @Query("SELECT * FROM users")
    fun getAllUser(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE name = :name LIMIT 1")
    fun getUserByName(name: String): User?

}