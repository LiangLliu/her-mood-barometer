---
name: add-feature
description: Scaffold a new feature module with ViewModel, Screen, and Navigation following project conventions. Use when user wants to create a new screen, add a new page/tab, or build a new feature module. Triggers on "new screen", "add feature", "create page", "new module".
---

Create a new feature module by reading existing feature modules and replicating their pattern exactly. Never hardcode templates — always derive structure from the current codebase.

Use LSP first for Kotlin files (`documentSymbol` to understand structure, `workspaceSymbol` to find symbols). Fall back to Grep/Read if LSP is unavailable.

## Required Input

From the user (ask if not provided):
- **name**: lowercase feature name (e.g., `profile`)
- **title string**: display title in 5 languages
- **is bottom nav tab?**: whether it appears in the bottom navigation bar
- **data needs**: what UseCases or Repositories it depends on

## Workflow

### Step 0: Read an Existing Feature as Reference

Pick an existing simple feature module as reference (e.g., `feature/diary/` or `feature/statistics/`):

1. Use `documentSymbol` on the ViewModel — quickly see UiState variants, methods, injected deps
2. Use `documentSymbol` on the Screen — see composable structure and parameters
3. Use `documentSymbol` on the Navigation file — see Route objects and extension functions
4. Read each file for full details (imports, annotations, patterns)
5. Read `settings.gradle.kts`, `app/build.gradle.kts`, `AppNavHost.kt` for registration patterns

### Step 1: Register Module

Add `include(":feature:{name}")` to `settings.gradle.kts`, following the existing pattern.

### Step 2: Create build.gradle.kts

Create `feature/{name}/build.gradle.kts` by replicating the reference feature's build file. Adjust:
- `namespace` to match the new feature name
- Dependencies based on what the feature actually needs

### Step 3: Create ViewModel

Create the ViewModel file following the reference. Key conventions to preserve:
- Sealed interface `{Name}UiState` with Loading/Success/Error variants
- `@HiltViewModel` annotation
- `jakarta.inject.Inject` (never javax)
- `StateFlow<UiState>` via `asStateFlow()`
- Inject UseCases, not Repositories

### Step 4: Create Screen

Create the Screen composable following the reference. Key conventions:
- `hiltViewModel()` for ViewModel injection
- `collectAsStateWithLifecycle()` for state collection
- Use the appropriate screen container (read `ScreenContainer.kt` in `core/ui/` to see available options)
- All text via `stringResource()`
- Accept `modifier: Modifier = Modifier` parameter

### Step 5: Create Navigation

Create the navigation file following the reference. Key conventions:
- `@Serializable object {Name}BaseRoute` and `@Serializable object {Name}Route`
- `NavGraphBuilder.{name}Screen()` extension function
- `NavController.navigateTo{Name}()` extension function
- Nested navigation graph: `navigation<BaseRoute>(startDestination = Route)`

### Step 6: Wire Into App

1. Add `implementation(projects.feature.{name})` to `app/build.gradle.kts`
2. Add `{name}Screen()` call inside `NavHost { }` in `AppNavHost.kt`

### Step 7: (If Bottom Nav Tab) TopLevelDestination

1. Use `workspaceSymbol` to find `TopLevelDestination`, or fall back to `Grep in app/`
2. Use `documentSymbol` to see existing enum entries, then Read for full pattern
3. Add a new enum entry following the existing pattern, with appropriate icons from `AppIcons`

### Step 8: Add Title String

Use `/i18n` skill to add the navigation title string (e.g., `nav_{name}`) to all 5 locales.

## Verification

Run `/verify` after creation. Common issues:
- Forgot module registration in `settings.gradle.kts`
- Wrong namespace in `build.gradle.kts`
- Missing `@HiltViewModel` annotation
- Used `javax.inject` instead of `jakarta.inject`
