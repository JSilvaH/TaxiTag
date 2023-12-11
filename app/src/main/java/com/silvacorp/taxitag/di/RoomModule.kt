package com.silvacorp.taxitag.di

import android.content.Context
import androidx.room.Room
import com.silvacorp.taxitag.register.data.database.TaxiTagDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, TaxiTagDataBase::class.java, "taxi_database").build()


    @Singleton
    @Provides
    fun provideTaxiDao (dataBase: TaxiTagDataBase) = dataBase.getTaxiDao()
}