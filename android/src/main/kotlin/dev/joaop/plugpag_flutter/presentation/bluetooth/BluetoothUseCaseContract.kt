package dev.joaop.plugpag_flutter.presentation.bluetooth

import android.bluetooth.BluetoothDevice

interface BluetoothUseCaseContract {

    fun getPairedDevices(): List<BluetoothDevice>

}