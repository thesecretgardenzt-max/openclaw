# QA Test Guide – Step 3 Edit Only

## Scope

Verify Add still works and Edit now works. Do not test Delete/Sort as implemented behavior yet.

## Build

```bash
cd /Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp
printf 'sdk.dir=/Volumes/DATA/AndroidStudioSetup/sdk\n' > local.properties
export JAVA_HOME="/Applications/Android Studio.app/Contents/jre/Contents/Home"
./gradlew --no-daemon :app:testDebugUnitTest
./gradlew --no-daemon :app:assembleDebug
```

## Manual QA checks

1. Install and launch `app/build/outputs/apk/debug/app-debug.apk` on API 26+.
2. Add a task using the Step 2 Add flow.
3. Tap `Edit` on the created task.
   - Expected: edit dialog opens with current title prefilled.
4. Change title to whitespace and tap `Save`.
   - Expected: dialog remains open and shows `Task title is required.`.
   - Expected: task title in list remains unchanged.
5. Enter a valid title with extra spaces, e.g. `  Updated task  `, and tap `Save`.
   - Expected: dialog closes and list shows `Updated task` trimmed.
6. Reopen Edit, change the field, then tap `Cancel` or dismiss dialog.
   - Expected: task title remains unchanged.
7. Close/reopen app.
   - Expected: edited title persists from Room.

## Out of scope this step

- Delete task.
- Changing sort order.
