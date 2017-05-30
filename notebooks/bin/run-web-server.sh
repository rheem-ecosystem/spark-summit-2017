#!/bin/bash

basedir="$(cd "$(dirname "$0")/.."; pwd)"
port="8889"
echo "Starting web server on port $port in $basedir"
(cd "$basedir" && python -m SimpleHTTPServer "$port")
