#!/bin/sh

USER=$1
PROJECT=$2
MODULE=$3
ROOT=/home/travis/build/$USER
#ROOT=.
LINT=$ROOT/$PROJECT/$MODULE/build/outputs/lint-results-debug.html
TEST_DIR=$ROOT/$PROJECT/$MODULE/build/reports/tests/debug
TEST=$TEST_DIR/index.html

echo ""
echo "********** Lynx Dump **********"
echo ""
echo "========== Lint =========="
echo ""

if [ -f $LINT ]; then
    lynx -dump $LINT
fi

echo ""
echo "========== Tests =========="
echo ""

if [ -f $TEST ]; then
    #lynx -dump $TEST
    find $TEST_DIR -name "*.html" -exec bash -c "lynx -dump {} && echo "" && echo "---------" && echo "" " \;
fi

#find $ROOT/$PROJECT/$MODULE/build/ -name "*.xml"
