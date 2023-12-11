package com.silvacorp.taxitag.taxis.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import com.silvacorp.taxitag.ui.theme.TaxiTagTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaxisActivity : ComponentActivity() {
    private val viewModel: TaxiInfoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaxiTagTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val taxiList = viewModel.listTaxis.collectAsState()
                    TaxisList(taxiList.value)
                }
            }
        }
    }

    @Composable
    fun TaxisList(taxis: List<Taxi>) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(items = taxis, itemContent = { taxi ->
                TaxiCard(taxi)
            })

        }
    }
}

