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

## Commit Convention

Use **Conventional Commits** in English: `feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `chore:`.

## Architecture Rules

- **Clean Architecture**: UI only knows `UiState` + events; business logic in UseCases (`core/domain`); data in Repositories (`core/data`).
- **MVVM + Compose**: ViewModels expose `StateFlow`, collect with `collectAsStateWithLifecycle()`.
- **Hilt DI**: Constructor injection preferred. Each module has its own Hilt module.

## UI Rules

- **Page containers**: Always use `ScreenContainer` / `FullScreenContainer` / `ScreenContainerWithBack` / `SimpleScreenContainer`. Never put `PageTitle` inside `LazyColumn`.
- **Previews**: All UI components must have `@Preview` functions.
- **Text**: Always use `stringResource(R.string.xxx)` — never hardcoded strings.
- **Charts (Vico)**: Use `CartesianChartHost` + `rememberCartesianChart()` + `CartesianChartModelProducer`. Columns via `rememberColumnCartesianLayer()`, lines via `rememberLineCartesianLayer()`.
- **Design System**: 3 color schemes (`WARM`/`OCEAN`/`PETAL`) + `DYNAMIC` via `ColorSchemeConfig` enum. Extended colors via `ExtendedTheme.colors`. Mood colors via `ExtendedColorScheme.moodColor(resourceKey)`.
- **Fonts**: Display/Headline/Title use `NotoSerifSCFamily` (Google Fonts); Body/Label use `FontFamily.Default`.

## Internationalization

5 languages are supported: **zh-CN**, **zh-TW**, **en**, **ja**, **ko**. When adding or modifying any user-facing string:
1. Add to `core/locales/src/main/res/values/strings.xml` (default = English)
2. Add translations to all 4 locale variants: `values-zh-rCN`, `values-zh-rTW`, `values-ja-rJP`, `values-ko-rKR`

## Privacy & Security

This app handles sensitive emotion data. Never log identifiable content or long-form notes. Prefer local storage with encryption (Keystore / EncryptedFile). Cloud backup requires explicit user consent + E2E encryption.

## Gotchas

- Build variants: `demo`/`prod` flavors × `debug`/`release` types. Default dev variant is `demoDebug`.
- Room uses `fallbackToDestructiveMigration()` in dev — provide proper `Migration` for production schema changes.
- Firebase/google-services plugin is commented out in build files.
- Subdirectory `CLAUDE.md` files can be added for module-specific instructions (auto-loaded when working in those directories).
- `ColorSchemeConfig` replaces the old `useDynamicColor: Boolean` throughout the data layer (Proto DataStore → Repository → ViewModel → Theme).
- Versions: Kotlin 2.3.20, AGP 9.0.1, Gradle 9.1.0, Target SDK 36, Min SDK 26.