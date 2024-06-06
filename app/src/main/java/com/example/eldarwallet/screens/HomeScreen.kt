package com.example.eldarwallet.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eldarwallet.R
import com.example.eldarwallet.database.AppDatabase
import com.example.eldarwallet.navigation.AppScreens
import com.example.eldarwallet.util.EncryptionUtils
import com.example.eldarwallet.util.Util
import com.example.eldarwallet.viewmodels.CreditCardListViewmodel
import com.example.eldarwallet.viewmodels.QRViewModel
import com.example.eldarwallet.viewmodels.UserViewmodel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, userId: Int) {

    val context = LocalContext.current
    val appDatabase = AppDatabase.instance(context = context)

    val userViewmodel = UserViewmodel(appDatabase, userId)
    val creditCardListViewmodel = CreditCardListViewmodel(appDatabase, userId)

    val user by userViewmodel.user.observeAsState()
    val cards by creditCardListViewmodel.creditCardList.observeAsState(initial = emptyList())

    val qrViewModel = QRViewModel()
    val qrCode = qrViewModel.qrCode.observeAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        user?.let {
            Text(
                text = stringResource(id = R.string.hello) + " ${it.nombre},",
                fontSize = 24.sp,
                modifier = Modifier.padding(10.dp)
            )
        }

        Text(
            text = stringResource(id = R.string.balance),
            fontSize = 24.sp,
            modifier = Modifier.padding(10.dp)
        )

        user?.let {
            Text(
                text = stringResource(id = R.string.money) + " " + it.saldo.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                modifier = Modifier.padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.height(21.dp))


        if (cards.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.cards),
                fontSize = 21.sp,
                modifier = Modifier.padding(21.dp)
            )
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp))
        {
            items(cards.size) { index ->
                val card = cards[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = Util.formatCreditCardNumber(EncryptionUtils().decryptStringFixedKey(card.numero)),
                            style = MaterialTheme.typography.titleSmall
                        )

                        Text(text = card.marca, style = MaterialTheme.typography.titleSmall)
                    }
                }
            }
        }

        Card(
            onClick = { goToAddNewCard(navController, userId) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    Modifier.padding(16.dp)
                )

                Text(text = stringResource(id = R.string.add_card), Modifier.padding(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            onClick = { goToNFCPaymentScreen(navController, userId) },
            modifier = Modifier.size(200.dp, 50.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(text = stringResource(id = R.string.nfc_payment))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            onClick = {qrViewModel.generateQR(user?.nombre ?: "",  user?.apellido ?: "")},
            modifier = Modifier.size(200.dp, 50.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(text = stringResource(id = R.string.show_qr))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        qrCode.value?.let {
            Image(
                bitmap = it,
                contentDescription = stringResource(id = R.string.image_qr_id)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController(), 0)
}

fun goToAddNewCard(navController: NavController, userId: Int) {
    navController.navigate(AppScreens.AddNewCreditCardScreen.route + "/$userId")
}

fun goToNFCPaymentScreen(navController: NavController, userId: Int) {
    navController.navigate(AppScreens.NFCPaymentScreen.route + "/$userId")
}
