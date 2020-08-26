#!/usr/bin/env bash

function onError {
    echo "Failed!"
    exit 1
}
set -e
trap onError ERR

USAGE="This script manage project version. It must be run from project root dir.
$0 [--patch | --minor | --major] [--just-print]
    <no param>: prints current version
    --patch: increment patch number. Do not combine with --minor or --major.
    --minor: increment minor number, set patch to zero. Do not combine with --patch or --major.
    --major: increment major number, set minor and patch to zero. Do not combine with --patch or --minor.
    --just-print: do not change version. When combined with patch, minor or major allows to check next version without changing it
    ? -h --help: prints this message"

VERSION_FILE_NAME='version'

version="$(cat ${VERSION_FILE_NAME})"

justPrint=false

while [[ -n "$1" ]]; do
    case "$1" in
    --patch)
        if [[ -z "${type}" ]]; then
            type='patch'
        else
            echo "${USAGE}"
            exit 1
        fi
    ;;
    --minor)
        if [[ -z "${type}" ]]; then
            type='minor'
        else
            echo "${USAGE}"
            exit 1
        fi
    ;;
    --major)
        if [[ -z "${type}" ]]; then
            type='major'
        else
            echo "${USAGE}"
            exit 1
        fi
    ;;
    --just-print)
        justPrint=true
    ;;
    *)
        echo "${USAGE}"
        exit 0
    ;;
    esac
    shift
done

IFS="." command eval 'version_arr=(${version})'

if [[ "${#version_arr[@]}" -ne 3 ]]; then
     echo "Content of file: ${VERSION_FILE_NAME} does not meet standard of SemVer!"
     echo "version=${version}"
     exit 1
fi
major=${version_arr[0]}
minor=${version_arr[1]}
patch=${version_arr[2]}
is_number='^0$|^[1-9][0-9]*$'
if ! ([[ ${major} =~ ${is_number} ]] && [[ ${minor} =~ ${is_number} ]] && [[ ${patch} =~ ${is_number} ]]) ; then
     echo "Content of file: ${VERSION_FILE_NAME} does not meet standard of SemVer!"
     echo "version=${version}"
     exit 1
fi

case "${type}" in
    '')
        echo "${version}"
        exit 0;
    ;;
    patch)
        patch=$((patch+1))
    ;;
    minor)
        minor=$((minor+1))
        patch=0
    ;;
    major)
        major=$((major+1))
        minor=0
        patch=0
    ;;
esac

newVersion="${major}.${minor}.${patch}"
echo "${newVersion}"
if [[ "${justPrint}" = false ]] ; then
    echo "${newVersion}" > ${VERSION_FILE_NAME}
fi
