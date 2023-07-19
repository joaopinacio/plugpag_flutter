# plugpag_flutter
[![pub package](https://img.shields.io/badge/pub-0.0.3-orange)](https://pub.dev/packages/plugpag_flutter)
[![stars](https://img.shields.io/github/stars/joaopinacio/plugpag_flutter?color=deeppink)](https://github.com/joaopinacio/plugpag_flutter)
[![build](https://img.shields.io/github/actions/workflow/status/joaopinacio/plugpag_flutter/publish.yml)](https://github.com/joaopinacio/plugpag_flutter/actions?query=Publish%20to%20pub.dev)
[![licence](https://img.shields.io/github/license/brim-borium/spotify_sdk?color=blue)](https://github.com/joaopinacio/plugpag_flutter/blob/main/LICENSE)

![Screenshot](https://assets.pagseguro.com.br/ps-bootstrap/v7.3.1/svg/pagbank/logo-pagbank.svg)

## Installation

To use this plugin, add `plugpag_flutter` as a [dependency](https://flutter.io/using-packages/) in your `pubspec.yaml` file like this

```yaml
dependencies:
  plugpag_flutter:
```
This will get you the latest version.

## Setup

### Android

#### AndroidManifest.xml

```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="YOUR_PACKAGE_NAME"
    // Add this line below
    xmlns:tools="http://schemas.android.com/tools">

    ...
</manifest>
```

```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="YOUR_PACKAGE_NAME"
    xmlns:tools="http://schemas.android.com/tools">
    <application
        android:label="YOUR_ANDROID_LABEL"
        // Add this line below (after android:label)
        tools:replace="android:label"
        android:name="${applicationName}"
        android:icon="@mipmap/ic_launcher">

        ...
    </application>
</manifest>
```

#### app/build.gradle

```
defaultConfig {
    ...
    minSdkVersion 21
    ...    
}

dependencies {
    ...
    
    implementation 'android.br.com.uol.pagseguro:plugpag:4.9.4'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.4.0'
}
```

### IOS

🚧 Under development 🚧

## Usage

To start using this package first import it in your Dart file.

```dart
import 'package:plugpag_flutter/plugpag_flutter.dart';
```

Initialize

```dart
var plugpag = PlugpagFlutter(onState: onState);
```

Basic example using it:

```dart
var plugpag = PlugpagFlutter(onState: (state) {});

await plugpag.requestPermissions();
await plugpag.requestAuthentication();
await plugpag.startTerminalDebitPayment(2.00);
```

## Official Plugpag Docs

- [Docs](https://dev.pagbank.uol.com.br/v1/reference/introducao)
- [Android](https://dev.pagbank.uol.com.br/v1/reference/plugpag-android-introducao)
- [IOS](https://dev.pagbank.uol.com.br/v1/reference/plugpag-ios-introducao)