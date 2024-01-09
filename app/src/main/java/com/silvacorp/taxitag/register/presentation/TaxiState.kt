package com.silvacorp.taxitag.register.presentation

import com.silvacorp.taxitag.register.data.database.entities.Taxi

data class TaxiState(
    val isLoading: Boolean = false,
    val taxiInfo: Taxi  =Taxi(0,0,0L,"0, 0", false, 0),
    val error: String = ""
)
