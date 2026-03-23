---
name: i18n
description: Add or update internationalized strings across all 5 supported locales (zh-CN, zh-TW, en, ja, ko). Use when adding new user-facing text or modifying existing strings.
---

This project supports 5 locales. All string resources live under `core/locales/src/main/res/`.

## Locale Files

| Locale | Path |
|--------|------|
| English (default) | `values/strings.xml` |
| Simplified Chinese | `values-zh-rCN/strings.xml` |
| Traditional Chinese | `values-zh-rTW/strings.xml` |
| Japanese | `values-ja-rJP/strings.xml` |
| Korean | `values-ko-rKR/strings.xml` |

## Workflow

When the user provides new strings or asks to add UI text (via `$ARGUMENTS` or in conversation):

1. **Add the English default** to `values/strings.xml`
2. **Translate** to all 4 other locales with natural, native-sounding translations (not machine-literal)
3. **Add translations** to each locale's `strings.xml`
4. **Verify** all 5 files have the same string keys in the same order
5. **Remind** the user to use `stringResource(R.string.xxx)` in Compose code

## Translation Guidelines

- zh-CN: Use mainland Simplified Chinese conventions
- zh-TW: Use Taiwan Traditional Chinese conventions (not just character conversion)
- ja: Use polite/standard Japanese (desu/masu form for UI)
- ko: Use standard Korean honorific form

## Bulk Update

If modifying existing strings, read all 5 files first to ensure consistency, then update all of them together.
