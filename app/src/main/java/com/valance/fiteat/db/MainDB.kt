package com.valance.fiteat.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.valance.fiteat.db.dao.UserDao
import com.valance.fiteat.db.entity.User

@Database(entities = [User::class], version = 1)
abstract class MainDB: RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object{
        fun getDB(context: Context): MainDB {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDB::class.java,
                "DataBase.db"
            ).build()
        }
    }
}