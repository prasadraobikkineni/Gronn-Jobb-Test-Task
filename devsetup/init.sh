#!/usr/bin/env bash

docker run --name elasticsearch -d -p 9200:9200 -p 9300:9300 \
-e 'discovery.type=single-node' -e 'xpack.security.enabled=false' -e 'ES_JAVA_OPTS=-Xms750m -Xmx750m' \
docker.elastic.co/elasticsearch/elasticsearch:7.4.2