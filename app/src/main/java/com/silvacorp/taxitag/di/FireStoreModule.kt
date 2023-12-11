package com.silvacorp.taxitag.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FireStoreModule {

    @Provides
    @Singleton
    fun provideFireStore():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
}