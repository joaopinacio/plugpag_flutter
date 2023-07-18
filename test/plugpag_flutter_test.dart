import 'package:flutter_test/flutter_test.dart';
import 'package:plugpag_flutter/plugpag_flutter.dart';
import 'package:plugpag_flutter/plugpag_flutter_platform_interface.dart';
import 'package:plugpag_flutter/plugpag_flutter_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockPlugpagFlutterPlatform
    with MockPlatformInterfaceMixin
    implements PlugpagFlutterPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final PlugpagFlutterPlatform initialPlatform = PlugpagFlutterPlatform.instance;

  test('$MethodChannelPlugpagFlutter is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelPlugpagFlutter>());
  });

  test('getPlatformVersion', () async {
    PlugpagFlutter plugpagFlutterPlugin = PlugpagFlutter();
    MockPlugpagFlutterPlatform fakePlatform = MockPlugpagFlutterPlatform();
    PlugpagFlutterPlatform.instance = fakePlatform;

    expect(await plugpagFlutterPlugin.getPlatformVersion(), '42');
  });
}
