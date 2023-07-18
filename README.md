# plugpag_flutter

# Setup - Android

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

### app/build.gradle

```
 dependencies {
    ...
    
    implementation 'android.br.com.uol.pagseguro:plugpag:4.9.4'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.4.0'
}
```
