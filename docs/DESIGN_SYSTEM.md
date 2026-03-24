# Her Mood Barometer - Design System

## Design Philosophy

**Aesthetic Direction**: Warm Journal / Organic Diary

This app is an intimate emotional companion for women to track their moods. The design avoids cold, clinical UI patterns and instead embraces a warm, paper-like journal feel — as if writing in a beautiful diary by candlelight.

**Core Principles**:
- **Warmth over Precision**: Soft edges, warm tones, organic textures
- **Intimacy over Flashiness**: Quiet elegance, not loud gradients
- **Tactile over Flat**: Paper texture, subtle shadows, depth through layering
- **Personal over Generic**: Every element feels hand-picked, not template-generated

---

## Typography

| Role | Font | Weights | Usage |
|------|------|---------|-------|
| Display | `Noto Serif SC` | 400, 500, 600, 700 | Headings, mood names, calendar title |
| Body | `Noto Sans SC` | 300, 400, 500, 600 | Content text, labels, buttons |
| Fallback Display | `Songti SC`, Georgia, serif | — | macOS/iOS fallback |
| Fallback Body | `PingFang SC`, system | — | System fallback |

**Rationale**: Serif + Sans-Serif pairing creates visual hierarchy. Noto Serif SC gives a literary, journal-like quality to headings while Noto Sans SC keeps body text clean and readable.

---

## Color Schemes

The app supports **3 color schemes**, each with light and dark variants. Users can switch schemes via a one-tap color picker in the theme controls.

### Architecture

CSS custom properties are layered using data attributes on `<body>`:
- `data-color="warm"` (default, attribute absent) | `"ocean"` | `"petal"`
- `data-theme="dark"` (present or absent)

Selectors: `:root` (warm light), `[data-theme="dark"]` (warm dark), `[data-color="ocean"]`, `[data-color="ocean"][data-theme="dark"]`, etc.

### 1. 暖阳 (Warm Sun) — Default

**Personality**: Warm earth tones, like a candlelit journal. Intimate and cozy.

| Role | Light | Dark |
|------|-------|------|
| Background | `#FBF8F4` (warm cream) | `#1A1714` (warm charcoal) |
| Text Primary | `#2C2520` (warm black) | `#EDE6DD` (warm cream) |
| Accent | `#C4735B` (dusty rose) | `#D8896F` (bright terracotta) |
| Shadow tint | `rgba(44,37,32,...)` | `rgba(0,0,0,...)` |

**Accent Palette**:

| Name | Hex | Soft | Background | Mood |
|------|-----|------|------------|------|
| **Accent** | `#C4735B` | `#E8B4A2` | `#FFF0EB` | Primary action |
| **Sage** | `#7A9E7E` | `#C5DBC7` | `#EFF5F0` | Calm |
| **Amber** | `#C9A84C` | `#E8D9A0` | `#FDF8EC` | Happy |
| **Rose** | `#B8687B` | `#E0B4BF` | `#FBF0F3` | Sad |
| **Lavender** | `#8B7BB5` | `#C5BBD9` | `#F3F0F8` | Tired |

### 2. 碧海 (Azure Sea)

**Personality**: Cool, serene ocean tones. Like sitting on a seaside terrace at dawn.

| Role | Light | Dark |
|------|-------|------|
| Background | `#F4F8FA` (cool blue-white) | `#121A1E` (deep ocean) |
| Text Primary | `#1C2830` (cool charcoal) | `#DDE8EE` (cool cream) |
| Accent | `#3D8FA0` (teal) | `#52A8BA` (bright teal) |
| Shadow tint | `rgba(28,40,48,...)` | `rgba(0,0,0,...)` |

**Accent Palette**:

| Name | Hex | Soft | Background | Mood |
|------|-----|------|------------|------|
| **Accent** | `#3D8FA0` | `#8DC4D0` | `#E8F4F7` | Primary action |
| **Seafoam** | `#5BA88E` | `#A8D6C4` | `#EBF5F0` | Calm |
| **Coral** | `#D4946B` | `#E8C4A8` | `#FDF2EB` | Happy |
| **Muted Rose** | `#B07080` | `#D8B0B8` | `#F8EEF0` | Sad |
| **Slate** | `#7B85A8` | `#B4BAD0` | `#EDEEF5` | Tired |

### 3. 花语 (Flower Whisper)

**Personality**: Soft pink and rose tones. Romantic, delicate, like cherry blossoms.

| Role | Light | Dark |
|------|-------|------|
| Background | `#FBF6F8` (rosy white) | `#1C1418` (dark plum) |
| Text Primary | `#2C2028` (warm plum) | `#EEDDE6` (rosy cream) |
| Accent | `#C2789A` (dusty pink) | `#D88AAE` (bright pink) |
| Shadow tint | `rgba(44,32,40,...)` | `rgba(0,0,0,...)` |

**Accent Palette**:

| Name | Hex | Soft | Background | Mood |
|------|-----|------|------------|------|
| **Accent** | `#C2789A` | `#E0B0C4` | `#FBEEF4` | Primary action |
| **Mint** | `#6EB5A0` | `#B0D8C8` | `#ECF6F2` | Calm |
| **Peach** | `#D4977B` | `#E8C8B0` | `#FDF2EC` | Happy |
| **Mauve** | `#A07AB8` | `#CCB0D8` | `#F4EEF8` | Sad |
| **Steel Blue** | `#8895B0` | `#B8C0D4` | `#EFF1F6` | Tired |

### Color Tier System

Each accent color has three tiers across all schemes:
1. **Full** — icons, borders, active states
2. **Soft** — subtle accents, gradient endpoints
3. **Background** — tinted surfaces, card fills

### Dark Mode Rules

All color schemes follow the same dark mode transformation:
- Backgrounds shift to scheme-tinted dark tones
- Text inverts to scheme-tinted cream
- Accents brighten slightly for dark-surface readability
- Shadows use higher opacity with `rgba(0,0,0,...)`

---

## Spacing & Radius

| Token | Value | Usage |
|-------|-------|-------|
| `--radius-sm` | `8px` | Small elements (calendar cells, chips) |
| `--radius-md` | `14px` | Medium elements (settings items, inputs) |
| `--radius-lg` | `20px` | Cards, diary entries |
| `--radius-xl` | `28px` | App frame, modal sheet |

---

## Shadows

| Token | Value | Usage |
|-------|-------|-------|
| `--shadow-sm` | Subtle lift | Card hover, tag hover |
| `--shadow-md` | Medium depth | Theme toggle, card hover |
| `--shadow-lg` | Strong depth | App frame |
| `--shadow-glow` | Accent glow | Selected emotion highlight |
| `--fab-glow` | FAB shadow | Idle FAB glow (scheme-colored) |
| `--modal-overlay` | Backdrop tint | Modal overlay background |
| `--save-glow` | Button shadow | Save button depth |

---

## Motion

| Animation | Duration | Easing | Usage |
|-----------|----------|--------|-------|
| `fadeUp` | 500ms | `cubic-bezier(0.16, 1, 0.3, 1)` | Page load staggered reveals |
| `fabBreathe` | 3s infinite | ease-in-out | FAB idle state, draws attention |
| Bar fill | 1200ms | ease-out | Statistics bar chart entry |
| Modal slide | 450ms | ease-out | Bottom sheet modal |
| Hover lift | 300ms | ease-out | Cards, diary entries |

**Staggered Reveals**: Child elements delay by 50ms increments (`0.05s` per child) for a natural cascade effect.

---

## Component Patterns

### Diary Entry
- Left color border (3px) indicates mood type
- Emoji in rounded square container
- 2-line note clamp with ellipsis
- Tag chips with hover state
- Hover slides entry right (`translateX(4px)`)

### Cards
- White/warm background with light border
- Uppercase label header with letter-spacing
- Hover lifts card 1px

### Bottom Sheet Modal
- Backdrop blur (8px) overlay
- Sheet slides up from bottom
- Drag handle indicator
- Section-based content layout

### Calendar
- 7-column grid, aspect-ratio cells
- Today highlighted with accent border
- Emoji below date number
- Adjacent month days at 25% opacity

### Settings
- Icon in tinted square (36x36, radius 10px)
- Title + description layout
- Toggle or arrow indicator
- Grouped with uppercase section headers

---

## Texture & Atmosphere

- **Paper Grain**: SVG noise filter overlay (`feTurbulence`, opacity 3%) on `body::before`
- **Warm Shadows**: Shadows use scheme-tinted rgba (warm brown / cool blue / rosy) instead of generic black
- **Gradient FAB**: FAB uses 135deg gradient from `accent` to `accent-soft`; save button uses solid `accent` for guaranteed white text contrast
- **Modal Overlay**: Uses `--modal-overlay` CSS variable — each scheme has its own tinted backdrop color matching the overall warmth/coolness

---

## Mood-to-Color Mapping

| Mood | Color Token | Emoji |
|------|------------|-------|
| Happy | `--amber` | 😊 |
| Calm | `--sage` | 😌 |
| Anxious | `--accent` | 😰 |
| Tired | `--lavender` | 😔 |
| Sad | `--rose` | 😢 |
| Touched | (no border) | 🥺 |
| Excited | `--accent` | 🤩 |
| Confused | `--lavender` | 😕 |
| Grateful | `--sage` | 🙏 |
| Lonely | `--rose` | 😔 |

---

## File Reference

- **Preview**: `preview.html` — Full interactive mockup with all 4 screens + 3 color schemes
- **Theme**: `core/designsystem/` — Compose theme implementation
- **Strings**: `core/locales/src/main/res/values*/strings.xml` — 5 locale variants

---

## Theme Switching Implementation

### Preview (HTML)

The preview uses CSS custom properties with `data-color` and `data-theme` attributes on `<body>`:
```js
// Color scheme
document.body.setAttribute('data-color', 'ocean'); // or 'petal', or remove for 'warm'

// Dark mode
document.body.setAttribute('data-theme', 'dark');  // or remove for light
```

Both are persisted to `localStorage` with keys `colorScheme` and `themeMode`.

### Android App (Planned)

The app should implement color scheme switching via:
1. `UserData.colorScheme` field in DataStore preferences
2. `ColorSchemeConfig` enum: `WARM`, `OCEAN`, `PETAL`
3. Compose theme maps the scheme to the correct `ColorScheme` in `HerMoodBarometerTheme`
4. Settings screen provides a visual color picker (3 colored circles)

---

## Design Review Notes

### Color Scheme Coverage Assessment

3 schemes are sufficient. They cover the three major aesthetic axes:

| Direction | Scheme | Season | Emotion Profile |
|-----------|--------|--------|----------------|
| Warm | 暖阳 | Autumn | Grounded, cozy |
| Cool | 碧海 | Summer | Calm, fresh |
| Soft | 花语 | Spring | Romantic, gentle |

Adding more schemes risks choice paralysis and maintenance burden. A 4th "dark-dominant" scheme (e.g. Starlight/星夜) could be considered for v2 if users request it.

### Key Design Decisions
- **Save button**: Solid `accent` color (not gradient) to guarantee white text contrast across all 6 theme combos
- **Modal overlay**: Uses `--modal-overlay` variable so each scheme has a tone-matched backdrop
- **`--bg-card-alt`**: Removed — was defined but never used in any component
- **Color dot identifiers**: Use fixed hex colors (not themed) so they serve as a stable visual anchor across all themes
