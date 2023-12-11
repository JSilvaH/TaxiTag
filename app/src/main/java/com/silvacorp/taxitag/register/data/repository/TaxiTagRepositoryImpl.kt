package com.silvacorp.taxitag.register.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.silvacorp.taxitag.common.Result
import com.silvacorp.taxitag.register.data.database.dao.TaxiDao
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import com.silvacorp.taxitag.register.domain.repository.repository.TaxiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaxiTagRepositoryImpl @Inject constructor(
    private val taxiDao: TaxiDao,
    private val firebaseFirestore: FirebaseFirestore
) : TaxiRepository {
    override suspend fun registerTaxiTag(taxi: Taxi): Flow<Result<Boolean>> = flow {
        val lastInsert = taxiDao.insertTaxi(taxi)

        if (lastInsert > 0L) {
            taxi.uid = lastInsert.toInt()
            firebaseFirestore
                .collection("taxis")
                .add(taxi)
                .addOnSuccessListener { it ->
                    Log.d("PEPE", "Exito")
                }
            emit(Result.Success(true))
        } else {
            emit(Result.Error("Error"))
        }

    }

    override suspend fun returnMayorTaxi(): Flow<Result<Taxi>> = flow {
        val response = taxiDao.getMayorTaxi()
        emit(Result.Success(response))
    }

    override suspend fun returnMinorTaxi() = flow {
        val response = taxiDao.getMinorTaxi()
        emit(Result.Success(response))
    }

    override suspend fun syncData() {
        val registersLocal = taxiDao.getAllTaxis().size
        val registersRemote = firebaseFirestore.collection("taxis").get().await()
        val data = taxiDao.getAllTaxis()
        val countRemote = registersRemote.size()

        when {
            registersLocal > countRemote -> {
//                coroutineScope {
//                val tasks = mutableListOf<Deferred<DocumentReference>>()
                for (i in countRemote until registersLocal) {
                    val taxi = data[i]
//                        val task = async(Dispatchers.IO) {
                    firebaseFirestore.collection("taxis").add(taxi)
//                        }
//                        tasks.add(task)
                }
//                    tasks.awaitAll()

            }
//            }

            registersLocal < countRemote -> {
                val taxisRemoteList = ArrayList<Taxi>()
                firebaseFirestore
                    .collection("taxis")
                    .orderBy("uid", Query.Direction.ASCENDING)
                    .get().addOnSuccessListener { taxis ->
                        for (taxi in taxis) {
                            taxisRemoteList.add(
                                Taxi(
                                    taxi.data["uid"].toString().toInt(),
                                    taxi.data["economicalNumber"].toString().toInt(),
                                    taxi.data["dateSeen"].toString().toLong(),
                                    taxi.data["location"].toString(),
                                    taxi.data["are_passenger"].toString().toBoolean(),
                                    taxi.data["user"].toString().toInt()
                                )
                            )

                        }

                    }.await()

                taxisRemoteList.sortedBy { it.uid }

                for (i in registersLocal until countRemote) {
                    taxiDao.insertTaxi(taxisRemoteList[i])
                }

            }
        }

    }


}