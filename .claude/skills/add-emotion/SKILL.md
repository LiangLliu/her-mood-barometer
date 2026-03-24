---
name: add-emotion
description: Add a new predefined emotion to the app. Use when user wants to add a new mood/emotion type, mentions adding emoji or emotion entries, or asks to expand the emotion list. Triggers on phrases like "add emotion", "new mood", "add feeling".
---

Add a new predefined emotion across all required files. Missing any file will cause runtime display issues.

## Required Input

From the user (ask if not provided):
- **resourceKey**: lowercase English identifier (e.g., `hopeful`)
- **emoji**: single emoji character (e.g., `🌟`)
- **name + description in 5 languages** (en, zh-CN, zh-TW, ja, ko)
- **color palette**: which mood group it belongs to (read existing mapping for options)

## Workflow

Every step below starts with **discovering the actual file** to match its current structure. Never assume file paths or code patterns.

Use LSP first for Kotlin files (`documentSymbol` to understand structure, `workspaceSymbol` to find symbols). Fall back to Grep/Read if LSP is unavailable.

### Step 1: PredefinedEmotions

1. Find: use `workspaceSymbol` to locate `PredefinedEmotions`, or fall back to `Grep for "PredefinedEmotion" in core/model/`
2. Use `documentSymbol` to understand the file structure, then Read to see existing entries, max `id` and `sortOrder`
3. Add a new entry following the exact same pattern, incrementing `id` and `sortOrder`

### Step 2: Locale Strings (5 files)

1. Find string files: `Glob for "strings.xml" in core/locales/src/main/res/`
2. Read `values/strings.xml`, find the `emotion_*` section, note the naming pattern and grouping (Positive / Negative / Neutral)
3. Add two strings per locale:
   - `emotion_{resourceKey}` — display name
   - `emotion_{resourceKey}_desc` — description
4. Repeat for all 5 locale files, ensuring same key order
5. Translations should be natural and native-sounding (not literal machine translation)

### Step 3: Emotion Mappers

1. Find: use `workspaceSymbol` to locate `getEmotionNameResourceId`, or fall back to `Grep in core/data/`
2. Use `documentSymbol` to see all mapper functions, then Read the `when` blocks that map resourceKey → R.string
3. Add the new resourceKey case to **both** `when` blocks, following the existing pattern

### Step 4: Color Mapping

1. Find: use `workspaceSymbol` to locate `moodColor`, or fall back to `Grep in core/designsystem/`
2. Use `documentSymbol` on the file to see ExtendedColorScheme structure, then Read `moodColor()` for existing mappings
3. Decide: add to an existing group (comma-separated), add a new case, or rely on the `else` default
4. The available palettes and their mood associations are defined in the same file — read them

### Step 5: Verify

Run `/verify` to check compilation. Common mistakes:
- Duplicate `id` in PredefinedEmotions
- Missing string key in one of the 5 locale files
- Missing case in mapper (causes name to display as empty)
