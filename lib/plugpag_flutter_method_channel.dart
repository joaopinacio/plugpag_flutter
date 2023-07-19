import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'plugpag_flutter_platform_interface.dart';

enum Type { message, error, loading }

class StatesPlugPag {
  final Type type;
  final String message;
  StatesPlugPag(this.type, this.message);
}

class MethodChannelPlugpagFlutter extends PlugpagFlutterPlatform {
  final Function(StatesPlugPag state) onState;

  @visibleForTesting
  final methodChannel = const MethodChannel('plugpag_flutter');

  MethodChannelPlugpagFlutter({required this.onState}) {
    methodChannel.setMethodCallHandler((a) => handler(a));
  }

  handler(MethodCall call) {
    if (call.method == "showMessage") {
      onState(StatesPlugPag(Type.message, call.arguments.toString()));
    } else if (call.method == "showErrorMessage") {
      onState(StatesPlugPag(Type.error, call.arguments.toString()));
    } else {
      onState(StatesPlugPag(Type.loading, call.arguments.toString()));
    }
  }

  int convertDouble(double amount) => (amount * 100).toInt();

  @override
  Future<void> requestPermissions() async {
    try {
      await methodChannel.invokeMethod<void>('requestPermissions');
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<void> requestAuthentication() async {
    try {
      await methodChannel.invokeMethod<void>('requestAuthentication');
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<bool> checkAuthentication() async {
    try {
      return await methodChannel.invokeMethod<bool>('checkAuthentication') ?? false;
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<void> invalidateAuthentication() async {
    try {
      await methodChannel.invokeMethod<void>('invalidateAuthentication');
    } catch (e) {
      rethrow;
    }
  }

  // ...

  @override
  Future<void> startPinpadDebitPayment(double amount) async {
    try {
      await methodChannel.invokeMethod<void>('startPinpadDebitPayment', convertDouble(amount));
    } catch (e) {
      rethrow;
    }
  }

  @override
  Future<void> startTerminalDebitPayment(double amount) async {
    try {
      await methodChannel.invokeMethod<void>('startTerminalDebitPayment', convertDouble(amount));
    } catch (e) {
      rethrow;
    }
  }
}
