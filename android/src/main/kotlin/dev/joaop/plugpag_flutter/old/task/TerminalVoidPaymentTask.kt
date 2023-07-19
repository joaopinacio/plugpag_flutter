package dev.joaop.plugpag_flutter.old.task

import br.com.uol.pagseguro.plugpag.PlugPag
import br.com.uol.pagseguro.plugpag.PlugPagDevice
import br.com.uol.pagseguro.plugpag.PlugPagTransactionResult
import br.com.uol.pagseguro.plugpag.PlugPagVoidData
import dev.joaop.plugpag_flutter.old.background.CoroutinesAsyncTask
import dev.joaop.plugpag_flutter.old.PlugPagManager
import dev.joaop.plugpag_flutter.old.TaskHandler
import dev.joaop.plugpag_flutter.old.helper.Bluetooth

class TerminalVoidPaymentTask(handler: TaskHandler, taskName: String) : CoroutinesAsyncTask<Void?, String?, PlugPagTransactionResult?>(taskName) { //AsyncTask<Void?, String?, PlugPagTransactionResult?>() {
    // -----------------------------------------------------------------------------------------------------------------
    // Instance attributes
    // -----------------------------------------------------------------------------------------------------------------
    private var mHandler: TaskHandler? = null

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

    override fun doInBackground(vararg params: Void?): PlugPagTransactionResult? {
        var result: PlugPagTransactionResult? = null
        val plugpag: PlugPag?
        try {
            // Just update the Throbber
            publishProgress("")
            plugpag = PlugPagManager.instance?.plugPag
            plugpag?.initBTConnection(PlugPagDevice(Bluetooth.terminal!!))
            result = plugpag?.voidPayment()
        } catch (e: Exception) {
            publishProgress(e.message)
        }
        return result!!
    }

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        if (values != null && values.isNotEmpty() && values[0] != null) {
            mHandler!!.onProgressPublished(
                    values[0],
                    PlugPagVoidData(".", "."))
        }
    }

    override fun onPostExecute(result: PlugPagTransactionResult?) {
        super.onPostExecute(result)
        mHandler!!.onTaskFinished(result)
        mHandler = null
    }
}