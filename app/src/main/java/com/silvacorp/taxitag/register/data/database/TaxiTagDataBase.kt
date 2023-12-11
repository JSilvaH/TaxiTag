package com.silvacorp.taxitag.register.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.silvacorp.taxitag.register.data.database.dao.TaxiDao
import com.silvacorp.taxitag.register.data.database.entities.Taxi

@Database(entities = [Taxi::class], version = 1)
abstract class TaxiTagDataBase : RoomDatabase(){
    abstract fun getTaxiDao():TaxiDao
}