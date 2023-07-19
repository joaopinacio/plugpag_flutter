package dev.joaop.plugpag_flutter.presentation.auth

import br.com.uol.pagseguro.plugpag.PlugPagAuthenticationListener
import br.com.uol.pagseguro.plugpag.PlugPagInitializationResult
import io.reactivex.rxjava3.core.Single

interface AuthenticationUseCaseContract {

    fun checkAuthentication(): Single<Boolean>

    fun requestAuthentication(authenticationListener: PlugPagAuthenticationListener)

    fun invalidateAuthentication()

    fun isSerialDevice(): Boolean

    fun sleep()

    fun wakeup()

}