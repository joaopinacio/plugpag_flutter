import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:plugpag_flutter/src/models/method_names.dart';
import 'package:plugpag_flutter/src/models/method_channels.dart';

import 'enums/plugpag_type_enum.dart';
import 'models/state_plug_pag.dart';
import 'plugpag_flutter_platform_interface.dart';

///
/// [PlugpagFlutter] holds the functionality to integrate applications,
/// via bluetooth, with PagSeguro readers, you can use the functionality
/// described [here](https://pub.dev/packages/plugpag_flutter#usage)
///
class PlugpagFlutter extends PlugpagFlutterPlatform {
  /// onState
  final Function(StatePlugPag state) onState;

  /// method channel
  @visibleForTesting
  final methodChannel = const MethodChannel(MethodChannels.plugpag);

  PlugpagFlutter({required this.onState}) {
    methodChannel.setMethodCallHandler((a) => handler(a));
  }

  /// Handle the messages
  handler(MethodCall call) {
    if (call.method == "showMessage") {
      onState(StatePlugPag(PlugPagTypeEnum.message, call.arguments.toString()));
    } else if (call.method == "showErrorMessage") {
      onState(StatePlugPag(PlugPagTypeEnum.error, call.arguments.toString()));
    } else {
      onState(StatePlugPag(PlugPagTypeEnum.loading, call.arguments.toString()));
    }
  }

  /// Helper to work in [Android]
  int convertDouble(double amount) => (amount * 100).toInt();

  /// Request permissions to use all functionalities
  /// Throws a [PlatformException] if connecting to the remote api failed
  /// Throws a [MissingPluginException] if the method is not implemented on
  /// the native platforms.
  @override
  Future<void> requestPermissions() async {
    try {
      await methodChannel.invokeMethod<void>(MethodNames.requestPermissions);
    } on Exception {
      rethrow;
    }
  }

  /// Request Authentication to log in to your Pagseguro Account
  /// Throws a [PlatformException] if connecting to the remote api failed
  /// Throws a [MissingPluginException] if the method is not implemented on
  /// the native platforms.
  @override
  Future<void> requestAuthentication() async {
    try {
      await methodChannel.invokeMethod<void>(MethodNames.requestAuthentication);
    } on Exception {
      rethrow;
    }
  }

  /// Check Authentication
  /// returning a [bool] if it is Authenticated or not
  /// Throws a [PlatformException] if connecting to the remote api failed
  /// Throws a [MissingPluginException] if the method is not implemented on
  /// the native platforms.
  @override
  Future<bool> checkAuthentication() async {
    try {
      return await methodChannel.invokeMethod<bool>(MethodNames.checkAuthentication) ?? false;
    } on Exception {
      rethrow;
    }
  }

  /// Invalidate Authentication to log out of your Pagseguro Account
  /// Throws a [PlatformException] if connecting to the remote api failed
  /// Throws a [MissingPluginException] if the method is not implemented on
  /// the native platforms.
  @override
  Future<void> invalidateAuthentication() async {
    try {
      await methodChannel.invokeMethod<void>(MethodNames.invalidateAuthentication);
    } on Exception {
      rethrow;
    }
  }

  // ...

  /// Start a Debit Payment with Pinpad (Minizinha ou similares)
  /// Required parameters are the [amount] to send the amount to pay
  /// Throws a [PlatformException] if connecting to the remote api failed
  /// Throws a [MissingPluginException] if the method is not implemented on
  /// the native platforms.
  @override
  Future<void> startPinpadDebitPayment(double amount) async {
    try {
      await methodChannel.invokeMethod<void>(MethodNames.startPinpadDebitPayment, convertDouble(amount));
    } on Exception {
      rethrow;
    }
  }

  /// Start a Debit Payment with Terminal (Pro ou similares)
  /// Required parameters are the [amount] to send the amount to pay
  /// Throws a [PlatformException] if connecting to the remote api failed
  /// Throws a [MissingPluginException] if the method is not implemented on
  /// the native platforms.
  @override
  Future<void> startTerminalDebitPayment(double amount) async {
    try {
      await methodChannel.invokeMethod<void>(MethodNames.startTerminalDebitPayment, convertDouble(amount));
    } on Exception {
      rethrow;
    }
  }
}
