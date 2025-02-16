#!/usr/bin/env bash

rm -rf /opt/hampa/apps/rest-template/*
mkdir -p /opt/hampa/apps/rest-template/

#copy spring-boot-rest-template-x.x.x.tar.gx to /opt/hampa/apps/template/

cd /opt/hampa/apps/rest-template/ || exit

tar -xvf *.tar.gz
mkdir -p /opt/hampa/logs/rest-template/
sed -i 's/\r//' bin/*.sh

cp -f bin/spring-boot-rest-template.service /etc/systemd/system/
systemctl daemon-reload
systemctl enable spring-boot-rest-template.service
systemctl restart spring-boot-rest-template.service
