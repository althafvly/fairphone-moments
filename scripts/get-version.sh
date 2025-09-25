#!/bin/sh

#
# Copyright (C) 2025 FairPhone B.V.
#
# SPDX-FileCopyrightText: 2025. FairPhone B.V.
#
# SPDX-License-Identifier: EUPL-1.2
#

getPropertyValue() {
  pattern="^$1:[[:space:]]*";
  ./gradlew properties | sed -n "/$pattern/s/$pattern//p";
}

# Returns the current version name
_getVersionName() {
  VERSION_MAJOR="$(getPropertyValue versionMajor)"
  VERSION_MINOR="$(getPropertyValue versionMinor)"
  VERSION_PATCH="$(getPropertyValue versionPatch)"
  echo $VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH
}

_main() {
  version="$(_getVersionName)"
  echo "Version: $(_getVersionName)"
}

_getVersionName