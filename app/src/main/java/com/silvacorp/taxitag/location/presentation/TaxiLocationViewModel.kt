package com.silvacorp.taxitag.location.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.silvacorp.taxitag.common.Result
import com.silvacorp.taxitag.common.components.InfoLocation
import com.silvacorp.taxitag.location.domain.repository.TaxiLocationRepository
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import com.silvacorp.taxitag.register.domain.repository.repository.TaxiRepository
import com.silvacorp.taxitag.register.presentation.TaxiState
import com.silvacorp.taxitag.taxis.domain.repository.TaxisRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaxiLocationViewModel @Inject constructor(
    private val repository: TaxisRepository
) : ViewModel() {

    private val _locations = MutableStateFlow<List<Taxi>>(emptyList())
    val locations: StateFlow<List<Taxi>> = _locations

    private val _taxiMayor = MutableStateFlow(TaxiState())
    val taxiMayor: StateFlow<TaxiState> = _taxiMayor

    private val _taxiMinor = MutableStateFlow(TaxiState())
    val taxiMinor: StateFlow<TaxiState> = _taxiMinor

    private val _message = mutableStateOf("")
    val message: State<String> = _message

    init {
        showAllLocation()
    }

    private fun showAllLocation() {
        viewModelScope.launch {
            repository.getAllTaxis().collect() { result ->
                when (result) {
                    is Result.Success -> {
                        _locations.value = result.data ?: emptyList()
                    }

                    is Result.Error -> _message.value = ""
                    is Result.Loading -> TODO()
                }
            }


        }
    }

     fun getLocationTaxiMayor(numberTaxi: Int) {
        viewModelScope.launch {
            repository.getLocationTaxi(numberTaxi).collect() { result ->
                when (result) {
                    is Result.Success -> {
                        _taxiMayor.value = TaxiState(taxiInfo = result.data?: Taxi(0, 0, 0L, "0, 0", false, 1 ),)
                    }

                    is Result.Loading -> TODO()
                    is Result.Error -> _message.value = "Error"
                }
                repository.getLocationTaxi(numberTaxi).collect() { result ->
                    when (result) {
                        is Result.Success -> {
                            _taxiMinor.value = TaxiState(taxiInfo =result.data?: Taxi(0, 0, 0L, "0, 0", false, 1 ))
                        }

                        is Result.Loading -> TODO()
                        is Result.Error -> _message.value = "Error"
                    }
                }

            }
        }
    }
}


