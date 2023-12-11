package com.silvacorp.taxitag.register.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taxi_info")
data class Taxi(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "economicalNumber") val economicalNumber: Int,
    @ColumnInfo(name = "dateSeen") val dateSeen: Long,
    @ColumnInfo(name = "location") val location: String = "0, 0",
    @ColumnInfo(name = "are_passenger") val arePassenger: Boolean,
    @ColumnInfo(name = "user") val user: Int
)
