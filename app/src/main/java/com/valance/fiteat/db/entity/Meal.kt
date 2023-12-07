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
    @ColumnInfo (name = "calories")
    var calories: Int,
    @ColumnInfo (name = "squirrels")
    var squirrels: String,
    @ColumnInfo (name = "fats")
    var fats: String,
    @ColumnInfo (name = "carbohydrates")
    var carbohydrates: String,
    @ColumnInfo (name = "fibre")
    var fibre: String,
    @ColumnInfo (name = "sugar")
    var sugar: String,
)

