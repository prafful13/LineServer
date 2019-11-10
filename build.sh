#!/bin/bash

if ! [ -x "$(command -v redis-server)" ]; then
  echo 'Error: git is not installed.' >&2
  curl -O http://download.redis.io/redis-stable.tar.gz
  tar xvzf redis-stable.tar.gz
  cd redis-stable
  make
  make test
  make install
  cd ..
fi

mvn clean
nohup redis-server &

exit 0