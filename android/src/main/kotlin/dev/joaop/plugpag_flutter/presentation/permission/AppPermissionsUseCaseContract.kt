package dev.joaop.plugpag_flutter.presentation.permission

import android.app.Activity

interface AppPermissionsUseCaseContract {

    fun getMissingPermissions(): Array<String>

    fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int)

}