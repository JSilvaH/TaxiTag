package com.silvacorp.taxitag.register.presentation

import com.silvacorp.taxitag.register.data.database.entities.Taxi

data class TaxiState(
    val isLoading: Boolean = false,
    val taxiInfo: Taxi?= null,
    val error: String = ""
)
