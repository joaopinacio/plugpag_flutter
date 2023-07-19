package dev.joaop.plugpag_flutter.presentation.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PlugPagPermission (
    private val activity: android.app.Activity
) {
  private val PERMISSIONS_REQUEST_CODE = 0x1234
  
  /**
   * Requests permissions on runtime, if any needed permission is not granted.
   */
   fun requestPermissions() {
    // Log.d("PLUGPAG_LOG", "Requested Permission 1")
    val missingPermissions = this.filterMissingPermissions(this.getManifestPermissions())

    if (missingPermissions.isNotEmpty()) {
      ActivityCompat.requestPermissions(this.activity, missingPermissions, PERMISSIONS_REQUEST_CODE)
    }
  }

  /**
   * Returns a list of permissions requested on the AndroidManifest.xml file.
   *
   * @return Permissions requested on the AndroidManifest.xml file.
   */
  private fun getManifestPermissions(): List<String> {
    return listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    )
  }

  /**
   * Filters only the permissions still not granted.
   *
   * @param permissions List of permissions to be checked.
   * @return Permissions not granted.
   */
  private fun filterMissingPermissions(permissions: List<String>): Array<String> {
    return permissions
      .filter { permission ->
        ContextCompat.checkSelfPermission(
                this.activity.applicationContext,
                permission
        ) != PackageManager.PERMISSION_GRANTED
      }
      .toTypedArray()
  }
}