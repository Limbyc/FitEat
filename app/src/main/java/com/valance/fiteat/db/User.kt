package com.valance.fiteat.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserInfo")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "UserName")
    var name: String,
    @ColumnInfo(name = "UserHeight")
    var height: Int,
    @ColumnInfo(name = "UserWeight")
    var weight: Double,
)