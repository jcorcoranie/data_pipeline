# data_pipeline

Starting Kafka Pipeline.

Getting Kafka.
https://kafka.apache.org/quickstart

tar -xzf kafka_2.12-2.3.0.tgz

cd dev/kafka_2.12-2.3.0/bin

— Start Zookeeper server:

 ./zookeeper-server-start.sh ../config/zookeeper.properties


— Start Kafka server:
 ./kafka-server-start.sh ../config/server.properties


— Create event log/message queue:
 ./kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic temperaturerecords

-- Monitor services.
 ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic viewrecords

 ./kafka-topics.sh --list --zookeeper localhost:2181
