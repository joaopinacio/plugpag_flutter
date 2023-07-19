import 'package:flutter_test/flutter_test.dart';
import 'package:plugpag_flutter/plugpag_flutter_platform_interface.dart';
import 'package:plugpag_flutter/plugpag_flutter_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockPlugpagFlutterPlatform with MockPlatformInterfaceMixin implements PlugpagFlutterPlatform {
  // @override
  // Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<void> requestPermissions() {
    throw UnimplementedError();
  }

  @override
  Future<void> cancelCurrentTransation(double amount) {
    // TODO: implement cancelCurrentTransation
    throw UnimplementedError();
  }

  @override
  Future<bool> checkAuthentication() {
    // TODO: implement checkAuthentication
    throw UnimplementedError();
  }

  @override
  Future<void> invalidateAuthentication() {
    // TODO: implement invalidateAuthentication
    throw UnimplementedError();
  }

  @override
  Future<void> requestAuthentication() {
    // TODO: implement requestAuthentication
    throw UnimplementedError();
  }

  @override
  Future<void> startPinpadCreditPayment(double amount) {
    // TODO: implement startPinpadCreditPayment
    throw UnimplementedError();
  }

  @override
  Future<void> startPinpadCreditWithInstallmentsPayment(double amount, int installments) {
    // TODO: implement startPinpadCreditWithInstallmentsPayment
    throw UnimplementedError();
  }

  @override
  Future<void> startPinpadDebitPayment(double amount) {
    // TODO: implement startPinpadDebitPayment
    throw UnimplementedError();
  }

  @override
  Future<void> startPinpadVoidPayment() {
    // TODO: implement startPinpadVoidPayment
    throw UnimplementedError();
  }

  @override
  Future<void> startPinpadVoucherPayment(double amount) {
    // TODO: implement startPinpadVoucherPayment
    throw UnimplementedError();
  }

  @override
  Future<void> startTerminalCreditPayment(double amount) {
    // TODO: implement startTerminalCreditPayment
    throw UnimplementedError();
  }

  @override
  Future<void> startTerminalCreditWithInstallmentsPayment(double amount, int installments) {
    // TODO: implement startTerminalCreditWithInstallmentsPayment
    throw UnimplementedError();
  }

  @override
  Future<void> startTerminalDebitPayment(double amount) {
    // TODO: implement startTerminalDebitPayment
    throw UnimplementedError();
  }

  @override
  Future<void> startTerminalQueryTransaction(double amount) {
    // TODO: implement startTerminalQueryTransaction
    throw UnimplementedError();
  }

  @override
  Future<void> startTerminalVoidPayment() {
    // TODO: implement startTerminalVoidPayment
    throw UnimplementedError();
  }

  @override
  Future<void> startTerminalVoucherPayment(double amount) {
    // TODO: implement startTerminalVoucherPayment
    throw UnimplementedError();
  }
}

void main() {
  final PlugpagFlutterPlatform initialPlatform = PlugpagFlutterPlatform.instance;

  test('$MethodChannelPlugpagFlutter is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelPlugpagFlutter>());
  });

  // test('getPlatformVersion', () async {
  //   PlugpagFlutter plugpagFlutterPlugin = PlugpagFlutter();
  //   MockPlugpagFlutterPlatform fakePlatform = MockPlugpagFlutterPlatform();
  //   PlugpagFlutterPlatform.instance = fakePlatform;

  //   expect(await plugpagFlutterPlugin.getPlatformVersion(), '42');
  // });
}
