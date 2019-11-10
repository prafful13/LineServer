#!/bin/bash

if [[ $# -eq 0 ]]
then
  echo "Run format: mvn spring-boot:run -Dspring-boot.run.arguments=FileName"
  exit 0
fi

mvn spring-boot:run -Dspring-boot.run.arguments=$1

exit 0
