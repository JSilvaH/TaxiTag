package com.silvacorp.taxitag.taxis.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.silvacorp.taxitag.common.Result
import com.silvacorp.taxitag.register.data.database.dao.TaxiDao
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import com.silvacorp.taxitag.taxis.domain.repository.TaxisRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class InfoTaxisRepository @Inject constructor(
    private val dao: TaxiDao,
    private val fireStore: FirebaseFirestore
): TaxisRepository {
    override suspend fun getAllTaxis(): Flow<Result<List<Taxi>>> = flow {
        val taxis = dao.getAllTaxis()

        if (taxis.isEmpty()){
            val taxisRemoteList = ArrayList<Taxi>()
            suspendCoroutine<List<Taxi>>{ continuation ->
                fireStore.collection("taxis")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result){
                            taxisRemoteList.add(
                                Taxi(0,
                                    document.data["economicalNumber"].toString().toInt(),
                                    document.data["dateSeen"].toString().toLong(),
                                    document.data["location"].toString(),
                                    document.data["are_passenger"].toString().toBoolean(),
                                    document.data["user"].toString().toInt())
                            )

                        }

                        continuation.resume(taxisRemoteList)
                    }


            }
            taxisRemoteList.forEach {
                dao.insertTaxi(it)
            }
            val result = dao.getAllTaxis()
            emit(Result.Success(result))

        }else {
            emit(Result.Success(taxis))
        }

    }

    override suspend fun getLocationTaxi(numberTaxi: Int): Flow<Result<Taxi>> = flow {
        val taxi = dao.getLocationTaxi(numberTaxi)
        emit(Result.Success(taxi))
    }


}