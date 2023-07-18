package dev.joaop.plugpag_flutter.helper

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

object Bluetooth {
    // -----------------------------------------------------------------------------------------------------------------
    // Bluetooth name fetcher
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Returns the first bluetooth terminal found.
     *
     * @return First bluetooth terminal found.
     */
    val terminal: String?
        get() = findBluetoothDevice(arrayOf("PRO-", "W-", "W+-", "PLUS-", "MCHIP-"))

    /**
     * Returns the first bluetooth pinpad found.
     *
     * @return First bluetooth pinpad found.
     */
    val pinpad: String?
        get() = findBluetoothDevice(arrayOf("PAX-", "MOBI-", "MOBIPIN-"))

    /**
     * Returns the first bluetooth device found that matches one of the given prefixes.
     *
     * @param prefixes Valid prefixes.
     * @return Bluetooth device name.
     */
    private fun findBluetoothDevice(prefixes: Array<String>): String? {
        var name: String? = null
        val adapter: BluetoothAdapter?
        val bondedDevices: Set<BluetoothDevice?>?
        adapter = BluetoothAdapter.getDefaultAdapter()
        bondedDevices = adapter.bondedDevices
        for (device in bondedDevices) {
            if (device != null && device.name != null) {
                for (prefix in prefixes) {
                    if (device.name.startsWith(prefix)) {
                        name = device.name
                        break
                    }
                }
            }
        }
        return name
    }
}