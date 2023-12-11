package com.silvacorp.taxitag.common.components

import android.content.res.Resources.Theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.silvacorp.taxitag.R
import com.silvacorp.taxitag.location.presentation.ui.theme.TaxiTagTheme

@Composable
fun TaxiDialog(
    text: String,
    showDialog: ()-> Unit
) {
    Dialog(onDismissRequest = { showDialog()}) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painterResource(id = R.drawable.icon_info),
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    contentDescription = null
                )

                Text(
                    text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    onClick = { showDialog()}
                ) {
                    Text(text = "Aceptar")
                }

            }
        }
    }
    
}

@Preview
@Composable
fun TaxiDialogPrev() {
    TaxiTagTheme {
        TaxiDialog(text = "Prueba", showDialog = {true})

    }
}