package com.example.eldarwallet.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CreditCard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val numero: String,
    val codigoSeguridad: String,
    val fechaVencimiento: String,
    val userId: Int,
    val marca: String
)
