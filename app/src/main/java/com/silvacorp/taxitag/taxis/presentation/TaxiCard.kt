package com.silvacorp.taxitag.taxis.presentation

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silvacorp.taxitag.R
import com.silvacorp.taxitag.common.unixToDate
import com.silvacorp.taxitag.location.presentation.TaxiLocationActivity
import com.silvacorp.taxitag.register.data.database.entities.Taxi
import com.silvacorp.taxitag.ui.theme.autoroneRegular
import com.silvacorp.taxitag.ui.theme.redTaxi

@Composable
fun TaxiCard(taxi: Taxi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp),

        ) {
        val context = LocalContext.current
        Column(
            horizontalAlignment = CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 10.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Image(
                        modifier = Modifier
                            .size(80.dp)
                            .clickable {
                                val intent = Intent(context, TaxiLocationActivity::class.java)
                                intent.putExtra("showAllTaxis", false)
                                intent.putExtra("number", taxi.economicalNumber)
                                intent.putExtra("location", taxi.location)
                                intent.putExtra("dataSeen", taxi.dateSeen.toInt().unixToDate())
                                intent.putExtra("taxiNumber", 0)
                                context.startActivity(intent)

                            },
                        painter = painterResource(id = R.drawable.maps),
                        contentDescription = null
                    )
                }

                Column(
                    horizontalAlignment = CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .size(60.dp)
                            .align(CenterHorizontally),
                        painter = if (taxi.arePassenger)
                            painterResource(id = R.drawable.taxi_aborded)
                        else
                            painterResource(id = R.drawable.taxi),
                        contentDescription = null
                    )
                    Text(
                        text = "NUMERO ECONOMICO",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = taxi.economicalNumber.toString(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        fontFamily = autoroneRegular,
                        color = redTaxi
                    )
                    Text(text = "Registrado el: ")
                    Text(text = taxi.dateSeen.toInt().unixToDate())
                }

            }


        }
    }

}