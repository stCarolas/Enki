#! /bin/sh
#
# build.sh
# Copyright (C) 2019 stCarolas <stCarolas@carbon>
#
# Distributed under terms of the MIT license.
#

cd modules
for file in ./*
do
    cd $file
    mvn clean package install
    cd ..
done
