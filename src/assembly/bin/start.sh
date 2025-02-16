#!/usr/bin/env bash
java -cp lib/*:../../conf/rest/ -Dspring.profiles.active=prod ${start-class}
