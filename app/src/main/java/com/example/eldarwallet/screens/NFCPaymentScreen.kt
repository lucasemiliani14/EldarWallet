package com.example.eldarwallet.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eldarwallet.R
import com.example.eldarwallet.data.CreditCard
import com.example.eldarwallet.database.AppDatabase
import com.example.eldarwallet.util.EncryptionUtils
import com.example.eldarwallet.util.Util
import com.example.eldarwallet.viewmodels.CreditCardListViewmodel
import kotlinx.coroutines.delay

// SE MUESTRA UN BOTON QUE SIMULARIA LA ACCION DE NFC, LO QUE HACE ES MOSTRAR UN MONTO RANDOM MOSTRANDO LO QUE SERIA UN MONTO DE PAGO
@Composable
fun NFCPaymentScreen(navController: NavController, userId: Int)  {

    val context = LocalContext.current
    val appDatabase = AppDatabase.instance(context = context)
    val creditCardListViewmodel = CreditCardListViewmodel(appDatabase, userId)

    val cards by creditCardListViewmodel.creditCardList.observeAsState(initial = emptyList())

    var expanded by remember { mutableStateOf(false) }
    var selectedCreditCard by remember { mutableStateOf<CreditCard?>(null) }

    val monto = remember { mutableStateOf("") }

    val showSuccessText = remember { mutableStateOf(false) }

    fun restartValues() {
        expanded = false
        selectedCreditCard = null
        monto.value = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = stringResource(id = R.string.nfc_payment), style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { monto.value = Util.generarNumeroAleatorioPagoNfc() },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(120.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSecondaryContainer)
        ) {
            Text(text = stringResource(id = R.string.nfc_simulation_button))
        }

        Spacer(modifier = Modifier.height(21.dp))

        Text(
            text = stringResource(id = R.string.payment),
            fontSize = 24.sp,
            modifier = Modifier.padding(10.dp)
        )

        Text(
            text = monto.value,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            modifier = Modifier.padding(10.dp)
        )


        OutlinedButton(onClick = { expanded = true }, enabled = (cards.isNotEmpty()), modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(60.dp)) {
            if (selectedCreditCard != null) {
                Text(text = "${selectedCreditCard?.marca}: ${Util.formatCreditCardNumber(EncryptionUtils()
                    .decryptStringFixedKey(selectedCreditCard!!.numero))}")
            } else {
                if (cards.isEmpty()) {
                    Text(stringResource(id = R.string.no_credit_cards))
                } else {
                    Text(stringResource(id = R.string.select_credit_card))
                }

            }
        }

        Button(
            onClick = { showSuccessText.value = true
                        restartValues()},
            enabled = selectedCreditCard != null && monto.value.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp)
                .padding(top = 16.dp))
        {
            Text(text = stringResource(id = R.string.make_payment))
        }

        Spacer(modifier = Modifier.height(21.dp))

        Box() {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                cards.forEach { card ->
                    DropdownMenuItem(
                        onClick = { expanded = false; selectedCreditCard = card },
                        text = { Text(text = "${card.marca}:" +
                                " ${Util.formatCreditCardNumber(EncryptionUtils().decryptStringFixedKey(card.numero))}") })
                }
            }
        }


        if (showSuccessText.value) {
            Text(
                text = stringResource(id = R.string.nfc_payment_success),
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
fun NFCPaymentScreenPreview() {
    NFCPaymentScreen(rememberNavController(), 0)
}