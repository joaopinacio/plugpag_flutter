import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'plugpag_flutter_platform_interface.dart';

/// An implementation of [PlugpagFlutterPlatform] that uses method channels.
class MethodChannelPlugpagFlutter extends PlugpagFlutterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('plugpag_flutter');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
