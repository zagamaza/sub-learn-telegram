#!/bin/bash
set -e
set -o pipefail

if [ "$1" == "-v" ]; then
  VERSION=$2
else
  echo "unkown version"
  exit 1
fi

echo '$PROJECT_TAG=$VERSION' > ~/subl/properties/$PROJECT_TAG
sh ~/subl/start.sh
