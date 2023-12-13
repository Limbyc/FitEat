package com.valance.fiteat.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = -1,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "height")
    var height: Int = -1,
    @ColumnInfo(name = "weight")
    var weight: Int = -1,
    @ColumnInfo(name = "eatingTime")
    var time: String = ""
)