# Build/Test Report – T01 BasicToDoApp

## Environment observed

- OS: macOS 13.4.1 arm64
- Java used for Gradle: Android Studio bundled JDK 11 at `/Applications/Android Studio.app/Contents/jre/Contents/Home`
- Gradle executable: local Gradle 7.4 distribution via project `./gradlew` helper
- Android SDK path configured: `/Volumes/DATA/AndroidStudioSetup/sdk`
- Local SDK config written to non-committed `local.properties`.

## Commands run and results

### Tool/version check

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jre/Contents/Home"
./gradlew --version
```

Result: Gradle 7.4 starts successfully with JDK 11.

### Local unit tests

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jre/Contents/Home"
./gradlew --no-daemon :app:testDebugUnitTest --stacktrace
```

Result: **PASS**

Gradle summary:

```
> Task :app:testDebugUnitTest
BUILD SUCCESSFUL in 24s
25 actionable tasks: 10 executed, 15 up-to-date
```

### Debug build

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jre/Contents/Home"
./gradlew --no-daemon :app:assembleDebug --stacktrace
```

Result: **PASS**

Gradle summary:

```
> Task :app:assembleDebug
BUILD SUCCESSFUL in 19s
34 actionable tasks: 16 executed, 18 up-to-date
```

### APK artifact

```
/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk
```

Observed file size: 6,473,345 bytes.

### No network/account dependency check

```bash
grep -R -n -E "android.permission.INTERNET|android.permission.ACCESS_NETWORK_STATE|login|account" app/src/main app/build.gradle.kts build.gradle.kts
```

Result: no matches.

## Notes

- Earlier blocker from missing SDK was resolved after user provided `/Volumes/DATA/AndroidStudioSetup/sdk`.
- Earlier build issue from missing `android.useAndroidX=true` was fixed by adding `gradle.properties`.
- Earlier Material 3 experimental API compile errors were fixed by adding `@OptIn(ExperimentalMaterial3Api::class)` around placeholder composables that use `Scaffold`/`FilterChip` in Material3 1.0.1.
- Device/emulator launch smoke test was not executed in this run; QA should install the debug APK on API 26+ and verify launch screen.

## Emulator configuration and launch smoke test

SDK/emulator environment script added:

```bash
/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/scripts/android-env.sh
```

Observed running emulator:

- Device id: `emulator-5554`
- Android version: 10
- API level: 29
- Model: Android SDK built for arm64

Commands run:

```bash
source scripts/android-env.sh
adb devices
adb -s emulator-5554 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s emulator-5554 shell am start -W -n com.example.basictodoapp/.MainActivity
adb -s emulator-5554 shell dumpsys window | grep -E 'mCurrentFocus|mFocusedApp'
```

Results:

- Install result: `Success`
- Launch result: `Status: ok`
- Foreground activity confirmed: `com.example.basictodoapp/com.example.basictodoapp.MainActivity`
- Screenshot captured: `/Users/lethimythao/.openclaw/workspace-shared/implementation/basictodoapp-t01-emulator.png`
- Visual smoke verification: placeholder screen shows `BasicToDoApp`, task input, add button, sort placeholders, and empty state.

Note: `avdmanager` from the legacy SDK tools fails under the current Java runtime due missing JAXB classes, but this did not block using the already-configured/running emulator via `emulator`/`adb`.
