package dev.joaop.plugpag_flutter

interface TaskHandler {
    fun onTaskStart()
    fun onProgressPublished(progress: String?, transactionInfo: Any?)
    fun onTaskFinished(result: Any?)
}