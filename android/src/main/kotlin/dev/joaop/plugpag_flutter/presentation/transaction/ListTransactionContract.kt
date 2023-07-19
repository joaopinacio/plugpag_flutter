package dev.joaop.plugpag_flutter.presentation.transaction

import br.com.uol.pagseguro.plugpag.PlugPagDevice
import dev.joaop.plugpag_flutter.data.model.TransactionSummary

interface ListTransactionContract {
    interface View {
        fun showTransactions(transactions: List<TransactionSummary>)
        fun showVoidTransaction(
            device: PlugPagDevice,
            transaction: TransactionSummary,
            position: Int
        )
    }

    interface Presenter {
        fun attach(view: View)
        fun detach()

        fun loadTransactions()
        fun startVoidPayment(transaction: TransactionSummary)
    }
}