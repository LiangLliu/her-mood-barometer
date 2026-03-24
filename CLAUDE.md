# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test

```bash
./gradlew assembleDemoDebug              # Dev build
./gradlew assembleProdRelease            # Release build
./gradlew test                           # All unit tests
./gradlew :core:domain:test              # Single module test
./gradlew connectedAndroidTest           # Instrumented tests
./gradlew lint                           # Android lint
```

Build requires **JVM 21** and **4GB+ heap** (configured in gradle.properties).

## Project Structure

- **Architecture**: Clean Architecture (UI → Domain → Data), MVVM + Compose, Hilt DI
- **Modules**: `feature/*` (UI), `core/domain` (UseCases), `core/data` (Repositories), `core/database` (Room), `core/datastore` (Proto DataStore), `core/designsystem` (Theme)
- **i18n**: 5 locales (en, zh-CN, zh-TW, ja, ko). Use `/i18n` skill when adding strings.
- **Verification**: Use `/verify` skill to run lint + tests before committing.

## Gotchas

- Build variants: `demo`/`prod` flavors × `debug`/`release` types. Default dev variant is `demoDebug`.
- Room uses `fallbackToDestructiveMigration(dropAllTables = true)` in dev — provide proper `Migration` for production schema changes.
- Firebase/google-services plugin is commented out in build files.
- Versions: Kotlin 2.3.20, AGP 9.0.1, Gradle 9.1.0, Target SDK 36, Min SDK 26.
