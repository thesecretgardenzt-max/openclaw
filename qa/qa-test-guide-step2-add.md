# QA Test Guide – Step 2 Add Only

## Scope

Verify Add ToDo only. Do not mark Edit/Delete/Sort as failed in this step because they are intentionally out of scope.

## Build

```bash
cd /Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp
printf 'sdk.dir=/Volumes/DATA/AndroidStudioSetup/sdk\n' > local.properties
export JAVA_HOME="/Applications/Android Studio.app/Contents/jre/Contents/Home"
./gradlew --no-daemon :app:testDebugUnitTest
./gradlew --no-daemon :app:assembleDebug
```

## Manual QA Add checks

1. Install and launch `app/build/outputs/apk/debug/app-debug.apk` on API 26+.
2. Empty state should show when no tasks exist.
3. Tap pink `+` FAB.
4. Add dialog opens with `Task title` field.
5. Submit empty or whitespace-only title.
   - Expected: dialog stays open and shows `Task title is required.`.
6. Enter a valid title with leading/trailing spaces, e.g. `  Buy milk  `, and tap Add.
   - Expected: dialog closes.
   - Expected: list shows `Buy milk` without outer spaces.
   - Expected: row shows created-time label.
7. Close and reopen app.
   - Expected: added task remains because Room persistence is wired.

## Out of scope this step

- Edit task.
- Delete task.
- Changing sort order.
