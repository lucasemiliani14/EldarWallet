package com.example.eldarwallet.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eldarwallet.R
import com.example.eldarwallet.database.AppDatabase
import com.example.eldarwallet.navigation.AppScreens
import com.example.eldarwallet.util.EncryptionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// PANTALLA DE CREACION DE INICIO SESION DEJO TODOS LOS CAMPOS HARDCODEADOS PARA ACELERAR LAS PRUEBAS, CONCUERDAN CON LOS DATOS HARDCODEADOS DEL USUARIO CREADO
@Composable
fun LoginScreen(navController: NavController) {

    val email = remember { mutableStateOf("EldarWallet@Gmail.com") }
    val password = remember { mutableStateOf("eldarwallet") }

    val showErrorText = remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun clearTextFields() {
        email.value = ""
        password.value = ""
    }

    fun onLoginClick() {
        CoroutineScope(Dispatchers.Main).launch {
            val database = AppDatabase.instance(context)
            val userFromEmail = database.userDao().getUserByEmail(email.value)
            if (userFromEmail != null) {
                val decryptedPassword = EncryptionUtils().decryptString(userFromEmail.password, userFromEmail.key)
                if (decryptedPassword == password.value) {
                    navController.navigate(AppScreens.HomeScreen.route + "/${userFromEmail.id}")
                } else {
                    showErrorText.value = true
                    clearTextFields()

                }
            } else {
                showErrorText.value = true
                clearTextFields()
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = stringResource(id = R.string.eldar_wallet), style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange =  { email.value = it } ,
            label =  { Text(stringResource(id = R.string.email)) } ,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(stringResource(id = R.string.password)) },
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onLoginClick() },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(stringResource(id = R.string.login))
        }

        Spacer(modifier = Modifier.height(16.dp))

        ClickableText(
            text = stringResource(id = R.string.create_user),
            textStyle = MaterialTheme.typography.titleSmall,
            onClick = { onCreateUserClick(navController) }
        )

        if (showErrorText.value) {
            Text(
                text = stringResource(id = R.string.invalid_user),
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(21.dp)
            )
        }
    }
}

@Composable
fun ClickableText(
    text: String,
    textStyle: TextStyle,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = textStyle,
        modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(rememberNavController())
}

fun onCreateUserClick(navController: NavController) {
    navController.navigate(AppScreens.CreateUser.route)
}