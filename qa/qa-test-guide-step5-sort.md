# QA Test Guide – Step 5 Sort Only

## Scope

Verify Add/Edit/Delete still work and Sort now works.

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
2. Add two or more tasks with a small time gap between adds.
3. Open the top-bar sort control.
4. Select `Newest first`.
   - Expected: newest created task appears first.
5. Open the sort control again and select `Oldest first`.
   - Expected: oldest created task appears first.
6. Confirm Add still works after changing sort.
7. Confirm Edit still updates a task title without changing the sort options.
8. Confirm Delete still removes the selected task and list remains sorted by the selected order.
9. Close/reopen app.
   - Expected: tasks persist; selected sort preference does not need to persist and may reset to `Newest first`.
