package com.example.eldarwallet.navigation

sealed class AppScreens(val route: String) {

    data object LoginScreen: AppScreens("login_screen")

    data object CreateUser: AppScreens("create_user")

    data object HomeScreen: AppScreens("home_screen")

    data object AddNewCreditCardScreen: AppScreens("add_new_credit_card_screen")

    data object NFCPaymentScreen: AppScreens("nfc_payment_screen")


}