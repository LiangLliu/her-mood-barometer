# Project Rules

## Always-On Constraints
- Use `jakarta.inject` (not `javax.inject`)
- Use `Timber` (never `android.util.Log`)
- Use `stringResource(R.string.xxx)` (never hardcoded strings)
- Use sealed interface for UiState (Loading/Success/Error)
- Never log identifiable content or diary notes (even with Timber)
- Local storage: prefer encryption (Keystore / EncryptedFile)
- Cloud backup: require explicit user consent + E2E encryption
- ViewModel injects UseCases, not Repositories

## Commit
- Never commit unless user says "提交" or "commit"
- Never include `Co-Authored-By:` in commit messages
- Conventional Commits in English: `feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `chore:`
- Exclude `docs/superpowers/` from commits
- Stage specific files by name, never `git add -A` or `git add .`

## Code Navigation — LSP First
For Kotlin/Java files, prefer LSP over Grep/Read:
- `documentSymbol` → understand file structure (classes, methods, properties)
- `workspaceSymbol` → find symbols across modules
- `goToDefinition` / `goToImplementation` → trace code flow
- `hover` → get type info and documentation
- `incomingCalls` / `outgoingCalls` → understand call chains
- `findReferences` → find all usages of a symbol

If LSP returns errors or incomplete results (server not ready, index not built), fall back to Grep/Read.

## Dynamic Docs — Read on demand
When working on a topic below, **read the corresponding doc first** before making changes:

| Topic | Read |
|-------|------|
| Colors, themes, typography, icon, splash | `docs/DESIGN_SYSTEM.md` |
| UI mockup reference | `docs/design/preview.html` |
| Icon design reference | `docs/design/icon_preview.html` |
| Version history, what's been done | `docs/CHANGELOG.md` |
