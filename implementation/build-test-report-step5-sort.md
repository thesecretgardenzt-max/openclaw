# Build/Test Report – Step 5 Sort Only

## Scope

Implemented Sort ToDo only. Add, Edit, and Delete behavior from prior steps are preserved.

## Commands run

```bash
cd /Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp
printf 'sdk.dir=/Volumes/DATA/AndroidStudioSetup/sdk\n' > local.properties
export JAVA_HOME="/Applications/Android Studio.app/Contents/jre/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew --no-daemon :app:testDebugUnitTest --stacktrace
./gradlew --no-daemon :app:assembleDebug --stacktrace
```

## Results

- Unit tests: **PASS** — `BUILD SUCCESSFUL in 23s`.
- Debug build: **PASS** — `BUILD SUCCESSFUL in 11s`.
- APK artifact: `/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk`.
- Observed APK size: `6,545,203` bytes.

## Notes

- Sort supports only `Newest first` and `Oldest first`.
- Sorting is based only on `createdAt`.
- Sort preference is ViewModel/UI state only and is not persisted.
- UI now uses a Material 3 dropdown menu in the top app bar.
- No network/account dependency was added.
