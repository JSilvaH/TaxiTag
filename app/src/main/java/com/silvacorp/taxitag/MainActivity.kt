package com.silvacorp.taxitag

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.silvacorp.taxitag.common.getUnixTime
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import com.silvacorp.taxitag.register.presentation.RegisterViewModel
import com.silvacorp.taxitag.taxis.presentation.TaxisActivity
import com.silvacorp.taxitag.ui.theme.TaxiTagTheme
import com.silvacorp.taxitag.ui.theme.autoroneRegular
import dagger.hilt.android.AndroidEntryPoint

import android.os.Looper
import android.provider.Settings
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.silvacorp.taxitag.common.components.TaxiDialog
import com.silvacorp.taxitag.location.presentation.TaxiLocationActivity

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private var currentLocation = ""

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        startLocationUpdates()
        setContent {
            var locationPermissionsGranted by remember { mutableStateOf(areLocationPermissionsAlreadyGranted()) }
            var shouldShowPermissionRationale by remember {
                mutableStateOf(
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }

            var shouldDirectUserToApplicationSettings by remember {
                mutableStateOf(false)
            }

            var currentPermissionsStatus by remember {
                mutableStateOf(decideCurrentPermissionStatus(locationPermissionsGranted, shouldShowPermissionRationale))
            }

            val locationPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            val locationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    locationPermissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
                        acc && isPermissionGranted
                    }

                    if (!locationPermissionsGranted) {
                        shouldShowPermissionRationale =
                            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }
                    shouldDirectUserToApplicationSettings = !shouldShowPermissionRationale && !locationPermissionsGranted
                    currentPermissionsStatus = decideCurrentPermissionStatus(locationPermissionsGranted, shouldShowPermissionRationale)
                })

            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(key1 = lifecycleOwner, effect = {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START &&
                        !locationPermissionsGranted &&
                        !shouldShowPermissionRationale) {
                        locationPermissionLauncher.launch(locationPermissions)
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
            )

            val minor = viewModel.minor.collectAsState()
            val mayor = viewModel.mayor.collectAsState()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    for (lo in p0.locations) {
                        // Update UI with location data
                        currentLocation = "${lo.latitude}, ${lo.longitude}"
                    }
                }
            }
            TaxiTagTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if(viewModel.success.value){
                        TaxiDialog(text = "Se registro correctamente ", showDialog = {viewModel.onDismiss()} )
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center

                    ) {
                        Column(
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                        ) {
                            TextField(
                                value = viewModel.numberEconomic.value,
                                onValueChange = {
                                    viewModel.onChangeNumber(it)
                                },
                                label = { Text("Ingresa el numero economico") }, // Etiqueta del campo de texto
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = viewModel.checked.value,
                                    onCheckedChange = { viewModel.onChecked(it) })
                                Text(text = "Soy pasajero")
                            }

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    sendNumber()
                                }
                            ) {
                                Text(text = "Registrar")
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = mayor.value.taxiInfo?.economicalNumber.toString(),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 15.dp),
                                        textAlign = TextAlign.Center,
                                        fontFamily = autoroneRegular
                                    )
                                    Text(
                                        text = "Mayor numero economico",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )

                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = minor.value.taxiInfo?.economicalNumber.toString(),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 15.dp),
                                        textAlign = TextAlign.Center,
                                        fontFamily = autoroneRegular
                                    )
                                    Text(
                                        text = "Menor numero economico",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }


                            }
                            Text(
                                text = "ver todos los Taxis",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                                    .clickable {
                                        navigateToTaxiList()
                                    },
                                textAlign = TextAlign.Center,
                                fontFamily = autoroneRegular
                            )
                            Text(
                                text = "Ver todos en el mapa",
                                modifier = Modifier.fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .clickable {
                                        navigateToMapTaxi()
                                    },
                                textAlign = TextAlign.Center,
                                fontFamily = autoroneRegular
                            )

                        }

                    }
                }
            }
        }


    }

    private fun navigateToMapTaxi() {
        val intent = Intent(this, TaxiLocationActivity::class.java)
        intent.putExtra("showAllTaxis", true)
        startActivity(intent)
    }

    private fun sendNumber() {
        if (isValid()){
            viewModel.insertTaxi(
                createTaxi(viewModel.numberEconomic.value.toInt())
            )
        }else {
            Toast.makeText(this, viewModel.msg.value, Toast.LENGTH_SHORT).show()
            viewModel.onClear()
        }
    }

    private fun isValid(): Boolean{
        var dataIsCorrect = true

        if (viewModel.numberEconomic.value == ""){
            dataIsCorrect = false
            viewModel.changeMessage("El Campo no puede ir vacio")
        }


        return dataIsCorrect
    }


    private fun createTaxi(number: Int): Taxi {
        val time = getUnixTime()
        return Taxi(
            0,
            number,
            time,
            currentLocation,
            viewModel.checked.value,
            0

        )
    }

    private fun navigateToTaxiList() {
        val intent = Intent(this, TaxisActivity::class.java)
        startActivity(intent)
    }



    private fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }

    private fun areLocationPermissionsAlreadyGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun openApplicationSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)).also {
            startActivity(it)
        }
    }

    private fun decideCurrentPermissionStatus(locationPermissionsGranted: Boolean,
                                              shouldShowPermissionRationale: Boolean): String {
        return if (locationPermissionsGranted) "Granted"
        else if (shouldShowPermissionRationale) "Rejected"
        else "Denied"
    }

    override fun onResume() {
        super.onResume()

        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
    }
}

