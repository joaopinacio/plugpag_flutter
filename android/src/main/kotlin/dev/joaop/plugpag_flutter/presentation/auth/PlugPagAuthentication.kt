package dev.joaop.plugpag_flutter.presentation.auth

import android.util.Log
import br.com.uol.pagseguro.plugpag.PlugPag
import br.com.uol.pagseguro.plugpag.PlugPagAuthenticationListener
import br.com.uol.pagseguro.plugpag.exception.PlugPagException

class PlugPagAuthentication (
    private val plugpag: PlugPag
) {
    fun checkAuthentication(): Boolean {
        return plugpag.isAuthenticated
    }

    fun requestAuthentication(authenticationListener: PlugPagAuthenticationListener) {
        try {
            plugpag.requestAuthentication(authenticationListener)
        } catch (e: PlugPagException) {
            Log.d("PLUGPAG_LOG", "$e")
            authenticationListener.onError()
        } catch (e: Exception) {
            Log.d("PLUGPAG_LOG", "$e")
            authenticationListener.onError()
        }
    }

    fun invalidateAuthentication() {
        plugpag.invalidateAuthentication()
    }

    fun isSerialDevice() = !plugpag.serialDevices.isNullOrEmpty()

    fun sleep() = plugpag.sleepPinpad()

    fun wakeup() = plugpag.wakeupPinpad()

    fun abort() = plugpag.abort()
}