package com.silvacorp.taxitag.taxis.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvacorp.taxitag.common.Result
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import com.silvacorp.taxitag.taxis.data.repository.InfoTaxisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaxiInfoViewModel @Inject constructor(
    private val repository: InfoTaxisRepository
): ViewModel() {

    private val _listTaxis = MutableStateFlow(emptyList<Taxi>())
    val listTaxis: StateFlow<List<Taxi>> =_listTaxis

    init {
        getAllTaxis()
    }


    private fun getAllTaxis(){
        viewModelScope.launch {
            repository.getAllTaxis().collect(){result ->
                when(result){
                    is Result.Success -> {
                        _listTaxis.value = result.data?:emptyList()
                    }
                    is Result.Error -> TODO()
                    is Result.Loading -> TODO()
                }

            }
        }
    }
}