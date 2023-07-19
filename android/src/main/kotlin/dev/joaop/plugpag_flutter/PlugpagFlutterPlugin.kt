package dev.joaop.plugpag_flutter

import android.app.Activity
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.uol.pagseguro.plugpag.PlugPag
import br.com.uol.pagseguro.plugpag.PlugPagAuthenticationListener
import br.com.uol.pagseguro.plugpag.PlugPagPaymentData
import br.com.uol.pagseguro.plugpag.PlugPagTransactionResult
import br.com.uol.pagseguro.plugpag.PlugPagVoidData
import dev.joaop.plugpag_flutter.task.PinpadPaymentTask
import dev.joaop.plugpag_flutter.task.PinpadVoidPaymentTask
import dev.joaop.plugpag_flutter.task.TerminalPaymentTask
import dev.joaop.plugpag_flutter.task.TerminalQueryTransactionTask
import dev.joaop.plugpag_flutter.task.TerminalVoidPaymentTask
import io.flutter.embedding.android.FlutterActivity

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** PlugpagFlutterPlugin */
class PlugpagFlutterPlugin: FlutterPlugin, MethodCallHandler, TaskHandler, PlugPagAuthenticationListener, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private val PERMISSIONS_REQUEST_CODE = 0
  private lateinit var channel : MethodChannel
  private lateinit var activity: Activity

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "plugpag_flutter")
    channel.setMethodCallHandler(this)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    this.activity = binding.activity;
    PlugPagManager.create(activity)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    TODO("Not yet implemented")
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    TODO("Not yet implemented")
  }

  override fun onDetachedFromActivity() {
    TODO("Not yet implemented")
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      // Default (test)
      "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")

      // Permissions
      "requestPermissions" -> this.requestPermissions()

      // Auth
      "requestAuthentication" -> this.requestAuthentication()
      "checkAuthentication" -> this.checkAuthenticationResult(result)
      "invalidateAuthentication" -> this.invalidateAuthentication()

      // Terminal
      "startTerminalCreditPayment" -> this.startTerminalCreditPayment(call.arguments as Int)
      "startTerminalCreditWithInstallmentsPayment" -> this.startTerminalCreditWithInstallmentsPayment((call.arguments as List<*>)[0] as Int,(call.arguments as List<*>)[1] as Int)
      "startTerminalDebitPayment" -> this.startTerminalDebitPayment(call.arguments as Int)
      "startTerminalVoucherPayment" -> this.startTerminalVoucherPayment(call.arguments as Int)
      "startTerminalVoidPayment" -> this.startTerminalVoidPayment()
      "startTerminalQueryTransaction" -> this.startTerminalQueryTransaction()

      // Pinpad
      "startPinpadCreditPayment" -> this.startPinpadCreditPayment(call.arguments as Int)
      "startPinpadCreditWithInstallmentsPayment" -> this.startPinpadCreditWithInstallmentsPayment((call.arguments as List<*>)[0] as Int,(call.arguments as List<*>)[1] as Int)
      "startPinpadDebitPayment" -> this.startPinpadDebitPayment(call.arguments as Int)
      "startPinpadVoucherPayment" -> this.startPinpadVoucherPayment(call.arguments as Int)
      "startPinpadVoidPayment" -> this.startPinpadVoidPayment()

      // Transation
      "cancelCurrentTransation" -> this.cancelCurrentTransation()

      else -> result.notImplemented()
    }
  }

  // -----------------------------------------------------------------------------------------------------------------
  // Request missing permissions
  // -----------------------------------------------------------------------------------------------------------------

  /**
   * Requests permissions on runtime, if any needed permission is not granted.
   */
  private fun requestPermissions() {
    // Log.d("PLUGPAG_LOG", "Requested Permission 1")
    var missingPermissions: Array<String>?

    missingPermissions = this.filterMissingPermissions(this.getManifestPermissions())

    if (missingPermissions != null && missingPermissions.isNotEmpty()) {
      ActivityCompat.requestPermissions(this.activity, missingPermissions, PERMISSIONS_REQUEST_CODE)
    } else {
      this.showMessage("Todas permissões concedidas")
    }
  }

  /**
   * Returns a list of permissions requested on the AndroidManifest.xml file.
   *
   * @return Permissions requested on the AndroidManifest.xml file.
   */
  private fun getManifestPermissions(): Array<String> {
    var permissions: Array<String>? = null
    var info: PackageInfo?
    var pm: PackageManager? = this.activity.packageManager

    try {
      info = pm?.getPackageInfo(this.activity.packageName, PackageManager.GET_PERMISSIONS)
      permissions = info!!.requestedPermissions
    } catch (e: PackageManager.NameNotFoundException) {

    }

    if (permissions == null) {
      permissions = arrayOf()
    }

    return permissions
  }

  /**
   * Filters only the permissions still not granted.
   *
   * @param permissions List of permissions to be checked.
   * @return Permissions not granted.
   */
  private fun filterMissingPermissions(permissions: Array<String>?): Array<String>? {
    var missingPermissions: Array<String>?
    var list: MutableList<String>?

    list = ArrayList()

    if (!permissions.isNullOrEmpty()) {
      for (permission in permissions) {
        var perm = ContextCompat.checkSelfPermission(this.activity.applicationContext, permission)
        // Log.d("PLUGPAG_LOG", "Permission: $permission - $perm")

        if (perm != PackageManager.PERMISSION_GRANTED) {
          list.add(permission)
        }
      }
    }

    missingPermissions = list.toTypedArray()

    return missingPermissions
  }

  // -----------------------------------------------------------------------------------------------------------------
  // Authentication handling
  // -----------------------------------------------------------------------------------------------------------------

  /**
   * Checks if a user is authenticated.
   */
  private fun checkAuthenticationResult(@NonNull result: Result){
    Log.d("PLUGPAG_LOG", "Check Authentication")

    result.success(checkAuthentication())
  }

  private fun checkAuthentication(): Boolean {
    var isAuthenticated = PlugPagManager.instance?.plugPag?.isAuthenticated ?: false
    if (isAuthenticated) {
      this.showMessage("Usuario autenticado")
    } else {
      this.showMessage("Usuario nao autenticado")
    }

    return isAuthenticated
  }

  /**
   * Requests authentication.
   */
  private fun requestAuthentication() {
    PlugPagManager.instance?.plugPag?.requestAuthentication(this)
  }

  /**
   * Invalidates current authentication.
   */
  private fun invalidateAuthentication() {
    PlugPagManager.instance?.plugPag?.invalidateAuthentication()
  }


  /**
   * Invalidates current authentication.
   */
  private fun cancelCurrentTransation() {
    PlugPagManager.instance?.plugPag?.abort();
  }

  // -----------------------------------------------------------------------------------------------------------------
  // Terminal transactions
  // -----------------------------------------------------------------------------------------------------------------

  /**
   * Starts a new credit payment on a terminal.
   */
  private fun startTerminalCreditPayment(amount: Int) {
    if(!checkAuthentication()) return
    var paymentData: PlugPagPaymentData?

    paymentData = PlugPagPaymentData.Builder()
            .setType(PlugPag.TYPE_CREDITO)
            .setInstallmentType(PlugPag.INSTALLMENT_TYPE_A_VISTA)
            .setAmount(amount)
            .setUserReference("CODVENDA")
            .build()

    TerminalPaymentTask(this, "startTerminalCreditPayment").execute(paymentData)
  }

  /**
   * Starts a new credit payment with installments on a terminal
   */
  private fun startTerminalCreditWithInstallmentsPayment(amount: Int, installments: Int) {
    var paymentData: PlugPagPaymentData?

    paymentData = PlugPagPaymentData.Builder()
            .setType(PlugPag.TYPE_CREDITO)
            .setInstallmentType(PlugPag.INSTALLMENT_TYPE_PARC_VENDEDOR)
            .setInstallments(installments)
            .setAmount(amount)
            .setUserReference("CODVENDA")
            .build()
    TerminalPaymentTask(this, "startTerminalCreditWithInstallmentsPayment").execute(paymentData)
  }

  /**
   * Starts a new debit payment on a terminal.
   */
  private fun startTerminalDebitPayment(amount: Int) {
    if(!checkAuthentication()) return

    var paymentData: PlugPagPaymentData?

    paymentData = PlugPagPaymentData.Builder()
            .setType(PlugPag.TYPE_DEBITO)
            .setAmount(amount)
            .setUserReference("CODVENDA")
            .build()
    TerminalPaymentTask(this, "startTerminalDebitPayment").execute(paymentData)
  }

  /**
   * Starts a new voucher payment on a terminal.
   */
  private fun startTerminalVoucherPayment(amount: Int) {
    var paymentData: PlugPagPaymentData?

    paymentData = PlugPagPaymentData.Builder()
            .setType(PlugPag.TYPE_VOUCHER)
            .setAmount(amount)
            .setUserReference("CODVENDA")
            .build()
    TerminalPaymentTask(this, "startTerminalVoucherPayment").execute(paymentData)
  }

  /**
   * Starts a new void payment on a terminal.
   */
  private fun startTerminalVoidPayment() {
    TerminalVoidPaymentTask(this, "startTerminalVoidPayment").execute()
  }

  /**
   * Starts a new transaction query on a terminal.
   */
  private fun startTerminalQueryTransaction() {
    TerminalQueryTransactionTask(this, "startTerminalQueryTransaction").execute()
  }

  // -----------------------------------------------------------------------------------------------------------------
  // Pinpad transactions
  // -----------------------------------------------------------------------------------------------------------------

  /**
   * Starts a new credit payment on a pinpad.
   */
  private fun startPinpadCreditPayment(amount: Int) {
    var paymentData: PlugPagPaymentData?

    paymentData = PlugPagPaymentData.Builder()
            .setType(PlugPag.TYPE_CREDITO)
            .setInstallmentType(PlugPag.INSTALLMENT_TYPE_A_VISTA)
            .setAmount(amount)
            .setUserReference("CODVENDA")
            .build()
    PinpadPaymentTask(this, "startPinpadCreditPayment").execute(paymentData)
  }

  /**
   * Starts a new credit payment with installments on a pinpad.
   */
  private fun startPinpadCreditWithInstallmentsPayment(amount: Int,installments: Int) {
    var paymentData: PlugPagPaymentData?

    paymentData = PlugPagPaymentData.Builder()
            .setType(PlugPag.TYPE_CREDITO)
            .setInstallmentType(PlugPag.INSTALLMENT_TYPE_PARC_VENDEDOR)
            .setInstallments(installments)
            .setAmount(amount)
            .setUserReference("CODVENDA")
            .build()
    PinpadPaymentTask(this, "startPinpadCreditWithInstallmentsPayment").execute(paymentData)
  }

  /**
   * Starts a new debit payment on a pinpad.
   */
  private fun startPinpadDebitPayment(amount: Int) {
    if(!checkAuthentication()) return

    var paymentData: PlugPagPaymentData?

    paymentData = PlugPagPaymentData.Builder()
            .setType(PlugPag.TYPE_DEBITO)
            .setAmount(amount)
            .setUserReference("CODVENDA")
            .build()
    PinpadPaymentTask(this, "startPinpadDebitPayment").execute(paymentData)
  }

  /**
   * Starts a new voucher payment on a pinpad.
   */
  private fun startPinpadVoucherPayment(amount: Int) {
    var paymentData: PlugPagPaymentData?

    paymentData = PlugPagPaymentData.Builder()
            .setType(PlugPag.TYPE_VOUCHER)
            .setAmount(amount)
            .setUserReference("CODVENDA")
            .build()
    PinpadPaymentTask(this, "startPinpadVoucherPayment").execute(paymentData)
  }

  /**
   * Starts a void payment on a pinpad.
   */
  private fun startPinpadVoidPayment() {
    var voidData: PlugPagVoidData?
    var lastTransaction: Array<String?>?

    lastTransaction = PreviousTransactions.pop()

    if (lastTransaction != null) {
      voidData = PlugPagVoidData.Builder()
              .setTransactionCode(lastTransaction[0]!!)
              .setTransactionId(lastTransaction[1]!!)
              .build()
      PinpadVoidPaymentTask(this, "startPinpadVoidPayment").execute(voidData)
    } else {
      this.showErrorMessage("Sem dados para estorno")
    }
  }

  // -----------------------------------------------------------------------------------------------------------------
  // AlertDialog
  // -----------------------------------------------------------------------------------------------------------------

  /**
   * Shows an AlertDialog with a simple message.
   *
   * @param message Message to be displayed.
   */
  private fun showMessage(message: String?) {

    if (TextUtils.isEmpty(message)) {
      this.channel.invokeMethod("showMessage","Erro inesperado")
    } else {
      this.channel.invokeMethod("showMessage",message)
      //  MethodChannel(flutterView, CHANNEL).invokeMethod("showMessage",message)
    }
  }

  /**
   * Shows an AlertDialog with a simple message.
   *
   * @param message Resource ID of the message to be displayed.
   */
  private fun showMessage( message: Int) {
    var msg: String?

    msg = this.activity.getString(message)
    this.showMessage(msg)
  }

  /**
   * Shows an AlertDialog with an error message.
   *
   * @param message Message to be displayed.
   */
  private fun showErrorMessage(message: String) {

    if (TextUtils.isEmpty(message)) {
      this.channel.invokeMethod("showErrorMessage","Erro inesperado")
    } else {
      this.channel.invokeMethod("showErrorMessage","Error")
    }

  }

  /**
   * Shows an AlertDialog with an error message.
   *
   * @param message Resource ID of the message to be displayed.
   */
  private fun showErrorMessage( message: Int) {
    var msg: String?

    msg = this.activity.getString(message)
    this.showErrorMessage(msg)
  }

  /**
   * Shows an AlertDialog with a ProgressBar.
   *
   * @param message Message to be displayed along-side with the ProgressBar.
   */
  private fun showProgressDialog( message: String?) {
    var msg: String?

    if (message == null) {
      msg = "Aguarde"
    } else {
      msg = message
    }
    if (TextUtils.isEmpty(msg)) {
      this.channel.invokeMethod("showProgressDialog",msg)
    } else {
      this.channel.invokeMethod("showProgressDialog",msg)
    }

  }

  /**
   * Shows an AlertDialog with a ProgressBar.
   *
   * @param message Resource ID of the message to be displayed along-side with the ProgressBar.
   */
  private fun showProgressDialog(message: Int) {
    var msg: String?

    msg = this.activity.getString(message)
    this.showProgressDialog(msg)
  }

  // -----------------------------------------------------------------------------------------------------------------
  // Task handling
  // -----------------------------------------------------------------------------------------------------------------

  override fun onTaskStart() {
    this.showProgressDialog("Aguarde")
  }

  override fun onProgressPublished(progress: String?, transactionInfo: Any?) {
    var message: String?
    var type: String? = null

    if (TextUtils.isEmpty(progress)) {
      message = "Aguarde"
    } else {
      message = progress
    }

    if (transactionInfo is PlugPagPaymentData) {
      when (transactionInfo.type) {
        PlugPag.TYPE_CREDITO -> type = "Crédito"

        PlugPag.TYPE_DEBITO -> type = "Débito"

        PlugPag.TYPE_VOUCHER -> type = "Voucher"
      }

      message =   "Tipo: $type \nValor: R$ ${transactionInfo.amount.toDouble() / 100.0}\nParcelamento: ${transactionInfo.amount.toDouble() / 100.0}\n==========\n ${message}"
    } else if (transactionInfo is PlugPagVoidData) {
      message = "Estorno\\n==========\\n${message}"
    }

    this.showProgressDialog(message)
  }

  override fun onTaskFinished(result: Any?) {
    if (result is PlugPagTransactionResult) {
      this.showResult(result)
    } else if (result is String) {
      this.showMessage(result)
    }
  }

  // -----------------------------------------------------------------------------------------------------------------
  // Result display
  // -----------------------------------------------------------------------------------------------------------------

  /**
   * Shows a transaction's result.
   *
   * @param result Result to be displayed.
   */
  private fun showResult(result: PlugPagTransactionResult) {
    var resultDisplay: String?
    var lines: MutableList<String>?

    if (result == null) {
      throw RuntimeException("Transaction result cannot be null")
    }

    lines = ArrayList()
    lines.add("Resultado: ${result.result}")

    if (!TextUtils.isEmpty(result.errorCode)) {
      lines.add("Codigo de error: ${result.errorCode}")
    }

    if (!TextUtils.isEmpty(result.amount)) {
      var value: String?

      value = String.format("%.2f",
              java.lang.Double.parseDouble(result.amount) / 100.0)
      lines.add("Valor: $value")
    }

    if (!TextUtils.isEmpty(result.availableBalance)) {
      var value: String?

      value = String.format("%.2f",
              java.lang.Double.parseDouble(result.amount) / 100.0)
      lines.add("Valor disponivel: $value")
    }

    if (!TextUtils.isEmpty(result.bin)) {
      lines.add("BIN: ${result.bin}")
    }

    if (!TextUtils.isEmpty(result.cardBrand)) {
      lines.add("Bandeira: ${result.cardBrand}")
    }

    if (!TextUtils.isEmpty(result.date)) {
      lines.add("Data:  ${result.date}")
    }

    if (!TextUtils.isEmpty(result.time)) {
      lines.add("Hora: ${result.time}")
    }

    if (!TextUtils.isEmpty(result.holder)) {
      lines.add("Titular:  ${result.holder}")
    }

    if (!TextUtils.isEmpty(result.hostNsu)) {
      lines.add("Host NSU:  ${result.hostNsu} ")
    }

    if (!TextUtils.isEmpty(result.message)) {
      lines.add("Menssagem: ${result.message}")
    }

    if (!TextUtils.isEmpty(result.terminalSerialNumber)) {
      lines.add("Numero de serie: ${result.terminalSerialNumber}")
    }

    if (!TextUtils.isEmpty(result.transactionCode)) {
      lines.add("Código da transação:  ${result.transactionCode}")
    }

    if (!TextUtils.isEmpty(result.transactionId)) {
      lines.add( "ID da transação; ${result.transactionId}")
    }

    if (!TextUtils.isEmpty(result.userReference)) {
      lines.add("Código de venda: ${result.userReference}")
    }

    resultDisplay = TextUtils.join("\n", lines)
    this.showMessage(resultDisplay)
  }

  override fun onSuccess() {
    this.showMessage("Usuario autenticado com sucesso")
  }

  override fun onError() {
    this.showMessage("Usuario nao autenticado")
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
