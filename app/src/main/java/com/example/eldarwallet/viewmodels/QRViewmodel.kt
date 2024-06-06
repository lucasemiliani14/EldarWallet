package com.example.eldarwallet.viewmodels

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eldarwallet.api.QRCodeApiService
import com.example.eldarwallet.data.QRCodeRequest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class QRViewModel : ViewModel() {

    private val _qrCode = MutableLiveData<ImageBitmap?>()
    val qrCode: LiveData<ImageBitmap?> = _qrCode

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://neutrinoapi-qr-code.p.rapidapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val qrCodeApiService = retrofit.create(QRCodeApiService::class.java)

    fun generateQR(name: String, surname: String) {
        val qrCodeRequest = QRCodeRequest("$name $surname")
        viewModelScope.launch {
            try {
                val response = qrCodeApiService.generateQRCode(qrCodeRequest)

                if (response.isSuccessful) {
                    val responseBody = response.body() ?: return@launch
                    val imageData = responseBody.bytes()
                    val imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                    _qrCode.value = imageBitmap.asImageBitmap()
                } else {
                    _qrCode.value = null
                }
            } catch (e: Exception) {
                _qrCode.value = null
            }
        }
    }
}
