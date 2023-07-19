import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:plugpag_flutter/plugpag_flutter.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockPlugpagFlutterPlatform with MockPlatformInterfaceMixin {
  // @override
  // Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  final PlugpagFlutter initialPlatform = PlugpagFlutter(onState: (state) {});

  test('$PlugpagFlutter is the default instance', () {
    expect(initialPlatform, isInstanceOf<PlugpagFlutter>());
  });

  // test('getPlatformVersion', () async {
  //   PlugpagFlutter plugpagFlutterPlugin = PlugpagFlutter();
  //   MockPlugpagFlutterPlatform fakePlatform = MockPlugpagFlutterPlatform();
  //   PlugpagFlutterPlatform.instance = fakePlatform;

  //   expect(await plugpagFlutterPlugin.getPlatformVersion(), '42');
  // });
}
