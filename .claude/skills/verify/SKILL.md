---
name: verify
description: Run lint checks and unit tests to verify code quality before committing. Use after making changes to ensure nothing is broken.
---

Run the following verification steps in order. Stop at the first failure and report the issue.

## Step 1: Lint

```bash
./gradlew lint
```

If lint reports errors, list them with file paths and line numbers. Suggest fixes.

## Step 2: Unit Tests

```bash
./gradlew test
```

If tests fail, show the failing test names and error messages. Suggest fixes.

## Step 3: Report

After both steps pass, output a brief summary:
- Lint: PASS (number of warnings, if any)
- Tests: PASS (number of tests run)

If the user provides a specific module (e.g., `/verify :core:domain`), scope both lint and test to that module:
```bash
./gradlew :MODULE:lint :MODULE:test
```
