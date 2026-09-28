# QA Test Guide – T01 Project Setup and Baseline Configuration

## Scope for QA

Validate only T01 baseline setup. Do **not** test add/edit/delete/sort business behavior yet; those are later tasks.

## Preconditions

- Android SDK installed/configured. Developer used `/Volumes/DATA/AndroidStudioSetup/sdk` locally.
- Test device/emulator: Android API 26+.
- Project path: `/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp`

## Build verification

```bash
cd /Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp
printf "sdk.dir=/Volumes/DATA/AndroidStudioSetup/sdk\n" > local.properties  # adjust if QA SDK path differs
export JAVA_HOME="/Applications/Android Studio.app/Contents/jre/Contents/Home"
./gradlew --no-daemon :app:testDebugUnitTest
./gradlew --no-daemon :app:assembleDebug
```

Expected:

- Unit test task passes.
- Debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Launch/smoke verification

Install and launch debug APK on API 26+ emulator/device. Developer smoke-tested on `emulator-5554` (Android 10 / API 29).

Expected placeholder screen:

- App launches without login/account/network prompt.
- Screen title displays `BasicToDoApp`.
- Placeholder task input area is visible with label `Task title` and placeholder `What do you need to do?`.
- Add placeholder control is visible.
- Sort placeholder controls are visible: `Newest first`, `Oldest first`.
- Empty state displays: `No tasks yet` and `Add your first task to get started.`

## Out-of-scope for T01 QA

- Adding a task.
- Editing a task.
- Deleting a task.
- Real sort ordering.
- Persistence verification.

These are expected to be implemented in later tasks.

## Emulator helper

From project root, QA can load SDK/ADB paths with:

```bash
source scripts/android-env.sh
adb devices
```

If `emulator-5554` is already running, install and launch with:

```bash
adb -s emulator-5554 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s emulator-5554 shell am start -W -n com.example.basictodoapp/.MainActivity
```
