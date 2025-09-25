#!/bin/sh

#
# Copyright (C) 2025 FairPhone B.V.
#
# SPDX-FileCopyrightText: 2025. FairPhone B.V.
#
# SPDX-License-Identifier: EUPL-1.2
#

#
# Change the build version to $NEW_VERSION and commit it to the branch
# this pipeline is running for.

# Script should be called with one parameter
if [ $# -lt 1 ]; then
    echo "usage: $0 X.Y.Z"
    exit 0
fi

# Check if parameter is a valid version
echo "Validating version number: $1"
# if echo $1 | grep -Pq '^\d+\.\d+\.\d+$';
# then
    echo "Version number is valid"
# else
#     echo "Version number not valid, it must follow the pattern: X.Y.Z"
#     exit 0
# fi

set -u
set -e


NEW_VERSION=$1
GRADLE_PROPERTIES_FILE="gradle.properties"
VERSION_MAJOR_DEFINITION="versionMajor="
VERSION_MINOR_DEFINITION="versionMinor="
VERSION_PATCH_DEFINITION="versionPatch="


_find_last_version_change_commit() {
    git blame --incremental -p "-L/${VERSION_MAJOR_DEFINITION}/,+3" -- "${GRADLE_PROPERTIES_FILE}"  | head -n1 | cut -d" " -f1
}


_list_changelog_since_commit() {
    local hash="$1"
    git log --format=format:"%w(0,0,2)- %s %b" "${hash}.."
}


_change_version() {
    echo "Updating version to ${NEW_VERSION}"
    local changelog="$1"

    TMP_FILE="$(mktemp)"
    VERSION_MAJOR=$(echo ${NEW_VERSION} | cut -d "." -f 1)
    VERSION_MINOR=$(echo ${NEW_VERSION} | cut -d "." -f 2)
    VERSION_PATCH=$(echo ${NEW_VERSION} | cut -d "." -f 3)

    cat "${GRADLE_PROPERTIES_FILE}" \
          | sed '/'"${VERSION_MAJOR_DEFINITION}"'/s/[0-9][0-9]*/'"${VERSION_MAJOR}"'/' \
          | sed '/'"${VERSION_MINOR_DEFINITION}"'/s/[0-9][0-9]*/'"${VERSION_MINOR}"'/' \
          | sed '/'"${VERSION_PATCH_DEFINITION}"'/s/[0-9][0-9]*/'"${VERSION_PATCH}"'/' \
          > "${TMP_FILE}"

    mv "${TMP_FILE}" "${GRADLE_PROPERTIES_FILE}"
}


_commit_changes() {
    local changelog="$1"

    # Configure git user.
    git config user.name "Gitlab CI"
    git config user.email gitlab-ci@fairphone.com

    # Commit and push the version change to the same branch this
    # pipeline is running for.
    git add "${GRADLE_PROPERTIES_FILE}"
    git commit \
        -m "Update version to ${NEW_VERSION}" \
        -m "Change log:" -m "$changelog" #\
    git tag -a ${NEW_VERSION} \
        -m "Release ${NEW_VERSION}" \
        -m "Change log:" -m "$changelog"
}


_push_changes() {
    echo "remote url: ${CI_REPOSITORY_URL}"
    echo "ref HEAD:${CI_COMMIT_REF_NAME}"
    git push "https://gitlab-ci-token:${CI_PIPELINE_ACCESS_TOKEN}@${CI_SERVER_HOST}/${CI_PROJECT_PATH}.git" HEAD:${CI_COMMIT_REF_NAME} --tags
}


_main() {
    local changelog=
    changelog="$(_list_changelog_since_commit "$(_find_last_version_change_commit)")"
    echo "Changelog: $changelog"
    _change_version "$changelog"
    _commit_changes "$changelog"
    _push_changes
}


_main
