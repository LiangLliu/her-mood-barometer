#!/usr/bin/env bash
# Verify all locale string resource files have identical keys in identical order.
# Usage: ./scripts/check_i18n_consistency.sh
# Exit 0 = all consistent, Exit 1 = mismatch found.

set -euo pipefail

# Ensure script runs from project root
if [[ ! -f "core/locales/src/main/res/values/strings.xml" ]]; then
    echo "Error: must be run from project root directory (core/locales/src/main/res/values/strings.xml not found)"
    exit 1
fi

BASE="core/locales/src/main/res"
REF="$BASE/values/strings.xml"
LOCALES=("values-zh-rCN" "values-zh-rTW" "values-ja-rJP" "values-ko-rKR")

extract_keys() {
  grep '<string ' "$1" | sed 's/.*name="\([^"]*\)".*/\1/'
}

extract_array_keys() {
  grep '<string-array ' "$1" | sed 's/.*name="\([^"]*\)".*/\1/'
}

ref_keys=$(extract_keys "$REF")
ref_arrays=$(extract_array_keys "$REF")
ref_count=$(echo "$ref_keys" | wc -l | tr -d ' ')

errors=0

for locale in "${LOCALES[@]}"; do
  file="$BASE/$locale/strings.xml"
  if [ ! -f "$file" ]; then
    echo "❌ $locale: file not found"
    errors=$((errors + 1))
    continue
  fi

  other_keys=$(extract_keys "$file")
  other_count=$(echo "$other_keys" | wc -l | tr -d ' ')

  # Check count
  if [ "$ref_count" != "$other_count" ]; then
    echo "❌ $locale: string count mismatch (en=$ref_count, $locale=$other_count)"
    errors=$((errors + 1))
  fi

  # Check order
  diff_result=$(diff <(echo "$ref_keys") <(echo "$other_keys") || true)
  if [ -n "$diff_result" ]; then
    echo "❌ $locale: key order mismatch"
    echo "$diff_result" | head -20
    errors=$((errors + 1))
  else
    echo "✅ $locale: $other_count strings, order matches"
  fi

  # Check string-array keys
  other_arrays=$(extract_array_keys "$file")
  array_diff=$(diff <(echo "$ref_arrays") <(echo "$other_arrays") || true)
  if [ -n "$array_diff" ]; then
    echo "❌ $locale: string-array mismatch"
    echo "$array_diff"
    errors=$((errors + 1))
  fi
done

echo ""
echo "Reference (en): $ref_count strings"

if [ "$errors" -gt 0 ]; then
  echo "❌ Found $errors issue(s)"
  exit 1
else
  echo "✅ All locales consistent"
  exit 0
fi
