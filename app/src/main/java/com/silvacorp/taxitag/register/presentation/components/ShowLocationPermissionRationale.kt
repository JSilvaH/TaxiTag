package com.silvacorp.taxitag.register.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ShowLocationPermissionRationale() {
    AlertDialog(
        onDismissRequest = {
            //Logic when dismiss happens
        },
        title = {
            Text("Permission Required")
        },
        text = {
            Text("You need to approve this permission in order to...")
        },
        confirmButton = {
            TextButton(onClick = {
                //Logic when user confirms to accept permissions
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                //Logic when user denies to accept permissions
            }) {
                Text("Deny")
            }
        })
}