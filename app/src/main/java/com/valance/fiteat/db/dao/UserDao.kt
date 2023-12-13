package com.valance.fiteat.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.valance.fiteat.db.UserMetrics
import com.valance.fiteat.db.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    
    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Int) : User

    @Query("SELECT weight, height FROM user WHERE id = :userId")
    suspend fun getUserMetrics(userId: Int): UserMetrics?

    @Query("UPDATE user SET weight = :newWeight WHERE id = :userId")
    suspend fun updateUserWeight(userId: Int, newWeight: String)
    
}