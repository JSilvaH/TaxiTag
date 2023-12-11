package com.silvacorp.taxitag.location.domain.repository

import com.silvacorp.taxitag.common.Result
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import kotlinx.coroutines.flow.Flow

interface TaxiLocationRepository {
    suspend fun getAllLocations(): Flow<Result<List<Taxi>>>
}