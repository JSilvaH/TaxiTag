package com.silvacorp.taxitag.location.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.silvacorp.taxitag.common.unixToDate
import com.silvacorp.taxitag.location.presentation.ui.theme.TaxiTagTheme
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaxiLocationActivity : ComponentActivity() {
    val viewModel: TaxiLocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val showAllTaxis = intent.extras!!.getBoolean("showAllTaxis", false)
        val number = intent.extras?.getString("number", "0")
        val location = intent.extras?.getString("location")
        val dateSeen = intent.extras?.getString("dateSeen", "0")

        setContent {
            TaxiTagTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showAllTaxis){
                        val list  = viewModel.locations.collectAsState()
                        MyMapsALlTaxis(locationsTaxis = list.value)
                    }else {
                        MyMap(
                            location!!,
                            number!!.toInt(),
                            dateSeen!!
                        )
                    }

                }
            }
        }
    }
//    AIzaSyCN0AOeOrhaWdXitmbj4BQCoyEfFILi0zI
}

@Composable
fun MyMapsALlTaxis(locationsTaxis: List<Taxi>) {
    val context = LocalContext.current
    val ags = LatLng(21.884397, -102.293982)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ags, 13f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        locationsTaxis.forEach { taxi ->
            val parts = taxi.location.split(", ")
            val latitude = parts[0]
            val longitude = parts[1]

            val location = LatLng(latitude.toDouble(), longitude.toDouble())
            Marker(
                position = location,
                title = taxi.economicalNumber.toString(),
                snippet = taxi.dateSeen.toInt().unixToDate(),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
        }

    }
}

@Composable
fun MyMap(location: String, number: Int, dateSeen: String) {
    val parts = location.split(", ")
    val latitude = parts[0]
    val longitude = parts[1]

    val ags = LatLng(latitude.toDouble(), longitude.toDouble())

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ags, 12f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            position = ags,
            title = number.toString(),
            snippet = dateSeen,
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        )
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    TaxiTagTheme {
        Greeting2("Android")
    }
}