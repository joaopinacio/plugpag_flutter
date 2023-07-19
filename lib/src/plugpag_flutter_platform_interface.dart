import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'models/state_plug_pag.dart';
import 'plugpag_flutter_method_channel.dart';

abstract class PlugpagFlutterPlatform extends PlatformInterface {
  /// Constructs a PlugpagFlutterPlatform.
  PlugpagFlutterPlatform() : super(token: _token);

  static final Object _token = Object();

  static PlugpagFlutterPlatform _instance = PlugpagFlutter(onState: (StatePlugPag state) {});

  /// The default instance of [PlugpagFlutterPlatform] to use.
  ///
  /// Defaults to [MethodChannelPlugpagFlutter].
  static PlugpagFlutterPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [PlugpagFlutterPlatform] when
  /// they register themselves.
  static set instance(PlugpagFlutterPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  /// * Permission

  Future<void> requestPermissions() {
    throw UnimplementedError('requestPermissions() has not been implemented.');
  }

  /// * Authentication

  Future<void> requestAuthentication() {
    throw UnimplementedError('requestAuthentication() has not been implemented.');
  }

  Future<bool> checkAuthentication() {
    throw UnimplementedError('checkAuthentication() has not been implemented.');
  }

  Future<void> invalidateAuthentication() {
    throw UnimplementedError('invalidateAuthentication() has not been implemented.');
  }

  /// * Terminal (Pro ou similares)

  Future<void> startTerminalCreditPayment(double amount) {
    throw UnimplementedError('startTerminalCreditPayment() has not been implemented.');
  }

  Future<void> startTerminalCreditWithInstallmentsPayment(double amount, int installments) {
    throw UnimplementedError('startTerminalCreditWithInstallmentsPayment() has not been implemented.');
  }

  Future<void> startTerminalDebitPayment(double amount) {
    throw UnimplementedError('startTerminalDebitPayment() has not been implemented.');
  }

  Future<void> startTerminalVoucherPayment(double amount) {
    throw UnimplementedError('startTerminalVoucherPayment() has not been implemented.');
  }

  Future<void> startTerminalVoidPayment() {
    throw UnimplementedError('startTerminalVoidPayment() has not been implemented.');
  }

  Future<void> startTerminalQueryTransaction(double amount) {
    throw UnimplementedError('startTerminalQueryTransaction() has not been implemented.');
  }

  /// * Pinpad (Minizinha ou similares)

  Future<void> startPinpadCreditPayment(double amount) {
    throw UnimplementedError('startPinpadCreditPayment() has not been implemented.');
  }

  Future<void> startPinpadCreditWithInstallmentsPayment(double amount, int installments) {
    throw UnimplementedError('startPinpadCreditWithInstallmentsPayment() has not been implemented.');
  }

  Future<void> startPinpadDebitPayment(double amount) {
    throw UnimplementedError('startPinpadDebitPayment() has not been implemented.');
  }

  Future<void> startPinpadVoucherPayment(double amount) {
    throw UnimplementedError('startPinpadVoucherPayment() has not been implemented.');
  }

  Future<void> startPinpadVoidPayment() {
    throw UnimplementedError('startPinpadVoidPayment() has not been implemented.');
  }

  /// * Transaction
  Future<void> cancelCurrentTransation(double amount) {
    throw UnimplementedError('cancelCurrentTransation() has not been implemented.');
  }
}
