# QA Test Guide – Step 4 Delete Only

## Scope

Verify Add/Edit still work and Delete now works. Do not test Sort as implemented behavior yet.

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
2. Add a task using the existing Add flow.
3. Optionally edit it using the existing Edit flow to confirm prior behavior remains working.
4. Tap `Delete` on the task row.
   - Expected: confirmation dialog opens with `Delete task?` and `This task will be removed.`.
5. Tap `Cancel` or dismiss the dialog.
   - Expected: task remains visible unchanged.
6. Tap `Delete` again, then confirm `Delete`.
   - Expected: selected task is removed from the list.
7. Delete the last remaining task.
   - Expected: empty state appears: `No tasks yet` / `Add your first task to get started.`.
8. Close/reopen app.
   - Expected: deleted task does not reappear.

## Out of scope this step

- Changing sort order.
