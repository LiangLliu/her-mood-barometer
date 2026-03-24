---
name: ui-fix-dev-with-preview-html
description: Develop or fix Android Compose UI pages using the HTML design spec (preview.html) as the single source of truth. Use for both new page development and fixing existing design mismatches. Triggers when the user mentions preview.html, wants to build/develop/implement a page from design, compare design vs implementation, fix UI discrepancies, or says "develop this page", "implement this screen", "fix this page", "compare with design", "not matching design".
---

Develop or fix a page/component to match the design spec in `docs/design/preview.html`.

Works for both **new development** (building a page from scratch based on the design) and **fixing mismatches** (adjusting an existing page to match the design).

The core principle: **screenshots are for visual verification, HTML/CSS source is the spec.** Never guess values from images — extract exact CSS values from the HTML source and map them precisely to Compose.

## Step 1: Identify the target

Ask the user which page/component needs fixing, or infer from context. The user may provide:
- A phone screenshot (via `./scripts/screenshot.sh ./screencap/<name>.png`)
- A component name (e.g., "Quick Record dialog", "Settings page")
- A Figma or design reference

## Step 2: Capture the design screenshot

Use Playwright to open the HTML design spec and capture the target component:

```
1. Check if HTTP server is running:
   lsof -i :8765 2>/dev/null | grep LISTEN

2. If not running, start it:
   python3 -m http.server 8765 -d /path/to/project &>/dev/null &

3. Navigate:
   browser_navigate → http://localhost:8765/docs/design/preview.html

4. Interact to reveal the component (click tabs, buttons, etc.)

5. Take screenshot:
   browser_take_screenshot → save to screencap/design-<name>.png
```

### Playwright troubleshooting

If Playwright fails to launch Chrome ("Opening in existing browser session" error):
1. Kill the stale process: `pkill -f "mcp-chrome"` and wait 1 second
2. Remove lock file: `rm -rf ~/Library/Caches/ms-playwright/mcp-chrome-*/SingletonLock`
3. Retry `browser_navigate`

If Playwright still fails after retries, **skip the screenshot and go directly to Step 5** (reading HTML/CSS source). The HTML source is the authoritative spec anyway — screenshots are only for visual verification.

## Step 3: Capture the phone screenshot

```bash
./scripts/screenshot.sh ./screencap/<name>.png
```

If the user already provided a screenshot, skip this step.

## Step 4: Side-by-side comparison

Use Read to view both images and create a difference table:

```
| Element | Design | Implementation | Status |
|---------|--------|----------------|--------|
| Title   | "情绪日历" | "日历" | MISMATCH |
| Nav btn | 30x30 circle, border | 36x36, warmBg | MISMATCH |
| Stats   | "记录天数" / "15天" | "追踪天数" / "1" | MISMATCH |
| ...     | ...    | ...            | ...    |
```

Focus on: text content, layout structure, spacing, colors, typography, component presence/absence, **data format and display logic** (e.g., value suffixes like "天"/"次", computed vs raw values).

### Task tracking for multi-issue fixes

When the difference table has 5+ mismatches, create TaskCreate tasks to track progress:
- Group related fixes (e.g., "i18n strings", "calendar UI details", "stats card")
- Mark each task in_progress/completed as you go
- This keeps the user informed and prevents losing track

## Step 5: Extract CSS spec from HTML source

This is the critical step. Read `docs/design/preview.html` and extract exact CSS values for every mismatched element.

Read the relevant CSS classes and HTML structure. Record precise values:
- `padding`, `margin`, `gap` → dp values
- `font-size`, `font-weight`, `letter-spacing` → sp/weight values
- `border-radius` → RoundedCornerShape
- `background`, `color`, `border` → ExtendedTheme.colors mapping
- `display: flex/grid` → Row/Column/LazyVerticalGrid
- `flex-wrap` → FlowRow (custom)
- `opacity` → `Color.copy(alpha = N)`
- CSS variables like `var(--accent)` → map to ExtendedTheme.colors.accent

## Step 6: CSS → Compose mapping reference

Apply these precise mappings:

| CSS Property | Compose Equivalent |
|---|---|
| `padding: 7px 14px` | `Modifier.padding(horizontal = 14.dp, vertical = 7.dp)` |
| `border-radius: 100px` | `RoundedCornerShape(100.dp)` |
| `border-radius: 50%` | `CircleShape` |
| `border-radius: 14px` | `RoundedCornerShape(14.dp)` |
| `border: 1.5px solid X` | `Modifier.border(1.5.dp, color, shape)` |
| `border: 1px solid X` | `Modifier.border(1.dp, color, shape)` |
| `font-size: 12px` | `fontSize = 12.sp` |
| `font-weight: 500` | `fontWeight = FontWeight.Medium` |
| `font-weight: 600` | `fontWeight = FontWeight.SemiBold` |
| `letter-spacing: 0.5px` | `letterSpacing = 0.5.sp` |
| `line-height: 1` | `lineHeight = fontSize.sp` (same as font size) |
| `gap: 8px` | `Arrangement.spacedBy(8.dp)` |
| `margin-bottom: 22px` | `Modifier.padding(bottom = 22.dp)` |
| `display: grid; grid-template-columns: repeat(7,1fr)` | 7-column `Row` with `Modifier.weight(1f)` per cell |
| `display: flex; flex-wrap: wrap; gap: 8px` | `FlowRow(horizontalSpacing = 8.dp, verticalSpacing = 8.dp)` |
| `width: 100%; height: 48px` | `Modifier.fillMaxWidth().height(48.dp)` |
| `aspect-ratio: 1` | `Modifier.aspectRatio(1f)` |
| `opacity: 0.25` | `color.copy(alpha = 0.25f)` |
| `text-transform: uppercase` | `.uppercase()` |
| `cursor: pointer` (on div) | `Modifier.clickable { }` |
| `overflow-x: auto` / `overflow-x: scroll` | `Modifier.horizontalScroll(rememberScrollState())` |

### CSS variable → ExtendedTheme mapping

| CSS Variable | ExtendedTheme |
|---|---|
| `var(--accent)` | `colors.accent` |
| `var(--accent-bg)` | `colors.accentBg` |
| `var(--accent-soft)` | `colors.accentSoft` |
| `var(--bg-warm)` | `colors.warmBackground` |
| `var(--bg-card)` | `colors.cardBackground` |
| `var(--text-primary)` | `MaterialTheme.colorScheme.onSurface` |
| `var(--text-secondary)` | `colors.textSecondary` |
| `var(--text-muted)` | `colors.textMuted` |
| `var(--text-hint)` | `colors.textHint` |
| `var(--border)` | `colors.border` |
| `var(--border-light)` | `colors.borderLight` |
| `var(--font-display)` | `MaterialTheme.typography.titleLarge` (NotoSerifSC) |

## Step 7: Fix the code

1. **Strings**: Check if new string resources are needed. When adding multiple strings, batch edit all 5 locale files in parallel (zh-CN, zh-TW, en, ja, ko). Use `/i18n` skill for reference.
2. **Compose code**: Apply CSS spec values precisely — do not approximate.
3. **Keep comments**: Add CSS spec comments inline (e.g., `// CSS: .cal-day-num 12px text-secondary`) for future reference.

### When the design spec itself needs updating

The design spec and code are a **two-way sync** — sometimes code matches design, sometimes design needs to evolve.

**Design is outdated** — the implementation is correct but the spec is stale:
- Edit `docs/design/preview.html` to match reality
- Explain the reasoning to the user before changing

**Design-first development** — building a new feature or redesigning an existing page:
1. Analyze the current implementation and identify issues (fake data, missing features, bad UX)
2. Update `docs/design/preview.html` with the new design (add CSS classes, HTML structure, JS interactions)
3. Verify the design spec visually with Playwright screenshot
4. Then implement the Compose code to match the updated spec (proceed to Step 8)

This "design first, code second" workflow is common for feature additions like adding a time range selector, restructuring a page layout, or removing components that relied on hardcoded data.

### Cleaning up orphaned strings after removing components

When you remove a Compose component (e.g., deleting a `PatternsCard`), its associated i18n strings become orphaned. After removal:
1. Use `Grep` to search for the string resource names (e.g., `pattern_morning`) across all `.kt` files
2. If no Kotlin code references them, remove the string entries from all 5 locale files
3. Run `./scripts/check_i18n_consistency.sh` to verify all locales stay in sync

## Step 8: Build and verify

```bash
./gradlew assembleDemoDebug
./scripts/check_i18n_consistency.sh
```

Both must pass. The build confirms Compose code compiles; the i18n check confirms all 5 locale files have the same string keys in the same order.

## Step 9: Re-screenshot and compare

Ask the user to open the target page/component on phone, then:

```bash
./scripts/screenshot.sh ./screencap/<name>.png
```

Read both the new phone screenshot and the design screenshot. Compare and report remaining differences. If there are issues, go back to Step 5 and fix.

## Common pitfalls

- **Do NOT use `Modifier.weight(1f)`** for chips/tags that should auto-size — this forces equal width and causes text wrapping. Use FlowRow instead.
- **Do NOT use `Dialog` centered layout** for bottom-sheet style modals unless the design explicitly shows centered.
- **Section title style**: Design uses 12sp/Medium/textMuted for section titles, not `titleMedium` (which is 16sp NotoSerif). Check `.card-label` CSS.
- **Always use `stringResource(R.string.xxx)`** — never hardcode user-facing text.
- **Calendar grids**: Use `Row` with `Modifier.weight(1f)` per cell (not LazyVerticalGrid) for 7-column calendar layouts — it renders all cells at once without lazy loading overhead.
- **Other-month days**: When a calendar grid shows days from adjacent months, render them with reduced opacity (`color.copy(alpha = 0.25f)`), matching `.cal-day.other { opacity: 0.25 }`.
- **Locale-aware formatting**: Use `stringResource` for date format patterns (e.g., `R.string.calendar_month_format`) so each locale gets the right format (en: "MMMM yyyy", zh: "yyyy年M月").
