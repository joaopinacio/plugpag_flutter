package dev.joaop.plugpag_flutter.presentation.bluetooth

import android.bluetooth.BluetoothAdapter

class BluetoothUseCase : BluetoothUseCaseContract {

    override fun getPairedDevices() = BluetoothAdapter.getDefaultAdapter().bondedDevices.toList()

}