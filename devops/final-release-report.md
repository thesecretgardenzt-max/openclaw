# Final DevOps Release Report - BasicToDoApp

## Metadata

- artifact_type: devops_release_report
- schema_version: 1.0.0
- generated_at: 2026-09-28T21:01:01.595Z
- status: FINAL_READY_FOR_DEMO_REVIEW
- app_path: `/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp`
- target_artifact: `/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk`
- APK SHA-256: `44eda513e612036b18e579770c67d16799cad7e6a7c00f493849cad568111cfd`
- APK size/evidence: `-rw-------  1 lethimythao  staff   6.2M Sep 29 01:10 /Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk`
- QA bug list: `/Users/lethimythao/.openclaw/workspace-shared/qa/bug-list.md`
- QA report: `/Users/lethimythao/.openclaw/workspace-shared/qa/test-report.md`

## Final Status

**FINAL_READY_FOR_DEMO_REVIEW**

Product/user decision accepted BUG-001 and BUG-002 for the current debug demo/review build. No app code changes were made by DevOps.

## Release Readiness Gates

| Gate | Result | Evidence |
|---|---:|---|
| Unit tests | PASS | `./gradlew --no-daemon :app:testDebugUnitTest --stacktrace` |
| Debug build | PASS | `./gradlew --no-daemon :app:assembleDebug --stacktrace` |
| APK exists + checksum | PASS | `/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk`, SHA-256 `44eda513e612036b18e579770c67d16799cad7e6a7c00f493849cad568111cfd` |
| ADB/emulator available | PASS | `adb devices` output captured |
| BUG-001 accepted | PASS | Expected `ACCEPTED_FOR_DEMO` in QA/devops docs |
| BUG-002 accepted | PASS | Expected `ACCEPTED_FOR_DEMO` in QA/devops docs |
| Production deployment | NOT_RUN | Debug demo/review verification only |

## Exact Commands Run

```bash
cd /Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp
if [ -f scripts/android-env.sh ]; then . scripts/android-env.sh; fi
./gradlew --no-daemon :app:testDebugUnitTest --stacktrace
./gradlew --no-daemon :app:assembleDebug --stacktrace
ls -lh /Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk
stat -f 'bytes=%z mtime=%Sm' /Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk
shasum -a 256 /Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk
adb devices
```

## Evidence

### Environment

```text
/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp
android-env=present
ANDROID_HOME=/Volumes/DATA/AndroidStudioSetup/sdk
ANDROID_SDK_ROOT=/Volumes/DATA/AndroidStudioSetup/sdk

------------------------------------------------------------
Gradle 7.4
------------------------------------------------------------

Build time:   2022-02-08 09:58:38 UTC
Revision:     f0d9291c04b90b59445041eaa75b2ee744162586

Kotlin:       1.5.31
Groovy:       3.0.9
Ant:          Apache Ant(TM) version 1.10.11 compiled on July 10 2021
JVM:          18.0.2 (Oracle Corporation 18.0.2+9-61)
```

### Unit tests

```text
To honour the JVM settings for this build a single-use Daemon process will be forked. See https://docs.gradle.org/7.4/userguide/gradle_daemon.html#sec:disabling_the_daemon.
Daemon will be stopped at the end of the build
> Task :app:preBuild UP-TO-DATE
> Task :app:preDebugBuild UP-TO-DATE
> Task :app:compileDebugAidl NO-SOURCE
> Task :app:compileDebugRenderscript NO-SOURCE
> Task :app:generateDebugBuildConfig UP-TO-DATE
> Task :app:checkDebugAarMetadata UP-TO-DATE
> Task :app:generateDebugResValues UP-TO-DATE
> Task :app:mapDebugSourceSetPaths UP-TO-DATE
> Task :app:generateDebugResources UP-TO-DATE
> Task :app:mergeDebugResources UP-TO-DATE
> Task :app:packageDebugResources UP-TO-DATE
> Task :app:parseDebugLocalResources UP-TO-DATE
> Task :app:createDebugCompatibleScreenManifests UP-TO-DATE
> Task :app:extractDeepLinksDebug UP-TO-DATE
> Task :app:processDebugMainManifest UP-TO-DATE
> Task :app:processDebugManifest UP-TO-DATE
> Task :app:processDebugManifestForPackage UP-TO-DATE
> Task :app:processDebugResources UP-TO-DATE
> Task :app:kaptGenerateStubsDebugKotlin UP-TO-DATE
> Task :app:kaptDebugKotlin UP-TO-DATE
> Task :app:compileDebugKotlin UP-TO-DATE
> Task :app:javaPreCompileDebug UP-TO-DATE
> Task :app:compileDebugJavaWithJavac UP-TO-DATE
> Task :app:bundleDebugClassesToRuntimeJar UP-TO-DATE
> Task :app:bundleDebugClassesToCompileJar UP-TO-DATE
> Task :app:kaptGenerateStubsDebugUnitTestKotlin UP-TO-DATE
> Task :app:kaptDebugUnitTestKotlin UP-TO-DATE
> Task :app:compileDebugUnitTestKotlin UP-TO-DATE
> Task :app:preDebugUnitTestBuild UP-TO-DATE
> Task :app:javaPreCompileDebugUnitTest UP-TO-DATE
> Task :app:compileDebugUnitTestJavaWithJavac NO-SOURCE
> Task :app:processDebugJavaRes NO-SOURCE
> Task :app:processDebugUnitTestJavaRes NO-SOURCE
> Task :app:testDebugUnitTest UP-TO-DATE

BUILD SUCCESSFUL in 5s
25 actionable tasks: 25 up-to-date

```

### Debug build

```text
To honour the JVM settings for this build a single-use Daemon process will be forked. See https://docs.gradle.org/7.4/userguide/gradle_daemon.html#sec:disabling_the_daemon.
Daemon will be stopped at the end of the build
> Task :app:preBuild UP-TO-DATE
> Task :app:preDebugBuild UP-TO-DATE
> Task :app:mergeDebugNativeDebugMetadata NO-SOURCE
> Task :app:compileDebugAidl NO-SOURCE
> Task :app:compileDebugRenderscript NO-SOURCE
> Task :app:generateDebugBuildConfig UP-TO-DATE
> Task :app:checkDebugAarMetadata UP-TO-DATE
> Task :app:generateDebugResValues UP-TO-DATE
> Task :app:mapDebugSourceSetPaths UP-TO-DATE
> Task :app:generateDebugResources UP-TO-DATE
> Task :app:mergeDebugResources UP-TO-DATE
> Task :app:packageDebugResources UP-TO-DATE
> Task :app:parseDebugLocalResources UP-TO-DATE
> Task :app:createDebugCompatibleScreenManifests UP-TO-DATE
> Task :app:extractDeepLinksDebug UP-TO-DATE
> Task :app:processDebugMainManifest UP-TO-DATE
> Task :app:processDebugManifest UP-TO-DATE
> Task :app:processDebugManifestForPackage UP-TO-DATE
> Task :app:processDebugResources UP-TO-DATE
> Task :app:kaptGenerateStubsDebugKotlin UP-TO-DATE
> Task :app:kaptDebugKotlin UP-TO-DATE
> Task :app:compileDebugKotlin UP-TO-DATE
> Task :app:javaPreCompileDebug UP-TO-DATE
> Task :app:compileDebugJavaWithJavac UP-TO-DATE
> Task :app:mergeDebugShaders UP-TO-DATE
> Task :app:compileDebugShaders NO-SOURCE
> Task :app:generateDebugAssets UP-TO-DATE
> Task :app:mergeDebugAssets UP-TO-DATE
> Task :app:compressDebugAssets UP-TO-DATE
> Task :app:processDebugJavaRes NO-SOURCE
> Task :app:mergeDebugJavaResource UP-TO-DATE
> Task :app:checkDebugDuplicateClasses UP-TO-DATE
> Task :app:desugarDebugFileDependencies UP-TO-DATE
> Task :app:mergeExtDexDebug UP-TO-DATE
> Task :app:mergeLibDexDebug UP-TO-DATE
> Task :app:dexBuilderDebug UP-TO-DATE
> Task :app:mergeProjectDexDebug UP-TO-DATE
> Task :app:mergeDebugJniLibFolders UP-TO-DATE
> Task :app:mergeDebugNativeLibs NO-SOURCE
> Task :app:stripDebugDebugSymbols NO-SOURCE
> Task :app:validateSigningDebug UP-TO-DATE
> Task :app:writeDebugAppMetadata UP-TO-DATE
> Task :app:writeDebugSigningConfigVersions UP-TO-DATE
> Task :app:packageDebug UP-TO-DATE
> Task :app:createDebugApkListingFileRedirect UP-TO-DATE
> Task :app:assembleDebug UP-TO-DATE

BUILD SUCCESSFUL in 5s
34 actionable tasks: 34 up-to-date

```

### APK and ADB

```text
APK=/Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk
-rw-------  1 lethimythao  staff   6.2M Sep 29 01:10 /Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk
bytes=6545203 mtime=Sep 29 01:10:37 2026
44eda513e612036b18e579770c67d16799cad7e6a7c00f493849cad568111cfd  /Users/lethimythao/.openclaw/workspace-shared/app/BasicToDoApp/app/build/outputs/apk/debug/app-debug.apk
--- adb devices ---
List of devices attached
emulator-5554	device
```

## QA / Bug Acceptance Review

- BUG-001 accepted for demo: yes
- BUG-002 accepted for demo: yes

Relevant parsed evidence from bug list / QA docs:

```text
BUG-001 | Task action controls do not meet icon/content-description design spec | Medium | ACCEPTED_FOR_DEMO |
| BUG-002 | Sort menu does not visually indicate selected option | Low | ACCEPTED_FOR_DEMO |

Product decision:

- Decision date: `2026-09-29 AEST`
- Decision: BUG-001 and BUG-002 are accepted for the current debug demo/review build.
- Follow-up: defer both issues to a future UI/accessibility polish pass before production sign-off.

## BUG-001 - Task action controls do not meet icon/content-description design spec

- Severity: Medium
- Status: ACCEPTED_FOR_DEMO
- Product decision: Accepted for current debug demo/review build; defer to future UI/accessibility polish before production.
- Area: Android UI / accessibility / design consistency
- Related AC: AC-017, UI Specifications: FAB, task list item actions, sort control

Steps to reproduce:

1. Install and launch the APK on `emulator-5554`.
2. Observe the main screen empty state and populated list.
3. Add two tasks so rows are visible.
4. Inspect UI hierarchy dumps:
   - `/Users/lethimythao/.openclaw/workspace-shared/qa/evidence/final-qa-initial-window.xml`
   - `/Users/lethimythao/.openclaw/workspace-shared/qa/evidence/final-qa-two-tasks-newest.xml`

Expected:

- FAB should use an add icon with content description `Add task`.
- Task row actions should be icon buttons with 48 x 48 dp target and content descriptions `Edit task` and `Delete task`.
- Sort should be an icon action in the top app bar.

Actual:

- FAB exposes visible text `+` and no content description in the UI dump.
- Row actions expose visible text `Edit` and `Delete`; no `content-desc` for `Edit task` / `Delete task`.
- Sort is a text button labelled `Newest first` / `Oldest first`, not an icon action.

Evidence:

- `final-qa-initial-window.xml`: FAB node has text `+`, `content-desc=""`.
- `final-qa-two-tasks-newest.xml`: task action
```

## Blockers / Limitations

- No DevOps blocker for debug demo/review readiness.
- Limitation: this is still a debug APK, not a production-signed release.

## Rollback Procedure

- No production deployment was performed.
- If demo APK distribution must be rolled back, stop using this APK and revert to the previous validated APK artifact.
- On emulator/device, uninstall this build or install the prior validated APK and rerun smoke checks.
