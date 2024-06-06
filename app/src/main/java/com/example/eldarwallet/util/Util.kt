package com.example.eldarwallet.util

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import okhttp3.ResponseBody
import java.util.Calendar
import java.util.Random

object Util {

    fun isEmailValid(email: String): Boolean {
        val regex = Regex(
            "[a-zA-Z0-9\\.\\_\\-\\+]+@[a-zA-Z0-9\\.\\-\\_]+\\.[a-zA-Z]{2,}"
        )
        return regex.matches(email)
    }

    fun isPasswordValid(password: String): Boolean {
        val longitud = password.length
        return longitud in 6..20
    }

    fun isNombreValid(nombre: String): Boolean {
        val longitud = nombre.length
        return longitud in 2..25
    }

    fun isApellidoValid(apellido: String): Boolean {
        val longitud = apellido.length
        return longitud in 2..25
    }

    fun isNumeroTarjetaValid(numeroTarjeta: String): Boolean {
        val longitud = numeroTarjeta.length
        return longitud == 16 || longitud == 15
    }

    fun isCodigoSeguridadValid(codigoSeguridad: String): Boolean {
        val longitud = codigoSeguridad.length
        return longitud == 3 || longitud == 4
    }

    fun formatCreditCardNumber(creditCardNumber: String): String {
        var lastFourDigits = ""
        if (creditCardNumber.length == 15) {
            lastFourDigits = creditCardNumber.substring(11, 15)
        } else if(creditCardNumber.length == 16) {
            lastFourDigits = creditCardNumber.substring(12, 16)
        }
        return "**** $lastFourDigits"
    }

    fun getCardBrand(cardNumber: String): String {
        return if (cardNumber.startsWith("3")) {
            "Amex"
        } else if (cardNumber.startsWith("4")) {
            "Visa"
        } else if (cardNumber.startsWith("5")) {
            "Mastercard"
        } else {
            "Desconocido"
        }
    }

    fun generateRandomKeyOf16Chars(): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random()
        val sb = StringBuilder(16) // Fixed length of 16

        while (sb.length < 16) {
            sb.append(allowedChars[random.nextInt(allowedChars.length)])
        }

        return sb.toString()
    }

    fun generarNumeroAleatorioPagoNfc(min: Int = 100, max: Int = 1000): String {
        val random = Random()
        val rango = max - min
        val numeroAleatorio = random.nextInt(rango) + min

        return "$ $numeroAleatorio"
    }

    fun isFechaVencimientoValid(fechaVencimiento: String): Boolean {
        if (fechaVencimiento.length != 4) {
            return false
        }

        val monthString = fechaVencimiento.substring(0, 2)
        val yearString = fechaVencimiento.substring(2)

        val month = try {
            monthString.toInt()
        } catch (e: NumberFormatException) {
            return false
        }

        if (month < 1 || month > 12) {
            return false
        }

        val year = try {
            yearString.toInt()
        } catch (e: NumberFormatException) {
            return false
        }

        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString().substring(2)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

        if (currentYear == yearString) {
            return month >= currentMonth
        }

        return year >= currentYear.toInt()
    }

    final const val base64 = "iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAIAAADTED8xAAARBUlEQVR4Xu3TQY7kyo4AwX//S8+sfGOAQLyAKhVqhS0bToqpLv3v/47jw/7nPxzHl5wP4Pi08wEcn3Y+gOPTzgdwfNr5AI5POx/A8WnnAzg+7XwAx6edD+D4tPMBHJ92PoDj084HcHza+QCOTzsfwPFp5wM4Pu18AMennQ/g+LTzARyfdj6A49POB3B82vkAjk87H8DxaecDOD7tfADHp50P4Pi08wEcn3Y+gOPTzgdwfNr5AI5POx/A8WnnAzg+7XwAx6edD+D4tPMBHJ92PoDj084HcHza+QCOTzsfwPFp5wM4Pu18AMennQ/g+LTzARyftsUH8L+X8O7YTZyfOB+72O3Ku5+wxxEv4d2xmzg/cT52sduVdz9hjyNewrtjN3F+4nzsYrcr737CHke8hHfHbuL8xPnYxW5X3v2EPY54Ce+O3cT5ifOxi92uvPsJexzxEt4du4nzE+djF7tdefcT9jjiJbw7dhPnJ87HLna78u4n7HHES3h37CbOT5yPXex25d1P2OOIC3a/4h2xmzgfu4nzsYtd7H7FO2L3hD2OuGD3K94Ru4nzsZs4H7vYxe5XvCN2T9jjiAt2v+IdsZs4H7uJ87GLXex+xTti94Q9jrhg9yveEbuJ87GbOB+72MXuV7wjdk/Y44gLdr/iHbGbOB+7ifOxi13sfsU7YveEPY64YPcr3hG7ifOxmzgfu9jF7le8I3ZP2OOIC3a/4h2xmzgfu4nzsYtd7H7FO2L3hD2OuGD3K94Ru4nzsZs4H7vYxe5XvCN2T9jjiAt2sVvl3tjFLnaxi13sYhe72MUudqvcG7vYPWGPIy7YxW6Ve2MXu9jFLnaxi13sYhe72K1yb+xi94Q9jrhgF7tV7o1d7GIXu9jFLnaxi13sYrfKvbGL3RP2OOKCXexWuTd2sYtd7GIXu9jFLnaxi90q98Yudk/Y44gLdrFb5d7YxS52sYtd7GIXu9jFLnar3Bu72D1hjyMu2MVulXtjF7vYxS52sYtd7GIXu9itcm/sYveEPY64YBe7Ve6NXexiF7vYxS52sYtd7GK3yr2xi90T9jjigl3sVrk3drGLXexiF7vYxS52sYvdKvfGLnZP2OOIC3axW+Xe2MUudrGbOB+7ifOxi90q98Yudk/Y44gLdrFb5d7YxS52sZs4H7uJ87GL3Sr3xi52T9jjiAt2sVvl3tjFLnaxmzgfu4nzsYvdKvfGLnZP2OOIC3axW+Xe2MUudrGbOB+7ifOxi90q98Yudk/Y44gLdrFb5d7YxS52sZs4H7uJ87GL3Sr3xi52T9jjiAt2sVvl3tjFLnaxmzgfu4nzsYvdKvfGLnZP2OOIC3axW+Xe2MUudrGbOB+7ifOxi90q98Yudk/Y44gLdrFb5d7YxS52sZs4H7uJ87GL3Sr3xi52T9jjiAt2sVvl3tjFLnZ383kT52MXu1XujV3snrDHERfsYrfKvbGLXezu5vMmzscudqvcG7vYPWGPIy7YxW6Ve2MXu9jdzedNnI9d7Fa5N3axe8IeR1ywi90q98YudrG7m8+bOB+72K1yb+xi94Q9jrhgF7tV7o1d7GJ3N583cT52sVvl3tjF7gl7HHHBLnar3Bu72MXubj5v4nzsYrfKvbGL3RP2OOKCXexWuTd2sYvd3XzexPnYxW6Ve2MXuyfsccQFu9itcm/sYhe7u/m8ifOxi90q98Yudk/Y44gLdr/iHbHblXfH7le8I3ZP2OOIC3a/4h2x25V3x+5XvCN2T9jjiAt2v+IdsduVd8fuV7wjdk/Y44gLdr/iHbHblXfH7le8I3ZP2OOIC3a/4h2x25V3x+5XvCN2T9jjiAt2v+IdsduVd8fuV7wjdk/Y44gLdr/iHbHblXfH7le8I3ZP2OOIC3a/4h2x25V3x+5XvCN2T9jjiJfw7tjFLnaxi13sYrcr737CHke8hHfHLnaxi13sYhe7XXn3E/Y44iW8O3axi13sYhe72O3Ku5+wxxEv4d2xi13sYhe72MVuV979hD2OeAnvjl3sYhe72MUudrvy7ifsccRLeHfsYhe72MUudrHblXc/YY8jXsK7Yxe72MUudrGL3a68+wl7HPES3h272MUudrGLXex25d1P2OKIf5X/4RPnj793Xvof8g984vzx985L/0P+gU+cP/7eeel/yD/wifPH3zsv/Q/5Bz5x/vh756X/If/AJ84ff++89D/kH/jE+ePvnZf+h/wDnzh//L0tXrp/CHfzeRPnYxe72MUudrGbOD9xfpV7Yxe7J+xxxB/zeRPnYxe72MUudrGbOD9xfpV7Yxe7J+xxxB/zeRPnYxe72MUudrGbOD9xfpV7Yxe7J+xxxB/zeRPnYxe72MUudrGbOD9xfpV7Yxe7J+xxxB/zeRPnYxe72MUudrGbOD9xfpV7Yxe7J+xxxB/zeRPnYxe72MUudrGbOD9xfpV7Yxe7J+xxxB/zeRPnYxe72MUudrGbOD9xfpV7Yxe7J+xxxB/zeRPnYxe72MUudrGbOD9xfpV7Yxe7J+xxxH/kfOwmzt/N502cnzj/Nv6eJ+xxxH/kfOwmzt/N502cnzj/Nv6eJ+xxxH/kfOwmzt/N502cnzj/Nv6eJ+xxxH/kfOwmzt/N502cnzj/Nv6eJ+xxxH/kfOwmzt/N502cnzj/Nv6eJ+xxxH/kfOwmzt/N502cnzj/Nv6eJ+xxxH/kfOwmzt/N502cnzj/Nv6eJ+xxxH/kfOwmzt/N502cnzj/Nv6eJ2xxxBVf2Cr3xm7i/Cr3xm7i/Cr3rnLvxPknbHHEFV/YKvfGbuL8KvfGbuL8Kveucu/E+SdsccQVX9gq98Zu4vwq98Zu4vwq965y78T5J2xxxBVf2Cr3xm7i/Cr3xm7i/Cr3rnLvxPknbHHEFV/YKvfGbuL8KvfGbuL8Kveucu/E+SdsccQVX9gq98Zu4vwq98Zu4vwq965y78T5J2xxxBVf2Cr3xm7i/Cr3xm7i/Cr3rnLvxPknbHHEFV/YKvfGbuL8KvfGbuL8Kveucu/E+SfsccRN3Bu72E2cX+XeifOxmzi/yr2xi91OtjjOF7bKvbGL3cT5Ve6dOB+7ifOr3Bu72O1ki+N8YavcG7vYTZxf5d6J87GbOL/KvbGL3U62OM4Xtsq9sYvdxPlV7p04H7uJ86vcG7vY7WSL43xhq9wbu9hNnF/l3onzsZs4v8q9sYvdTrY4zhe2yr2xi93E+VXunTgfu4nzq9wbu9jtZIvjfGGr3Bu72E2cX+XeifOxmzi/yr2xi91OtjjOF7bKvbGL3cT5Ve6dOB+7ifOr3Bu72O1k6+P+mv9Rq9y7yr2xmzgfu7v5vInzT9jiiKf4H7LKvavcG7uJ87G7m8+bOP+ELY54iv8hq9y7yr2xmzgfu7v5vInzT9jiiKf4H7LKvavcG7uJ87G7m8+bOP+ELY54iv8hq9y7yr2xmzgfu7v5vInzT9jiiKf4H7LKvavcG7uJ87G7m8+bOP+ELY54iv8hq9y7yr2xmzgfu7v5vInzT9jiiKf4H7LKvavcG7uJ87G7m8+bOP+ELY54O/9jJ87HbuL8KvdOnH+zf+rHPMU/kInzsZs4v8q9E+ff7J/6MU/xD2TifOwmzq9y78T5N/unfsxT/AOZOB+7ifOr3Dtx/s3+qR/zFP9AJs7HbuL8KvdOnH+zf+rHPMU/kInzsZs4v8q9E+ff7J/6MU/xD2TifOwmzq9y78T5N/unfsxT/AOZOB+7ifOr3Dtx/s22+DG+4F159918XuxWufduPu8NtjjaF7kr776bz4vdKvfezee9wRZH+yJ35d1383mxW+Xeu/m8N9jiaF/krrz7bj4vdqvcezef9wZbHO2L3JV3383nxW6Ve+/m895gi6N9kbvy7rv5vNitcu/dfN4bbHG0L3JX3n03nxe7Ve69m897gy2O9kXuyrvv5vNit8q9d/N5b7DF0b7I2P2Kd8Tubj5vlXtjF7vYTZyPXeyesMcRF+x+xTtidzeft8q9sYtd7CbOxy52T9jjiAt2v+Idsbubz1vl3tjFLnYT52MXuyfsccQFu1/xjtjdzeetcm/sYhe7ifOxi90T9jjigt2veEfs7ubzVrk3drGL3cT52MXuCXscccHuV7wjdnfzeavcG7vYxW7ifOxi94Q9jrhg9yveEbu7+bxV7o1d7GI3cT52sXvCHkdcsPsV74jd3XzeKvfGLnaxmzgfu9g9YY8jLtjFbpV7Yxe7u/m82P2Kd6xy7062OM4XFrvYrXJv7GJ3N58Xu1/xjlXu3ckWx/nCYhe7Ve6NXezu5vNi9yvescq9O9niOF9Y7GK3yr2xi93dfF7sfsU7Vrl3J1sc5wuLXexWuTd2sbubz4vdr3jHKvfuZIvjfGGxi90q98YudnfzebH7Fe9Y5d6dbHGcLyx2sVvl3tjF7m4+L3a/4h2r3LuTLY7zhcUudqvcG7vY3c3nxe5XvGOVe3eyxXG+sNjFbpV7Yxe72E2cnzgfu9jF7m4+L3Y72eI4X1jsYrfKvbGLXewmzk+cj13sYnc3nxe7nWxxnC8sdrFb5d7YxS52E+cnzscudrG7m8+L3U62OM4XFrvYrXJv7GIXu4nzE+djF7vY3c3nxW4nWxznC4td7Fa5N3axi93E+YnzsYtd7O7m82K3ky2O84XFLnar3Bu72MVu4vzE+djFLnZ383mx28kWx/nCYhe7Ve6NXexiN3F+4nzsYhe7u/m82O1ki+N8YbGL3Sr3xi52sZs4P3E+drGL3d18Xux2ssVxvrDYxW6Ve2MXu9hNnI/dxPmJ87GL3cT5N9jiaF9k7GK3yr2xi13sJs7HbuL8xPnYxW7i/BtscbQvMnaxW+Xe2MUudhPnYzdxfuJ87GI3cf4NtjjaFxm72K1yb+xiF7uJ87GbOD9xPnaxmzj/Blsc7YuMXexWuTd2sYvdxPnYTZyfOB+72E2cf4MtjvZFxi52q9wbu9jFbuJ87CbOT5yPXewmzr/BFkf7ImMXu1XujV3sYjdxPnYT5yfOxy52E+ffYIujfZGxi90q98YudrGbOB+7ifMT52MXu4nzb7DF0b7I2P2Kd8QudrGLXewmzt/N561yb+yesMcRF+x+xTtiF7vYxS52E+fv5vNWuTd2T9jjiAt2v+IdsYtd7GIXu4nzd/N5q9wbuyfsccQFu1/xjtjFLnaxi93E+bv5vFXujd0T9jjigt2veEfsYhe72MVu4vzdfN4q98buCXscccHuV7wjdrGLXexiN3H+bj5vlXtj94Q9jrhg9yveEbvYxS52sZs4fzeft8q9sXvCHkdcsPsV74hd7GIXu9hNnL+bz1vl3tg9YY8jXsK7YzdxfuL8KvfGbpV732CLo32Ru/Lu2E2cnzi/yr2xW+XeN9jiaF/krrw7dhPnJ86vcm/sVrn3DbY42he5K++O3cT5ifOr3Bu7Ve59gy2O9kXuyrtjN3F+4vwq98ZulXvfYIujfZG78u7YTZyfOL/KvbFb5d432OJoX+SuvDt2E+cnzq9yb+xWufcNtjjaF7kr747dxPmJ86vcG7tV7n2DVx59HHc5H8DxaecDOD7tfADHp50P4Pi08wEcn3Y+gOPTzgdwfNr5AI5POx/A8WnnAzg+7XwAx6edD+D4tPMBHJ92PoDj084HcHza+QCOTzsfwPFp5wM4Pu18AMennQ/g+LTzARyfdj6A49POB3B82vkAjk87H8DxaecDOD7tfADHp50P4Pi08wEcn3Y+gOPTzgdwfNr5AI5POx/A8WnnAzg+7XwAx6edD+D4tPMBHJ92PoDj084HcHza+QCOTzsfwPFp5wM4Pu18AMen/T/n88r8rBc6ogAAAABJRU5ErkJggg=="

}

