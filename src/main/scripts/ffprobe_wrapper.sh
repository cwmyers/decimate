#!/bin/sh

FFPROBE_BIN=$1
INPUT_VIDEO=$2

${FFPROBE_BIN} "${INPUT_VIDEO}" 2>&1