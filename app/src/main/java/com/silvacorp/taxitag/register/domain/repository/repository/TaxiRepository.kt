package com.silvacorp.taxitag.register.domain.repository.repository

import com.silvacorp.taxitag.common.Result
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import kotlinx.coroutines.flow.Flow

interface TaxiRepository {
    suspend fun registerTaxiTag(taxi: Taxi): Flow<Result<Boolean>>
    suspend fun returnMayorTaxi(): Flow<Result<Taxi>>
    suspend fun returnMinorTaxi(): Flow<Result<Taxi>>
    suspend fun syncData()
}