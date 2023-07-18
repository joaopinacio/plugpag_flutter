package dev.joaop.plugpag_flutter.task

import android.text.TextUtils
import br.com.uol.pagseguro.plugpag.PlugPag
import br.com.uol.pagseguro.plugpag.PlugPagDevice
import br.com.uol.pagseguro.plugpag.PlugPagEventData
import br.com.uol.pagseguro.plugpag.PlugPagEventListener
import br.com.uol.pagseguro.plugpag.PlugPagPaymentData
import br.com.uol.pagseguro.plugpag.PlugPagTransactionResult
import dev.joaop.plugpag_flutter.background.CoroutinesAsyncTask
import dev.joaop.plugpag_flutter.PlugPagManager
import dev.joaop.plugpag_flutter.PreviousTransactions
import dev.joaop.plugpag_flutter.TaskHandler
import dev.joaop.plugpag_flutter.helper.Bluetooth

class PinpadPaymentTask(handler: TaskHandler, taskName: String) : CoroutinesAsyncTask<PlugPagPaymentData?, String?, PlugPagTransactionResult?>(taskName), PlugPagEventListener {
    // -----------------------------------------------------------------------------------------------------------------
    // Instance attributes
    // -----------------------------------------------------------------------------------------------------------------
    private var mHandler: TaskHandler? = null
    private var mPaymentData: PlugPagPaymentData? = null

    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Creates a new terminal payment task.
     *
     * @param handler Handler used to report updates.
     */
    init {
        if (handler == null) {
            throw RuntimeException("TaskHandler reference cannot be null")
        }
        mHandler = handler
    }

    override fun doInBackground(vararg params: PlugPagPaymentData?): PlugPagTransactionResult? {
        var result: PlugPagTransactionResult? = null
        val plugpag: PlugPag?
        if (params != null && params.isNotEmpty() && params[0] != null) {
            plugpag = PlugPagManager.instance?.plugPag
            plugpag?.setEventListener(this)
            mPaymentData = params[0]
            try {
                // Update the throbber
                publishProgress("")

                // Perform payment
                plugpag?.initBTConnection(PlugPagDevice(Bluetooth.pinpad!!))
                result = plugpag?.doPayment(params[0]!!)
            } catch (e: Exception) {
                publishProgress(e.message)
            } finally {
                plugpag?.setEventListener(null)
            }
            mPaymentData = null
        }
        return result!!
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Task execution
    // -----------------------------------------------------------------------------------------------------------------
    override fun onPreExecute() {
        super.onPreExecute()
        mHandler!!.onTaskStart()
    }

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        if (values != null && values.isNotEmpty() && values[0] != null) {
            mHandler!!.onProgressPublished(values[0], mPaymentData)
        }
    }

    override fun onPostExecute(result: PlugPagTransactionResult?) {
        super.onPostExecute(result)
        if (result != null &&
                !TextUtils.isEmpty(result.transactionCode) &&
                !TextUtils.isEmpty(result.transactionId)) {
            PreviousTransactions.push(arrayOf(
                    result.transactionCode,
                    result.transactionId
            ))
        }
        mHandler!!.onTaskFinished(result)
        mHandler = null
    }

    // -----------------------------------------------------------------------------------------------------------------
    // PlugPag event handling
    // -----------------------------------------------------------------------------------------------------------------
    override fun onEvent(plugPagEventData: PlugPagEventData): Int {
        publishProgress(PlugPagEventData.getDefaultMessage(plugPagEventData.eventCode))
        return PlugPag.RET_OK
    }
}