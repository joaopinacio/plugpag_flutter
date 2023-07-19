package dev.joaop.plugpag_flutter.data.model

data class TransactionSummary(
    val id: Int,
    val date: String,
    val time: String,
    val transactionId: String,
    val transactionCode: String,
    val cardBrand: String,
    val value: String,
    val method: Int
)