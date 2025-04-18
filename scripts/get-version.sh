#!/bin/sh

#
# Copyright (c) 2025. Fairphone B.V.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
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