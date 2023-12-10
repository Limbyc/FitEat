package com.valance.fiteat.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.valance.fiteat.db.UserMetrics
import com.valance.fiteat.db.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT weight, height FROM user WHERE id = :userId")
    suspend fun getUserMetrics(userId: Int): UserMetrics?
}