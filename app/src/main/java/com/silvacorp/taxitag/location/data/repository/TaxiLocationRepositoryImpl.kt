package com.silvacorp.taxitag.location.data.repository

import com.silvacorp.taxitag.common.Result
import com.silvacorp.taxitag.location.domain.repository.TaxiLocationRepository
import com.silvacorp.taxitag.register.data.database.dao.TaxiDao
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaxiLocationRepositoryImpl @Inject constructor(
    private val dao: TaxiDao
): TaxiLocationRepository {
    override suspend fun getAllLocations(): Flow<Result<List<Taxi>>> = flow{
        try {
            val result = dao.getAllTaxis()
            emit(Result.Success(result))
        }catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }
}