#!/bin/bash

VEGETA_DIR="$(dirname "$0")"
RECOVERY_SECONDS=5  # time to wait between tests

# Read all non-comment, non-empty lines from targets.txt
grep -v '^#' "$VEGETA_DIR/targets.txt" | grep -v '^[[:space:]]*$' | while read -r url; do
    # Create a safe filename for each URL
    name=$(echo "$url" | sed 's|http[s]*://||; s|/|_|g; s|[^a-zA-Z0-9_]||g')

    echo "Running Vegeta attack for $url..."

    # Run attack for this URL
    echo "$url" | vegeta attack -duration=20s -rate=50 \
        | vegeta report

    # Wait between tests
    echo "Waiting $RECOVERY_SECONDS seconds before next test..."
    sleep $RECOVERY_SECONDS
    echo
done