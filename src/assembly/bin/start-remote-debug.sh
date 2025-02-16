#!/usr/bin/env bash
nohup java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=4001,suspend=n -cp lib/*:../../conf/rest-template/ -Dspring.profiles.active=prod ${start-class} &
