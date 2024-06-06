package com.example.eldarwallet.screens

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eldarwallet.R
import com.example.eldarwallet.data.User
import com.example.eldarwallet.database.AppDatabase
import com.example.eldarwallet.util.EncryptionUtils
import com.example.eldarwallet.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// PANTALLA DE CREACION DE USUARIO, DEJO TODOS LOS CAMPOS HARDCODEADOS PARA ACELERAR LAS PRUEBAS

@Composable
fun CreateUserScreen() {

    val nombre = remember { mutableStateOf("Eldar") }
    val apellido = remember { mutableStateOf("Wallet") }
    val email = remember { mutableStateOf("EldarWallet@Gmail.com") }
    val password = remember { mutableStateOf("eldarwallet") }
    val repeatPassword = remember { mutableStateOf("eldarwallet") }

    val showErrorText = remember { mutableStateOf(false) }
    val showSuccessText = remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun clearTextFields() {
        nombre.value = ""
        apellido.value = ""
        email.value = ""
        password.value = ""
        repeatPassword.value = ""
    }

    fun onCreateUserClick() {
        if (validateCredentials(email.value, password.value, repeatPassword.value, nombre.value, apellido.value)){
            val database = AppDatabase.instance(context)

            val newUser = createUser(email.value, password.value, nombre.value, apellido.value)
            CoroutineScope(Dispatchers.IO).launch {
                if (database.userDao().getUserByEmail(email.value) !=null) {
                    clearTextFields()
                    showErrorText.value = true
                    return@launch
                }
                database.userDao().insertUser(newUser)
                val userFromDatabase = database.userDao().getUserByEmail(newUser.email)
                if (userFromDatabase != null) {
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

        Text(text = stringResource(id = R.string.create_new_user), style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre.value,
            onValueChange =  { nombre.value = it } ,
            label =  { Text(stringResource(id = R.string.name)) } ,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = apellido.value,
            onValueChange =  { apellido.value = it } ,
            label =  { Text(stringResource(id = R.string.surname)) } ,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text(stringResource(id = R.string.email)) },
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

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = repeatPassword.value,
            onValueChange = { repeatPassword.value = it },
            label = { Text(stringResource(id = R.string.repeat_password)) },
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onCreateUserClick() },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(stringResource(id = R.string.create_user))
        }

        if (showErrorText.value) {
            Text(
                text = stringResource(id = R.string.create_user_error),
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(21.dp)
            )
        }

        if (showSuccessText.value) {
            Text(
                text = stringResource(id = R.string.user_created),
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
fun CreateUserPreview() {
    CreateUserScreen()
}

fun validateCredentials(email: String, password: String, repeatPassword: String, nombre: String, apellido: String): Boolean {
    return (Util.isEmailValid(email) &&
            Util.isPasswordValid(password) &&
            Util.isNombreValid(nombre) &&
            Util.isApellidoValid(apellido) &&
            (password == repeatPassword))
}

fun createUser(email: String, password: String, nombre: String, apellido: String): User {
    val userKey = Util.generateRandomKeyOf16Chars()
    val encryptedPassword = EncryptionUtils().encryptStringFixedKey(password, userKey)

    return User(email = email, password = encryptedPassword, nombre = nombre, apellido = apellido, key = userKey)
}

