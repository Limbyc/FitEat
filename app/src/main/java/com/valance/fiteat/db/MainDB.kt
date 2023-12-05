package com.valance.fiteat.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.valance.fiteat.db.dao.UserDao
import com.valance.fiteat.db.dao.MealDao
import com.valance.fiteat.db.entity.Meal
import com.valance.fiteat.db.entity.User
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@Database(entities = [User::class, Meal::class],  version = 2)
abstract class MainDB: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao

    companion object {
        private var instance: MainDB? = null

        fun getDB(context: Context): MainDB {
            if (instance == null) {
                instance = createDatabase(context)
            }
            return instance!!
        }

        private fun createDatabase(context: Context): MainDB {
            val databaseName = "FitEat.db"
            val dbPath = context.getDatabasePath(databaseName).absolutePath


            if (!checkDatabaseExists(context, dbPath)) {
                copyDatabaseFromAssets(context, databaseName, dbPath)
            }

            return Room.databaseBuilder(
                context.applicationContext,
                MainDB::class.java,
                databaseName
            ).fallbackToDestructiveMigration()
                .build()
        }

        private fun checkDatabaseExists(context: Context, dbPath: String): Boolean {
            val file = File(dbPath)
            return file.exists()
        }

        private fun copyDatabaseFromAssets(context: Context, databaseName: String, dbPath: String) {
            try {
                val inputStream: InputStream = context.assets.open(databaseName)
                val outputStream: OutputStream = FileOutputStream(dbPath)
                inputStream.copyTo(outputStream)

                inputStream.close()
                outputStream.flush()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
