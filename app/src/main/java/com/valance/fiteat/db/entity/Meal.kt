package com.valance.fiteat.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal")
data class Meal(
    @PrimaryKey
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "calories")
    var calories: Int?,
    @ColumnInfo(name = "squirrels")
    var squirrels: Int?,
    @ColumnInfo(name = "fats")
    var fats: Int?,
    @ColumnInfo(name = "carbohydrates")
    var carbohydrates: Int?,
    @ColumnInfo(name = "fibre")
    var fibre: Int?,
    @ColumnInfo(name = "sugar")
    var sugar: Int?
)






