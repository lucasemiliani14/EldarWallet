package com.example.eldarwallet.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eldarwallet.R
import com.example.eldarwallet.data.CreditCard
import com.example.eldarwallet.database.AppDatabase
import com.example.eldarwallet.util.EncryptionUtils
import com.example.eldarwallet.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// PANTALLA DE CREACION DE NUEVAS TARJETAS, DEJO TODOS LOS CAMPOS HARDCODEADOS PARA ACELERAR LAS PRUEBAS
@Composable
fun AddNewCreditCardScreen(navController: NavController, userId: Int) {

    val numeroTarjeta = remember { mutableStateOf("4124123412341234") }
    val expireMonth = remember { mutableStateOf("07") }
    val expireYear= remember { mutableStateOf("27") }
    val codigoSeguridad = remember { mutableStateOf("123") }

    val showErrorText = remember { mutableStateOf(false) }
    val showSuccessText = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val appDatabase = AppDatabase.instance(context = context)

    fun clearTextFields() {
        numeroTarjeta.value = ""
        expireMonth.value = ""
        expireYear.value = ""
        codigoSeguridad.value = ""
    }

    fun createCreditCard(): CreditCard {
        return CreditCard(numero = EncryptionUtils().encryptStringFixedKey(numeroTarjeta.value),
            codigoSeguridad = EncryptionUtils().encryptStringFixedKey(codigoSeguridad.value),
            fechaVencimiento = EncryptionUtils().encryptStringFixedKey(expireMonth.value + expireYear.value),
            userId = userId,
            marca = Util.getCardBrand(numeroTarjeta.value))
    }

    fun onAddCardClick() {
        val newCreditCard = createCreditCard()
        if (validateCreditCard(numeroTarjeta.value, codigoSeguridad.value, expireMonth.value + expireYear.value)) {
            CoroutineScope(Dispatchers.Main).launch {
                appDatabase.creditCardDao().insert(newCreditCard)
                if (appDatabase.creditCardDao().getCreditCardByNumber(newCreditCard.numero) != null) {
                    showSuccessText.value = true
                    clearTextFields()
                } else {
                    showErrorText.value = true
                    clearTextFields()
                }
            }
        } else {
            showErrorText.value = true
            clearTextFields()
        }
    }


    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = stringResource(id = R.string.add_card), style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = numeroTarjeta.value,
            onValueChange = { numeroTarjeta.value = it },
            label = { Text(stringResource(id = R.string.card_number)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Vencimiento:")

        Row(
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            OutlinedTextField(
                value = expireMonth.value,
                onValueChange = { expireMonth.value = it },
                label = { Text(stringResource(id = R.string.month)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(0.5f)
            )

            OutlinedTextField(
                value = expireYear.value,
                onValueChange = { expireYear.value = it },
                label = { Text(stringResource(id = R.string.year)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = codigoSeguridad.value,
            onValueChange = { codigoSeguridad.value = it },
            label = { Text(stringResource(id = R.string.security_code)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAddCardClick() },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(stringResource(id = R.string.add_card))
        }

        if (showErrorText.value) {
            Text(
                text = stringResource(id = R.string.invalid_card),
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(21.dp)
            )
        }

        if (showSuccessText.value) {
            Text(
                text = stringResource(id = R.string.added_card_succsess),
                color = Color.Green,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(21.dp)

            )
            LaunchedEffect(Unit) {
                delay(2000)
                showSuccessText.value = false
            }
        }

    }

}





@Preview(showBackground = true)
@Composable
fun AddNewCreditCardScreenPreview() {
    AddNewCreditCardScreen(rememberNavController(), 0)
}

fun validateCreditCard(numero: String, codigoSeguridad: String, fechaVencimiento: String): Boolean {

    return Util.isNumeroTarjetaValid(numero) &&
            Util.isCodigoSeguridadValid(codigoSeguridad) &&
            Util.isFechaVencimientoValid(fechaVencimiento)

}