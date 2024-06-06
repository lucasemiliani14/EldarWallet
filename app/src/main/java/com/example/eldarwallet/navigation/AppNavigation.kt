package com.example.eldarwallet.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eldarwallet.R
import com.example.eldarwallet.screens.AddNewCreditCardScreen
import com.example.eldarwallet.screens.CreateUserScreen
import com.example.eldarwallet.screens.HomeScreen
import com.example.eldarwallet.screens.LoginScreen
import com.example.eldarwallet.screens.NFCPaymentScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route){

        composable(AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }

        composable(AppScreens.CreateUser.route) {
            CreateUserScreen()
        }

        composable(AppScreens.HomeScreen.route + "/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) {
            HomeScreen(navController, userId = it.arguments!!.getInt("userId"))

        }

        composable(AppScreens.AddNewCreditCardScreen.route + "/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) {
            AddNewCreditCardScreen(navController, userId = it.arguments!!.getInt("userId"))
        }

        composable(AppScreens.NFCPaymentScreen.route + "/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) {
            NFCPaymentScreen(navController, userId = it.arguments!!.getInt("userId"))
        }
    }

}