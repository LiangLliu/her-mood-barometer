# Changelog

All notable changes to this project are documented here. Detailed history is in git log.

## [Unreleased]

### 2026-03-24: Code Quality + Visual Refresh

**Code Quality (14 optimizations)**
- javax.inject -> jakarta.inject full migration (Hilt 2.59.2+)
- ProGuard rules for Room, Proto DataStore, kotlinx.serialization
- Backup rules: exclude DB/datastore from cloud, allow device transfer
- Room `fallbackToDestructiveMigration(dropAllTables = true)`
- N+1 query fix: EmotionRecordDao GROUP BY replaces 5-loop query
- DiaryUiState: data class -> sealed interface (Loading/Success/Error)
- LazyColumn key optimization (CalendarScreen)
- Dispatchers.IO -> injected `@Dispatcher(AppDispatchers.IO)`
- EmotionManagementViewModel migrated to UseCases
- Hardcoded colors -> MaterialTheme semantic colors
- Hardcoded strings -> stringResource
- android.util.Log -> Timber (11 files, auto-included via convention plugin)

**Permission System**
- Removed `SCHEDULE_EXACT_ALARM` / `USE_EXACT_ALARM` (Google Play compliance)
- `AlarmManager.setWindow()` with 15-min window replaces exact alarms
- Notification permission: system dialog first, Settings fallback for permanent denial
- Boot receiver respects user preference (enabled state + custom time)
- Settings UI: error card when notifications blocked

**Notification Optimization**
- Custom heart icon (`ic_notification.xml`)
- Randomized daily content (8 pairs x 5 locales, day-based index)
- BigTextStyle + CATEGORY_REMINDER
- "Record Now" action button (deep link to quick-record)

**App Icon Redesign**
- WARM palette gradient background (#F5D4C4 -> #C4735B)
- White heart foreground with mood wave accent
- Monochrome layer for Android 13+ themed icons
- Removed 10 legacy webp files (minSdk 26 = always adaptive)

**Splash Screen Branding**
- `Theme.SplashScreen.IconBackground` with dusty rose icon circle
- Dark mode support (`values-night/colors.xml`)
- 220ms fade-out exit animation
- Cleaned up template colors (purple/teal)

**Documentation & Project Organization**
- Added `README.md` with project overview, architecture, and build instructions
- Replaced 350+ line `DEVELOPMENT_PLAN.md` with concise `docs/CHANGELOG.md`
- Moved `preview.html` and `icon_preview.html` to `docs/design/`
- Added `.claude/rules/` and `.claude/skills/` (add-emotion, add-feature, i18n, verify)
- Updated `CLAUDE.md` with icon, splash, logging, permissions sections
- Deleted `.cursorrules` (outdated, superseded by `.claude/rules/`)
- Deleted unused `compose_compiler_config.conf`

### 2026-03-23: Design System Refactor

- 3 color schemes (WARM/OCEAN/PETAL) + DYNAMIC, each with light/dark
- `ColorSchemeConfig` enum replaces `useDynamicColor: Boolean` across full data layer
- `ExtendedColorScheme`: 22 semantic colors + MoodColorMapping
- Noto Serif SC (Google Fonts downloadable) for display/headline/title
- `ColorSchemeBottomSheet` picker in Settings
- Deleted 4 old TTF font files (~570KB APK savings)

### 2025-09-12: Unified Page Title System

- `ScreenContainer` / `ScreenContainerWithBack` / `FullScreenContainer` / `SimpleScreenContainer`
- Fixed titles with Material 3 TopAppBar, LazyColumn for content only

### 2025-09-06: Maintenance

- i18n consistency fix (189 strings x 5 locales)
- Material 3 extended color definitions
- Statistics custom date range fix

### 2025-08-27: Data Model Unification

- Merged `Emotion` and `CustomEmotion` into single model
- Emotion trend timeline replaces daily average intensity chart
- Database reset to v1 (dev phase)

### 2025-08-23: Emotion Model Refactor

- Removed `EmotionType` enum, unified `Emotion` data model
- Database migration v2 -> v3

### 2025-08-22: Statistics Redesign

- Dual chart layout (line + bar)
- Overview metrics as vertical list
- Error states and empty states
