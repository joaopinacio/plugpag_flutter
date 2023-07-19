package dev.joaop.plugpag_flutter.presentation.auth


interface AuthenticationContract {

    interface View {
        fun showLoadingAnimation()
        fun hideLoadingAnimation()
        fun showAuthenticatedSession()
        fun showMissingAuthenticationView()
        fun showToast(message: String)
    }

    interface Presenter {
        fun attach(view: View)
        fun detach()
        fun checkAuthentication()
        fun checkBluetoothDevice()
        fun requestAuthentication()
        fun invalidateAuthentication()
        fun isSerialDevice(): Boolean
        fun sleep()
        fun wakeup()
    }

}