#!/usr/bin/env bash
nohup java -cp lib/*:../../conf/rest-template/ -Dspring.profiles.active=prod ${start-class} &
