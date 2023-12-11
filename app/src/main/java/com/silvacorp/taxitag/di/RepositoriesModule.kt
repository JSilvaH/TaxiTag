package com.silvacorp.taxitag.di

import com.google.firebase.firestore.FirebaseFirestore
import com.silvacorp.taxitag.location.data.repository.TaxiLocationRepositoryImpl
import com.silvacorp.taxitag.location.domain.repository.TaxiLocationRepository
import com.silvacorp.taxitag.register.data.database.dao.TaxiDao
import com.silvacorp.taxitag.register.data.repository.TaxiTagRepositoryImpl
import com.silvacorp.taxitag.register.domain.repository.repository.TaxiRepository
import com.silvacorp.taxitag.taxis.data.repository.InfoTaxisRepository
import com.silvacorp.taxitag.taxis.domain.repository.TaxisRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideTaxiTagRepository(
        taxiDao: TaxiDao,
        firebaseFirestore: FirebaseFirestore
    ): TaxiRepository{
        return  TaxiTagRepositoryImpl(taxiDao, firebaseFirestore)
    }

    @Singleton
    @Provides
    fun provideTaxiInfoRepository(
        taxiDao: TaxiDao,
        fireStore: FirebaseFirestore
    ): TaxisRepository{
        return InfoTaxisRepository(taxiDao, fireStore)
    }

    @Singleton
    @Provides
    fun provideTaxiLocationRepository(
        taxiDao: TaxiDao
    ): TaxiLocationRepository{
        return TaxiLocationRepositoryImpl(taxiDao)
    }
}