package dev.joaop.plugpag_flutter.task

import br.com.uol.pagseguro.plugpag.PlugPag
import br.com.uol.pagseguro.plugpag.PlugPagDevice
import br.com.uol.pagseguro.plugpag.PlugPagPaymentData
import br.com.uol.pagseguro.plugpag.PlugPagTransactionResult
import dev.joaop.plugpag_flutter.background.CoroutinesAsyncTask
import dev.joaop.plugpag_flutter.PlugPagManager
import dev.joaop.plugpag_flutter.TaskHandler
import dev.joaop.plugpag_flutter.helper.Bluetooth

class TerminalPaymentTask(handler: TaskHandler, taskName: String) : CoroutinesAsyncTask<PlugPagPaymentData?, String?, PlugPagTransactionResult?>(taskName) {
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

    // -----------------------------------------------------------------------------------------------------------------
    // Task execution
    // -----------------------------------------------------------------------------------------------------------------
    override fun onPreExecute() {
        super.onPreExecute()
        mHandler!!.onTaskStart()
    }

    override fun doInBackground(vararg params: PlugPagPaymentData?): PlugPagTransactionResult? {
        var result: PlugPagTransactionResult? = null
        val plugpag: PlugPag?
        if (params != null && params.isNotEmpty() && params[0] != null) {
            // Update throbber
            mPaymentData = params[0]
            publishProgress("")
            plugpag = PlugPagManager.instance?.plugPag
            try {
                plugpag?.initBTConnection(PlugPagDevice(Bluetooth.terminal!!))
                result = plugpag?.doPayment(params[0]!!)
            } catch (e: Exception) {
                publishProgress(e.message)
            }
            mPaymentData = null
        }
        return result!!
    }

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        if (values != null && values.isNotEmpty()) {
            mHandler!!.onProgressPublished(values[0], mPaymentData)
        }
    }

    override fun onPostExecute(result: PlugPagTransactionResult?) {
        super.onPostExecute(result)
        mHandler!!.onTaskFinished(result)
        mHandler = null
    }
}