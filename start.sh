#!/bin/bash

cd core && mvn clean install && cd ..

docker-compose down
docker-compose build
docker-compose up