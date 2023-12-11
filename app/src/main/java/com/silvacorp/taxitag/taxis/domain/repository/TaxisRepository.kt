package com.silvacorp.taxitag.taxis.domain.repository

import com.silvacorp.taxitag.common.Result
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import kotlinx.coroutines.flow.Flow

interface TaxisRepository {
    suspend fun getAllTaxis(): Flow<Result<List<Taxi>>>
}