#!/usr/bin/env sh
DIR="$(cd "$(dirname "$0")" && pwd)"

if [ -z "$GRADLE_HOME" ]; then
    gradle_exe=gradle
else
    gradle_exe="$GRADLE_HOME/bin/gradle"
fi

exec "$gradle_exe" --project-dir "$DIR" "$@"
