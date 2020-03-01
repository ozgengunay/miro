#!/bin/bash -ex
echo "Starting server"
nohup java -jar /opt/miro-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &


