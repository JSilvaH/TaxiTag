package com.silvacorp.taxitag.register.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.silvacorp.taxitag.register.data.database.entities.Taxi

@Dao
interface TaxiDao {
    @Query("SELECT * FROM  taxi_info ORDER BY uid")
    suspend fun getAllTaxis(): List<Taxi>

//    @Query("SELECT economicalNumber,location FROM taxi_info")
//    suspend fun getAllLocation(): List<String>

    @Insert
    suspend fun insertTaxi(taxi: Taxi): Long

    @Query("SELECT * FROM taxi_info ORDER BY economicalNumber DESC LIMIT 1")
    suspend fun getMayorTaxi(): Taxi

    @Query("SELECT * FROM taxi_info ORDER BY economicalNumber ASC LIMIT 1")
    suspend fun getMinorTaxi(): Taxi

    @Query("SELECT * FROM taxi_info WHERE economicalNumber = :numberTaxi ORDER BY dateSeen DESC LIMIT 1")
    suspend fun getLocationTaxi(numberTaxi: Int): Taxi


}