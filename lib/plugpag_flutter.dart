
import 'plugpag_flutter_platform_interface.dart';

class PlugpagFlutter {
  Future<String?> getPlatformVersion() {
    return PlugpagFlutterPlatform.instance.getPlatformVersion();
  }
}
