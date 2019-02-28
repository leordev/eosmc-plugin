#!/bin/sh

FLAGS="-Xms1G -Xmx2G -XX:+UseConcMarkSweepGC"
if [ "$1" == "--debug" ] 
then
  FLAGS="$FLAGS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
fi 

cd "$( dirname "$0" )"
java $FLAGS -jar spigot.jar
