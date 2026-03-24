# Her Mood Barometer

**她的晴雨表** — A beautiful mood tracker with journal-like design, mood trends, and multi-language support.

## Features

- Record daily emotions with intensity levels and diary notes
- Calendar view with mood visualization
- Statistics and trend analysis (line + bar charts)
- 20+ predefined emotions with custom emotion support
- Daily reminder notifications with warm, encouraging messages
- 5 language support (English, 简体中文, 繁體中文, 日本語, 한국어)
- 3 color schemes (Warm Sun / Azure Sea / Flower Whisper) + Dynamic theming
- Light and dark mode

## Tech Stack

- **Language**: Kotlin 2.3.20
- **UI**: Jetpack Compose + Material 3
- **Architecture**: Clean Architecture (UI → Domain → Data), MVVM
- **DI**: Hilt (jakarta.inject)
- **Database**: Room
- **Preferences**: Proto DataStore
- **Networking**: Ktor
- **Build**: Gradle 9.1.0, AGP 9.0.1, Convention Plugins
- **Min SDK**: 26 / **Target SDK**: 36

## Architecture

```
app/                          # Application entry point, navigation
feature/                      # Feature modules (UI layer)
  calendar/                   # Calendar mood view
  diary/                      # Diary entries list
  record/                     # Emotion recording
  settings/                   # App settings & emotion management
  statistics/                 # Mood statistics & charts
core/                         # Shared modules
  analytics/                  # Analytics abstraction
  common/                     # Dispatchers, utilities
  data/                       # Repository implementations
  database/                   # Room database, DAOs, entities
  datastore/                  # Proto DataStore preferences
  designsystem/               # Theme, colors, typography
  domain/                     # UseCases (business logic)
  locales/                    # String resources (5 locales)
  model/                      # Data models
  network/                    # Ktor network layer
  notifications/              # Notification system
  permissions/                # Runtime permission handling
  shortcuts/                  # App shortcuts
  ui/                         # Shared UI components
build-logic/                  # Convention plugins
```

## Build

```bash
./gradlew assembleDemoDebug       # Development build
./gradlew assembleProdRelease     # Release build
./gradlew test                    # Unit tests
./gradlew lint                    # Lint checks
```

Requires **JDK 21** and **4GB+ heap**.

## Design System

The app embraces a **Warm Journal / Organic Diary** aesthetic — soft edges, warm tones, and paper-like textures. Typography pairs **Noto Serif SC** (headings) with the system default font (body text).

See [docs/DESIGN_SYSTEM.md](docs/DESIGN_SYSTEM.md) for the full specification.

## License

[MIT License](LICENSE)
