import 'plugpag_flutter_platform_interface.dart';

class PlugpagFlutter {
  Future<void> requestPermissions() async {
    await PlugpagFlutterPlatform.instance.requestPermissions();
  }
}
