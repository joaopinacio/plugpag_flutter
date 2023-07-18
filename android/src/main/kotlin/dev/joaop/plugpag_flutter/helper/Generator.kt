package dev.joaop.plugpag_flutter.helper

import java.util.Random

object Generator {
    // -----------------------------------------------------------------------------------------------------------------
    // Payment value generator
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Generates a random payment value.
     *
     * @return Random payment value.
     */
    fun generateValue(): Int {
        return Random(System.currentTimeMillis()).nextInt(400) + 100
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Installments generator
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Generates a random amount of installments.
     *
     * @return Random amount of installments.
     */
    fun generateInstallments(): Int {
        return Random(System.currentTimeMillis()).nextInt(10) + 2
    }
}