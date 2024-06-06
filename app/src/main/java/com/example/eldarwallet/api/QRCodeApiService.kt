package com.example.eldarwallet.api

import com.example.eldarwallet.data.QRCodeRequest
import com.example.eldarwallet.data.QRCodeResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface QRCodeApiService {

    // DEJO PUESTA MI API KEY PARA LAS PRUEBAS, EN CASO DE QUERER PROBAR MAS VECES CAMBIARLA PORQUE TIENE TOPE DE 10 PEDIDOS
    @Headers("X-RapidAPI-Key: 53ebac413bmsh5d16cfbb3ce48b9p14a216jsnc82682311564",
        "Content-Type: application/json",
        "X-RapidAPI-Host: neutrinoapi-qr-code.p.rapidapi.com")
    @POST("/qr-code")
    suspend fun generateQRCode(@Body content: QRCodeRequest): Response<ResponseBody>

}