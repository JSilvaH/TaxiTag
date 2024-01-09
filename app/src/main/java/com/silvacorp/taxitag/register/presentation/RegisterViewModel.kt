package com.silvacorp.taxitag.register.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvacorp.taxitag.common.Result
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import com.silvacorp.taxitag.register.domain.repository.repository.TaxiRepository
import com.silvacorp.taxitag.taxis.domain.repository.TaxisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.silvacorp.taxitag.register.presentation.TaxiState

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: TaxiRepository,
    private val repositoryAllTaxis: TaxisRepository
): ViewModel() {
    init {
        getAllTaxis()
        getMayorTaxi()
        getMinorTaxi()
        syncData()
//
    }
    private val _success = mutableStateOf(false)
    val success: State<Boolean> =_success

    private val _numberEconomic = mutableStateOf("")
    val numberEconomic: State<String> =_numberEconomic

    private val _mayor = MutableStateFlow(TaxiState())
    val mayor: StateFlow<TaxiState> =_mayor
    
    private val _msg = mutableStateOf("")
    val msg: State<String> =_msg

    private val _minor = MutableStateFlow(TaxiState())
    val minor: StateFlow<TaxiState> =_minor

    private val _loading = mutableStateOf<Boolean>(false)
    val loading: State<Boolean> =_loading

    private val _valid = mutableStateOf<Boolean>(false)
    val valid: State<Boolean> =_valid

    private val _checked = mutableStateOf<Boolean>(false)
    val checked: State<Boolean> =_checked

    fun changeMessage(error: String){
        _msg.value += error
    }

    fun onChangeNumber(number: String){
        _numberEconomic.value = number
    }

    fun onChecked(checked: Boolean){
        _checked.value = checked
    }

    fun insertTaxi(taxi: Taxi){
        viewModelScope.launch {
            repository.registerTaxiTag(taxi).collect(){ result ->
                when(result){
                    is Result.Success -> {
                        _success.value = true
                        getMayorTaxi()
                        getMinorTaxi()
                        _numberEconomic.value = ""
                        _checked.value = false
                    }
                    is Result.Error -> {
                        _msg.value = result.message ?: "An error has occurred"
                    }
                    is Result.Loading -> TODO()
                }

            }

        }
    }

    private fun getAllTaxis(){
        viewModelScope.launch {
            repositoryAllTaxis.getAllTaxis()
        }
    }

    private fun getMayorTaxi(){
        viewModelScope.launch {
            
            repository.returnMayorTaxi().collect(){ result ->
                when(result){
                    is Result.Success -> {
                        _mayor.value = TaxiState(
                            false,
                            result.data?: Taxi(0, 0, 0L, "21.884397, -102.293982", false, 1 ),
                            ""
                        )
                    }
                    is Result.Error -> _msg.value = result.message ?: "Error"
                    is Result.Loading -> _loading.value = true
                }
            }


        }
    }

    private fun syncData(){
        viewModelScope.launch {
            repository.syncData()
        }
    }

    private fun getMinorTaxi(){
        viewModelScope.launch {
            repository.returnMinorTaxi().collect(){ result ->
                when(result){
                    is Result.Success -> {
                        _minor.value = TaxiState(
                            false,
                            result.data?: Taxi(0, 0, 0L, "21.884397, -102.293982", false, 1 ),
                            ""
                        )
                    }
                    is Result.Error -> _msg.value = result.message ?: "Error"
                    is Result.Loading -> _loading.value = true
                }
            }
        }
    }

    fun onClear() {
        _msg.value = ""
    }
    fun onDismiss(){
        _success.value = false
    }


}