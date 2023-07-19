package dev.joaop.plugpag_flutter.old.task

import br.com.uol.pagseguro.plugpag.PlugPag
import br.com.uol.pagseguro.plugpag.PlugPagDevice
import br.com.uol.pagseguro.plugpag.PlugPagEventData
import br.com.uol.pagseguro.plugpag.PlugPagEventListener
import br.com.uol.pagseguro.plugpag.PlugPagTransactionResult
import br.com.uol.pagseguro.plugpag.PlugPagVoidData
import dev.joaop.plugpag_flutter.old.background.CoroutinesAsyncTask
import dev.joaop.plugpag_flutter.old.PlugPagManager
import dev.joaop.plugpag_flutter.old.TaskHandler
import dev.joaop.plugpag_flutter.old.helper.Bluetooth

class PinpadVoidPaymentTask(handler: TaskHandler, taskName: String) : CoroutinesAsyncTask<PlugPagVoidData?, String?, PlugPagTransactionResult?>(taskName), PlugPagEventListener {
    // -----------------------------------------------------------------------------------------------------------------
    // Instance attributes
    // -----------------------------------------------------------------------------------------------------------------
    private var mHandler: TaskHandler? = null
    private var mVoidPaymentData: PlugPagVoidData? = null
    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Creates a new void payment task.
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

    override fun doInBackground(vararg params: PlugPagVoidData?): PlugPagTransactionResult? {
        var result: PlugPagTransactionResult? = null
        val plugpag: PlugPag?
        if (params != null && params.isNotEmpty() && params[0] != null) {
            mVoidPaymentData = params[0]
            plugpag = PlugPagManager.instance?.plugPag
            plugpag?.setEventListener(this)
            try {
                // Update the throbber
                publishProgress("")

                // Perform void payment
                plugpag?.initBTConnection(PlugPagDevice(Bluetooth.pinpad!!))
                result = plugpag?.voidPayment(params[0])
            } catch (e: Exception) {
                publishProgress(e.message)
            } finally {
                plugpag?.setEventListener(null)
            }
            mVoidPaymentData = null
        }
        return result!!
    }

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        if (values != null && values.isNotEmpty() && values[0] != null) {
            mHandler!!.onProgressPublished(values[0], mVoidPaymentData)
        }
    }

    override fun onPostExecute(result: PlugPagTransactionResult?) {
        super.onPostExecute(result)
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