package dev.joaop.plugpag_flutter.extensions

import java.text.NumberFormat
import java.util.Locale

fun Double.toCurrency(): String =
    NumberFormat.getCurrencyInstance(
        Locale("pt", "BR")
    ).format(this)

fun String.removeFormat(): String =
    this.replace("[^\\d]".toRegex(), "")
