import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'plugpag_flutter_method_channel.dart';

abstract class PlugpagFlutterPlatform extends PlatformInterface {
  /// Constructs a PlugpagFlutterPlatform.
  PlugpagFlutterPlatform() : super(token: _token);

  static final Object _token = Object();

  static PlugpagFlutterPlatform _instance = MethodChannelPlugpagFlutter();

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

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
