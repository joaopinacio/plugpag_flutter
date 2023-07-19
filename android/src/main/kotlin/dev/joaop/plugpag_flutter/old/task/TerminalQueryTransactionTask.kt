package dev.joaop.plugpag_flutter.old.task

import br.com.uol.pagseguro.plugpag.PlugPag
import br.com.uol.pagseguro.plugpag.PlugPagDevice
import br.com.uol.pagseguro.plugpag.PlugPagTransactionResult
import dev.joaop.plugpag_flutter.old.background.CoroutinesAsyncTask
import dev.joaop.plugpag_flutter.old.PlugPagManager
import dev.joaop.plugpag_flutter.old.TaskHandler
import dev.joaop.plugpag_flutter.old.helper.Bluetooth

class TerminalQueryTransactionTask(handler: TaskHandler, taskName: String) : CoroutinesAsyncTask<Void?, Void?, PlugPagTransactionResult?>(taskName) {
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
        val result: PlugPagTransactionResult?
        val plugpag: PlugPag? = PlugPagManager.instance?.plugPag
        plugpag?.initBTConnection(PlugPagDevice(Bluetooth.terminal!!))
        result = plugpag?.lastApprovedTransaction
        return result
    }

    override fun onPostExecute(result: PlugPagTransactionResult?) {
        super.onPostExecute(result)
        mHandler!!.onTaskFinished(result)
        mHandler = null
    }
}